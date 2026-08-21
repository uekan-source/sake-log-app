/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package mvc

import javax.inject.Inject

/**
 * Type alias for BaseControllerComponents from ixias.web.play.
 * Provides the action builder, body parsers, message API, execution context, etc.
 */
type BaseControllerComponents = ixias.web.play.BaseControllerComponents

/**
 * Application-level controller components.
 *
 * Aggregates Play's base components with the application repositories so that
 * every controller receives them through a single injected value.
 *
 * @param base  ixias/Play base controller components
 * @param repos aggregated repositories ([[AppRepositoryFacade]])
 * @param auth  cookie session authentication ([[mvc.auth.AuthProfile]])
 */
case class AppControllerComponents @Inject()(
  base:  BaseControllerComponents,
  repos: AppRepositoryFacade,
  auth:  mvc.auth.AuthProfile,
)

/**
 * Abstract base controller. All application controllers extend this to inherit
 * ixias's `BaseController` helpers (`request.decode[T]`, JSON responses, i18n)
 * and convenient access to `repos` / `auth`.
 */
abstract class BaseAbstractController @Inject()(
  protected val app: AppControllerComponents
) extends ixias.web.play.BaseAbstractController(app.base):
  protected lazy val repos = app.repos
  protected lazy val auth  = app.auth
