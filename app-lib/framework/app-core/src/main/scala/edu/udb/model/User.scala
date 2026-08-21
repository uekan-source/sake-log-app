/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * Userのエンティティモデル
 */

package edu.udb.model

import ixias.core.model.*

/**
 * User: a registered account (profile only).
 */
import User.*
case class User(
  id:        Option[Id],                        // 管理 ID（永続化前は None）
  uuid:      UUID,                              // 公開用 UUID
  email:     String,                            // ログイン ID（一意）
  name:      String,                            // 表示名
  state:     Status        = Status.IS_ACTIVE,  // アカウント状態
  updatedAt: LocalDateTime = Now,               // データ更新日
  createdAt: LocalDateTime = Now                // データ作成日
) extends EntityModel[Id]

object User:

  // --[ Typedefs ]----------------------------------------------------
  type Id         = Id.Repr
  type UUID       = UUID.Repr
  type WithNoId   = Entity.WithNoId[Id, User]
  type EmbeddedId = Entity.EmbeddedId[Id, User]

  // --[ Objects ]-----------------------------------------------------
  object Id extends Entity.Id[Long]

  /** Public UUID identifier. */
  object UUID extends Entity.Id[String]:
    def generate: UUID = UUID(java.util.UUID.randomUUID.toString)

  // --[ Value Objects ]-----------------------------------------------
  /** Account status */
  enum Status(val code: Short) extends EnumStatus[Short]:
    case IS_INACTIVE extends Status(code = -1) // Inactive
    case IS_ACTIVE   extends Status(code =  1) // Active
