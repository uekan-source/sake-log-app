/*
 * DrinkRecordのエンティティモデル
 * 設計: docs/domain/drinkRecord/03_design.md
 */

package edu.user.model

import java.time.LocalDate

import ixias.core.model.*

/**
 * 記録: 酒との出会い 1 回分。
 *
 * 「気になる」（調べたが飲んでいない）も同じモデルの状態違い。
 * 写真は記録に付く（同じ銘柄でも飲むたびに撮る写真は別）。
 */
import DrinkRecord.*
case class DrinkRecord(
  id:        Option[Id],        // 管理Id
  userId:    User.Id,           // ユーザーId（アクセス境界の鍵）
  brandId:   Brand.Id,          // 銘柄Id
  state:     Status,            // 記録状態
  drankAt:   Option[LocalDate], // 飲んだ日（IS_DRUNK で埋まる。不明なら空）
  shopName:  Option[String],    // 店名（どこで）
  memo:      Option[String],    // 備考（本人の感想）
  photo:     Option[Photo],     // 写真（URL と出所のセット）
  updatedAt: LocalDateTime = Now, // データ更新日
  createdAt: LocalDateTime = Now  // データ作成日
) extends EntityModel[Id]

/**
 * 記録: 付随する型と処理の定義
 */
object DrinkRecord:

  // --[ Type Aliases ]------------------------------------------------
  type Id         = Id.Repr
  type WithNoId   = Entity.WithNoId[Id, DrinkRecord]
  type EmbeddedId = Entity.EmbeddedId[Id, DrinkRecord]

  // --[ Opaque Values ]-----------------------------------------------
  object Id extends Entity.Id[Long]

  // --[ Value Objects ]-----------------------------------------------
  /**
   * 記録状態
   */
  enum Status(val code: Short) extends EnumStatus[Short]:
    case IS_INTERESTED extends Status(code = 1) // 気になる（調べたが飲んでいない）
    case IS_DRUNK      extends Status(code = 2) // 飲んだ

  /**
   * 写真: URL と出所のセット。片方だけの状態を型で作れなくする
   */
  case class Photo(
    url:    String,     // 参照キー（推測不能なキー。直リンクの静的配信はしない）
    source: PhotoSource // 出所
  )

  /**
   * 写真の出所
   */
  enum PhotoSource(val code: Short) extends EnumStatus[Short]:
    case IS_SELF  extends PhotoSource(code = 1) // 自分が撮った
    case IS_FOUND extends PhotoSource(code = 2) // 知識源が探してきた
