/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package mvc

import javax.inject.Inject
import scala.concurrent.Future

import ixias.core.util.Log.*
import ixias.web.play.json.{ JsonBaseSyntax, JsonFailure }
import play.api.http.HttpErrorHandler
import play.api.http.Status.*
import play.api.mvc.Results.*
import play.api.mvc.{ RequestHeader, Result }
import play.api.i18n.{ Messages, MessagesApi }

/**
 * Application-wide error handler returning standardized JSON error responses.
 *
 * Enabled via `play.http.errorHandler = mvc.ErrorHandler` in application.conf.
 */
class ErrorHandler @Inject() (messagesApi: MessagesApi)
  extends HttpErrorHandler with JsonBaseSyntax:

  /** 4xx client errors — logged at INFO level. */
  def onClientError(rh: RequestHeader, stateCode: Int, message: String): Future[Result] =
    given Messages = messagesApi.preferred(rh)
    Future.successful:
      info("state = %d, message = %s".format(stateCode, message))
      JsonFailure(Status(stateCode), "Client error occurred")
        .withCode(stateCode.toString)
        .build

  /** Unhandled exceptions — mapped to a status code and logged by severity. */
  def onServerError(rh: RequestHeader, ex: Throwable): Future[Result] =
    given Messages = messagesApi.preferred(rh)
    Future.successful:
      val stateCode = ex match
        case _: NoSuchElementException   => NOT_FOUND
        case _: IllegalArgumentException => BAD_REQUEST
        case _                           => INTERNAL_SERVER_ERROR
      val message = "state = %d, api = %s, message = %s".format(stateCode, rh.path, ex.getMessage)
      stateCode match
        case INTERNAL_SERVER_ERROR => error(message, ex)
        case _                     => info(message, ex)
      JsonFailure(Status(stateCode), "Unexpected error")
        .withCode(stateCode.toString)
        .build
