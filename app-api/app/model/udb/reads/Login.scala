/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * ログインのためのJSON翻訳ファイル
 */

package model.udb.reads

import io.circe.Decoder
import io.circe.generic.semiauto.*

/** Request body for user login. */
case class JsValueLogin(
  email:    String,
  password: String,
)

object JsValueLogin:
  given Decoder[JsValueLogin] = deriveDecoder
