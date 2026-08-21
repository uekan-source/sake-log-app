/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package controllers.auth

import javax.inject.Inject

import mvc.{ AppControllerComponents, BaseAbstractController }

/**
 * Logout.  POST /user/api/logout
 *
 * Deletes the session row (if any) and clears the session cookie. Idempotent —
 * a request without a valid cookie still returns 204.
 */
class LogoutController @Inject()(
  cc: AppControllerComponents,
) extends BaseAbstractController(cc):

  def invoke = Action.async: request =>
    auth.close(request)(NoContent)
