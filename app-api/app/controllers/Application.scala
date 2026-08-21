/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * ヘルスチェックのファイル
 */

package controllers

import play.api.mvc.*

/**
 * Application controller for basic system operations.
 *
 * Provides the health-check endpoint used by load balancers / monitoring.
 */
class ApplicationController extends InjectedController:

  /** Health check. Always returns "ok" with HTTP 200. */
  def ping = Action:
    Ok("ok")
