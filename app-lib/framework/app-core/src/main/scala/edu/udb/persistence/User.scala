/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * Userリポジトリ
 */

package edu.udb.persistence

import javax.inject.*
import scala.concurrent.Future
import ixias.db.slick.{ SlickBaseRepository, SlickDatabaseContext }
import ixias.core.persistence.HostSpec

import edu.udb.persistence.table.UserTable

/**
 * Repository for User persistence.
 */
@Singleton
class UserRepository @Inject()(
  table: UserTable,
  ctx:   SlickDatabaseContext
) extends SlickBaseRepository(table, ctx):
  import api.*

  /**
   * Resolve a user by login email (used by signup/login).
   * Userを検索するときにemailで検索するメソッド(emailは一意なため)
   */
  def findByEmail(email: String): Future[Option[EntityEmbeddedId]] =
    RunDBAction(HostSpec.REPLICA): slick =>
      slick
        .filter(_.email === email)
        .result.headOption
