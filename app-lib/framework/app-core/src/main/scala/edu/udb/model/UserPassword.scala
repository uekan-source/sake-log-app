/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * Userパスワードのエンティティモデル
 */

package edu.udb.model

import ixias.core.model.*
import ixias.core.security.PBKDF2

/**
 * UserPassword: a user's credential, kept separate from the [[User]] profile.
 * `hash` is a self-contained PBKDF2 hash string (salt + iterations + digest)
 * produced by `ixias.core.security.PBKDF2`. The raw password is never stored.
 */
import UserPassword.*
case class UserPassword(
  id:        Option[Id],           // 管理 ID
  uid:       User.Id,              // ユーザーID
  hash:      String,               // PBKDF2ハッシュ文字列
  updatedAt: LocalDateTime = Now,  // データ更新日
  createdAt: LocalDateTime = Now   // データ作成日
) extends EntityModel[Id]:

  /**
   * Verify a raw password against the stored PBKDF2 hash.
   * 実際に入力された生のパスワード(raw)とハッシュを比較している
   */
  def verify(raw: String): Boolean = PBKDF2.compare(raw, hash)

object UserPassword:

  // --[ Typedefs ]----------------------------------------------------
  type Id         = Id.Repr
  type WithNoId   = Entity.WithNoId[Id, UserPassword]
  type EmbeddedId = Entity.EmbeddedId[Id, UserPassword]

  // --[ Objects ]-----------------------------------------------------
  object Id extends Entity.Id[Long]

  /**
   * Build a new credential record, hashing the raw password with PBKDF2.
   * パスワードを生成する際にハッシュ化しているメソッド
   */
  def hashed(uid: User.Id, raw: String): WithNoId =
    UserPassword(
      id   = None,
      uid  = uid,
      hash = PBKDF2.hash(raw)
    ).toWithNoId
