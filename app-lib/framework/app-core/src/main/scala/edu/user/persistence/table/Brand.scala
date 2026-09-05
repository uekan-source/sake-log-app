/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * Brandテーブル表
 */

package edu.user.persistence.table

import javax.inject.*
import slick.jdbc.JdbcProfile
import ixias.core.model.*
import ixias.db.slick.{ SlickTable, SlickDatabaseContext }
import ixias.core.persistence.HostSpec
import edu.user.model.{ User, Brand }

/**
 * Brand エンティティ ⇄ `brand` テーブル の対応表
 */
@Singleton
class BrandTable @Inject()(ctx: SlickDatabaseContext)
  extends SlickTable[Brand.Id, Brand, JdbcProfile](ctx):
  import api.*

  // --[ データソース ]--------------------------------------------------
  // どの接続を使うか。書き込みは PRIMARY、読み取り（find/filter）は
  // REPLICA へ向けられるよう両方登録する
  val ds = Map(
    HostSpec.PRIMARY -> DataSourceFactory("ixias.db.mysql://primary/app"),
    HostSpec.REPLICA -> DataSourceFactory("ixias.db.mysql://replica/app")
  )

  // --[ テーブルクエリ ]------------------------------------------------
  val query = TableQuery[Table]

  // --[ テーブル定義 ]--------------------------------------------------
  case class Table(tag: Tag) extends BasicTable(tag, "brand"):
    import Brand.*

    @pk  def id        = column[Id]             ("id",         O.UInt64, O.AutoInc, O.PrimaryKey)
    @col def uid       = column[User.Id]        ("uid",        O.UInt64)
    @col def kind      = column[Kind]           ("kind",       O.Int16)
    @col def name      = column[String]         ("name",       O.Varchar(255))
    @col def variety   = column[Option[String]] ("variety",    O.Varchar(255))
    @col def region    = column[Option[String]] ("region",     O.Varchar(255))
    @col def taste     = column[Option[String]] ("taste",      O.Text)
    @col def trivia    = column[Option[String]] ("trivia",     O.Text)
    @col def state     = column[Status]         ("state",      O.Int16)
    @col def updatedAt = column[LocalDateTime]  ("updated_at", O.Timestamp(onUpdate = true))
    @col def createdAt = column[LocalDateTime]  ("created_at", O.Timestamp)

    // drink_record からの複合 FK（brand_id, uid）の参照先
    def ukey01 = index("ukey01", (id, uid), unique = true)
    def key01  = index("key01", uid)

    // 行 ⇄ Brand の相互変換。書き込みのたびに updatedAt を現在時刻にする
    def * = deriveColumns.mapTo[Brand](
      onWrite = _.copy(updatedAt = Now)
    )
