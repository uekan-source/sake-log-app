/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * Userセッションリポジトリ
 */

package edu.udb.persistence

import javax.inject.*
import scala.concurrent.Future
import ixias.core.model.value.Token
import ixias.db.slick.{ SlickBaseRepository, SlickDatabaseContext }

import edu.udb.persistence.table.UserSessionTable

/**
 * Repository for UserSession persistence (server-side login sessions).
 */
@Singleton
class UserSessionRepository @Inject()(
  table: UserSessionTable,
  ctx:   SlickDatabaseContext
) extends SlickBaseRepository(table, ctx):
  import api.*

  /**
   * Resolve a session by its cookie token (the raw, unsigned form).
   * トークン検索によってセッション見つける(レプリケーション遅延回避のためprimary指定)
   */
  def findByToken(token: Token): Future[Option[EntityEmbeddedId]] =
    RunDBAction: slick =>
      slick
        .filter(_.token === token)
        .result
        .headOption

  /**
   * Delete a session by its cookie token (logout). Returns the rows removed.
   * トークン検索によってセッション削除(レプリケーション遅延回避のためprimary指定) 
   */
  def deleteByToken(token: Token): Future[Int] =
    RunDBAction: slick =>
      slick
        .filter(_.token === token)
        .delete
