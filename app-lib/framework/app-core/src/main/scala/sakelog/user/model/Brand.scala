/*
 * Brandのエンティティモデル
 * 設計: docs/domain/drinkRecord/03_design.md
 */

package sakelog.user.model

import ixias.core.model.*

/**
 * 銘柄: ユーザーごとに育つ、酒の銘柄情報。
 *
 * AI（知識源）が下書きし、ユーザーが確認・修正して確定する。
 * kind〜trivia の 6 欄が「AI が書ける欄」。再照会するとこの 6 欄は上書きされる。
 */
import Brand.*
case class Brand(
  id:        Option[Id],       // 管理Id
  userId:    User.Id,          // ユーザーId（アクセス境界の鍵）
  kind:      Kind,             // 種類
  name:      String,           // 銘柄名
  variety:   Option[String],   // 品種（kind が読み方を決める）
  region:    Option[String],   // 産地
  taste:     Option[String],   // 味わい
  trivia:    Option[String],   // 豆知識
  state:     Status,           // 確認状態（デフォルトを持たない。経路ごとに明示する）
  updatedAt: LocalDateTime = Now, // データ更新日
  createdAt: LocalDateTime = Now  // データ作成日
) extends EntityModel[Id]

/**
 * 銘柄: 付随する型と処理の定義
 */
object Brand:

  // --[ Type Aliases ]------------------------------------------------
  type Id         = Id.Repr
  type WithNoId   = Entity.WithNoId[Id, Brand]
  type EmbeddedId = Entity.EmbeddedId[Id, Brand]

  // --[ Opaque Values ]-----------------------------------------------
  object Id extends Entity.Id[Long]

  // --[ Value Objects ]-----------------------------------------------
  /**
   * 種類。深く扱うのはワインと日本酒
   */
  enum Kind(val code: Short, val name: String) extends EnumStatus[Short]:
    case IS_WINE  extends Kind(code = 1, name = "ワイン")
    case IS_SAKE  extends Kind(code = 2, name = "日本酒")
    case IS_OTHER extends Kind(code = 3, name = "その他") // 浅く受ける

  /**
   * 確認状態
   */
  enum Status(val code: Short) extends EnumStatus[Short]:
    case IS_UNCONFIRMED extends Status(code = 1) // 未確定（AI の推定のまま）
    case IS_CONFIRMED   extends Status(code = 2) // 確定（ユーザーが確認した）
