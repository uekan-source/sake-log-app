/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package controllers.auth

import javax.inject.Inject

import play.api.libs.json.Json

import mvc.{ AppControllerComponents, BaseAbstractController }

/**
 * 現在ログインしているユーザーをセッションクッキーから取得して返すコントローラー。
 * GET /user/api/me  →  200でユーザー情報を返す、またはログインしていない場合は401を返す。
 *
 * クッキー検証とセッションルックアップは [[mvc.auth.AuthProfile]] で行われ、
 * このコントローラーは戻ってきた `Either` の結果を返すだけである。
 *
 * Returns the currently logged-in user resolved from the session cookie.
 * GET /user/api/me  →  200 with the user, or 401 if not logged in.
 *
 * Cookie verification and session lookup live in [[mvc.auth.AuthProfile]]; this
 * controller only renders whichever side of the `Either` came back.
 */
class GetMyProfileController @Inject()(
  cc: AppControllerComponents,
) extends BaseAbstractController(cc):

  def invoke = Action.async: request =>
    auth.resolveUser(request).map {
      case Left(rejected) => rejected
      case Right(user)    =>
        Ok(Json.obj(
          "id"    -> user.id.value,
          "uuid"  -> user.v.uuid.value,
          "name"  -> user.v.name,
          "email" -> user.v.email,
        ))
    }
