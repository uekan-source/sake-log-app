/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * DrinkRecordテーブル表
 */

package edu.user.persistence.table

import java.time.LocalDate
import javax.inject.*
import slick.jdbc.JdbcProfile
import ixias.core.model.*
import ixias.db.slick.{ SlickTable, SlickDatabaseContext }
import ixias.core.persistence.HostSpec
import edu.user.model.{ User, Brand, DrinkRecord }

/**
 * DrinkRecord エンティティ ⇄ `drink_record` テーブル の対応表
 *
 * photo（URL と出所のセット）は photo_url / photo_source の 2 列に平坦化して持つ。
 * 「片方だけ入っている」は型（Photo）と DB の CHECK 制約の両方で防ぐ。
 */
@Singleton
class DrinkRecordTable @Inject()(ctx: SlickDatabaseContext)
  extends SlickTable[DrinkRecord.Id, DrinkRecord, JdbcProfile](ctx):
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
  case class Table(tag: Tag) extends BasicTable(tag, "drink_record"):
    import DrinkRecord.*

    @pk  def id          = column[Id]                  ("id",           O.UInt64, O.AutoInc, O.PrimaryKey)
    @col def uid         = column[User.Id]             ("uid",          O.UInt64)
    @col def brandId     = column[Brand.Id]            ("brand_id",     O.UInt64)
    @col def state       = column[Status]              ("state",        O.Int16)
    @col def drankAt     = column[Option[LocalDate]]   ("drank_at",     O.Date)
    @col def shopName    = column[Option[String]]      ("shop_name",    O.Varchar(255))
    @col def memo        = column[Option[String]]      ("memo",         O.Text)
    @col def photoUrl    = column[Option[String]]      ("photo_url",    O.Varchar(255, Charset.Ascii))
    @col def photoSource = column[Option[PhotoSource]] ("photo_source", O.Int16)
    @col def updatedAt   = column[LocalDateTime]       ("updated_at",   O.Timestamp(onUpdate = true))
    @col def createdAt   = column[LocalDateTime]       ("created_at",   O.Timestamp)

    // 写真ファイルは記録と 1:1 所有（共有しない。NULL は複数行あってよい）
    def ukey01 = index("ukey01", photoUrl, unique = true)
    def key01  = index("key01", uid)

    // 行 ⇄ DrinkRecord の相互変換。書き込みのたびに updatedAt を現在時刻にする
    def * = deriveColumns.mapTo[DrinkRecord](
      onWrite = _.copy(updatedAt = Now)
    )
