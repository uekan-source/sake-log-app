/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 *　Userセッションエンティティモデル
 */

package edu.udb.model

import ixias.core.model.*
import ixias.core.model.value.Token

/**
 * UserSession: a server-side login session.
 *
 * After login succeeds a random [[Token]] is issued, stored here, and handed to
 * the client in an httpOnly cookie — signed, so the cookie value is the
 * `SignedToken` form (`{signature}-{nonce}-{token}`) while this table keeps the
 * raw token. Subsequent requests verify the signature, recover the raw token,
 * and resolve the user by looking it up here.
 */
import UserSession.*
case class UserSession(
  id:        Option[Id],                        // 管理 ID
  uid:       User.Id,                           // ユーザー ID
  token:     Token,                             // セッショントークン（未署名）
  state:     Status        = Status.IS_ACTIVE,  // セッション状態
  expiresAt: LocalDateTime = Now.plusDays(30),  // 有効期限
  updatedAt: LocalDateTime = Now,               // データ更新日
  createdAt: LocalDateTime = Now                // データ作成日
) extends EntityModel[Id]

object UserSession:

  // --[ Typedefs ]----------------------------------------------------
  type Id         = Id.Repr
  type WithNoId   = Entity.WithNoId[Id, UserSession]
  type EmbeddedId = Entity.EmbeddedId[Id, UserSession]

  // --[ Objects ]-----------------------------------------------------
  object Id extends Entity.Id[Long]

  // --[ Value Objects ]-----------------------------------------------
  /** Session status */
  enum Status(val code: Short) extends EnumStatus[Short]:
    case IS_CLOSED extends Status(code = -1) // Closed / invalidated
    case IS_ACTIVE extends Status(code =  1) // Active
