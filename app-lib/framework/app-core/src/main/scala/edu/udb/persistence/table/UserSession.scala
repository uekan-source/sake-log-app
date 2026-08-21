/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * Userセッションテーブル表
 */

package edu.udb.persistence.table

import javax.inject.*
import slick.jdbc.JdbcProfile
import ixias.core.model.*
import ixias.core.model.value.Token
import ixias.db.slick.{ SlickTable, SlickDatabaseContext }
import ixias.core.persistence.HostSpec
import edu.udb.model.{ User, UserSession }

/**
 * Table Definition: UserSession (`udb_user_session`)
 */
@Singleton
class UserSessionTable @Inject()(ctx: SlickDatabaseContext)
  extends SlickTable[UserSession.Id, UserSession, JdbcProfile](ctx):
  import api.{ given, * }

  val ds = Map(
    HostSpec.PRIMARY -> DataSourceFactory("ixias.db.mysql://primary/app"),
    HostSpec.REPLICA -> DataSourceFactory("ixias.db.mysql://replica/app")
  )

  val query = TableQuery[Table]

  case class Table(tag: Tag) extends BasicTable(tag, "udb_user_session"):
    import UserSession.*

    @pk  def id        = column[Id]            ("id",         O.UInt64, O.AutoInc, O.PrimaryKey)
    @col def uid       = column[User.Id]       ("uid",        O.UInt64)
    @col def token     = column[Token]         ("token",      O.Varchar(255, Charset.Ascii))
    @col def state     = column[Status]        ("state",      O.Int16)
    @col def expiresAt = column[LocalDateTime] ("expires_at", O.Timestamp)
    @col def updatedAt = column[LocalDateTime] ("updated_at", O.Timestamp(onUpdate = true))
    @col def createdAt = column[LocalDateTime] ("created_at", O.Timestamp)

    // uidは他の端末でも同時にログインできるように一意にしない
    def ukey01 = index("ukey01", token, unique = true)
    def key01  = index("key01", uid)

    /**
     * The bidirectional mappings.
     * 1) Tuple(table) => Model
     * 2) Model        => Tuple(table)
     */
    def * = deriveColumns.mapTo[UserSession](
      onWrite = _.copy(updatedAt = Now)
    )
