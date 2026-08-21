/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package mvc

import javax.inject.{ Inject, Singleton }
import com.google.inject.AbstractModule
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcProfile

import ixias.core.security.{ Signer, MacSigner }
import ixias.db.slick.SlickDatabaseContext
import ixias.db.slick.driver.JdbcMySQLProfileExt

/**
 * Concrete SlickDatabaseContext with constructor injection.
 */
@Singleton
class AppDatabaseContext @Inject()(
  val driver: JdbcProfile,
  val ec:     ExecutionContext,
) extends SlickDatabaseContext

/**
 * Guice module providing the infrastructure bindings ixias needs
 * (the Slick JDBC profile and the database context).
 *
 * Enabled via `play.modules.enabled += mvc.AppModule` in application.conf.
 */
class AppModule extends AbstractModule:

  override def configure(): Unit =
    bind(classOf[JdbcProfile])
      .toInstance(new JdbcMySQLProfileExt{})
    bind(classOf[SlickDatabaseContext])
      .to(classOf[AppDatabaseContext])
    bind(classOf[Signer])
      .toInstance(MacSigner.fromSecretKey("secret-hash", "HmacSHA256"))
