/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * Userテーブル表
 */

package edu.udb.persistence.table

import javax.inject.*
import slick.jdbc.JdbcProfile
import ixias.core.model.*
import ixias.db.slick.{ SlickTable, SlickDatabaseContext }
import ixias.core.persistence.HostSpec
import edu.udb.model.User

/**
 * Table Definition: User (`udb_user`)
 */
@Singleton
class UserTable @Inject()(ctx: SlickDatabaseContext)
  extends SlickTable[User.Id, User, JdbcProfile](ctx):
  import api.*

  val ds = Map(
    HostSpec.PRIMARY -> DataSourceFactory("ixias.db.mysql://primary/app"),
    HostSpec.REPLICA -> DataSourceFactory("ixias.db.mysql://replica/app")
  )

  val query = TableQuery[Table]

  case class Table(tag: Tag) extends BasicTable(tag, "udb_user"):
    import User.*

    @pk  def id        = column[Id]            ("id",         O.UInt64, O.AutoInc, O.PrimaryKey)
    @col def uuid      = column[UUID]          ("uuid",       O.Varchar(64, Charset.Ascii))
    @col def email     = column[String]        ("email",      O.Varchar(255, Charset.Ascii))
    @col def name      = column[String]        ("name",       O.Varchar(255))
    @col def state     = column[Status]        ("state",      O.Int16)
    @col def updatedAt = column[LocalDateTime] ("updated_at", O.Timestamp(onUpdate = true))
    @col def createdAt = column[LocalDateTime] ("created_at", O.Timestamp)

    // uuidとemaiを一意にする処理
    def ukey01 = index("ukey01", uuid,  unique = true)
    def ukey02 = index("ukey02", email, unique = true)

    /**
     * The bidirectional mappings.
     * 1) Tuple(table) => Model
     * 2) Model        => Tuple(table)
     */
    def * = deriveColumns.mapTo[User](
      onWrite = _.copy(updatedAt = Now)
    )
