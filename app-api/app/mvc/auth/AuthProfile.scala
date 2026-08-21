/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package mvc.auth

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ Future, ExecutionContext }

import play.api.mvc.{ RequestHeader, Result }
import play.api.mvc.Results.Unauthorized

import ixias.core.model.value.Token
import ixias.core.security.Signer
import ixias.web.play.session.TokenManagerViaCookie
import ixias.web.play.session.AuthProfile as IxiasAuthProfile

import mvc.AppRepositoryFacade
import edu.udb.model.{ User, UserSession }

/**
 * Session authentication for the email/password login flow.
 *
 * Implements ixias's [[ixias.web.play.session.AuthProfile]] on top of
 * [[ixias.web.play.session.TokenManagerViaCookie]]: the cookie carries a
 * *signed* token (`{signature}-{nonce}-{token}`), while `udb_user_session`
 * stores the raw [[Token]]. A tampered cookie fails the HMAC check before any
 * query runs, and logout revokes the session server-side by deleting the row —
 * so the database stays the single source of truth.
 *
 * The three controllers use it like this:
 * {{{
 *   SignupController / LoginController  auth.open(uid)(result)      // 発行 + Cookie 付与
 *   GetMyProfileController              auth.resolveUser(request)   // 検証 + ユーザー解決
 *   LogoutController                    auth.close(request)(result) // 失効 + Cookie 破棄
 * }}}
 *
 * Cookie attributes (name / maxAge / path / domain / secure / httpOnly /
 * sameSite) are **not** hard-coded here — `TokenManagerViaCookie` reads them
 * from `cookie.session.*` in `conf/application.conf`, so per-environment
 * differences (`secure = true` behind HTTPS) are a config change only.
 *
 * The HMAC key comes from the [[Signer]] bound in [[mvc.AppModule]].
 */
@Singleton
class AuthProfile @Inject()(
  repos:  AppRepositoryFacade,
  signer: Signer,
) extends IxiasAuthProfile[User.EmbeddedId]:

  private given Signer = signer

  /** Namespace `session` → the `cookie.session.*` block in application.conf. */
  private val tokenManager = TokenManagerViaCookie("session")

  /**
   * Resolves the logged-in user from the request cookie.
   *
   * Three ways to fail, all 401: no cookie / bad signature (both reported by
   * `extract`), the session row is gone (logged out or never existed), or the
   * user behind the session no longer exists.
   */
  override def resolveUser(request: RequestHeader)
    (using ExecutionContext): Future[Either[Result, User.EmbeddedId]] =
    tokenManager.extract(request) match
      case Left(rejected) => Future.successful(Left(rejected))
      case Right(token)   =>
        repos.udb.userSession.findByToken(token).flatMap {
          case None          => Future.successful(Left(Unauthorized("The session is no longer valid")))
          case Some(session) =>
            repos.udb.user.find(session.v.uid).map {
              case None       => Left(Unauthorized("The session owner no longer exists"))
              case Some(user) => Right(user)
            }
        }

  /**
   * Opens a session for `uid`: stores a fresh token and attaches the signed
   * cookie to `result`. Called after signup and after a successful login.
   */
  def open(uid: User.Id)(result: Result)(using ExecutionContext): Future[Result] =
    val token = Token.generate
    repos.udb.userSession
      .add(UserSession(id = None, uid = uid, token = token).toWithNoId)
      .map(_ => tokenManager.put(token)(result))

  /**
   * Closes the request's session: deletes the row first (so the token is dead
   * even if the client keeps its copy), then discards the cookie. A missing or
   * unverifiable cookie is not an error here — logout stays idempotent.
   */
  def close(request: RequestHeader)(result: Result)(using ExecutionContext): Future[Result] =
    val revoked = tokenManager.extract(request) match
      case Right(token) => repos.udb.userSession.deleteByToken(token).map(_ => ())
      case Left(_)      => Future.unit
    revoked.map(_ => tokenManager.discard(result))
