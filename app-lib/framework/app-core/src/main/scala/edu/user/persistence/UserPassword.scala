/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * Userパスワードリポジトリ
 */

package edu.user.persistence

import javax.inject.*
import scala.concurrent.Future
import ixias.db.slick.{ SlickBaseRepository, SlickDatabaseContext }
import ixias.core.persistence.HostSpec

import edu.user.model.User
import edu.user.persistence.table.UserPasswordTable

/**
 * Repository for UserPassword persistence (credentials).
 */
@Singleton
class UserPasswordRepository @Inject()(
  table: UserPasswordTable,
  ctx:   SlickDatabaseContext
) extends SlickBaseRepository(table, ctx):
  import api.*

  /**
   * Resolve a user's credential by user id (used at login).
   * パスワードをUserIdによって検索する
   */
  def findByUserId(uid: User.Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(HostSpec.REPLICA): slick =>
      slick
        .filter(_.uid === uid)
        .result.headOption
