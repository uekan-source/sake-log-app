/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * パッケージ、ここにリポジトリ、テーブルを追記して管理する。Facadeにはリポジトリを追記して使う
 */

package sakelog.user

import javax.inject.*
import ixias.core.inject.IxiasModule
import sakelog.user.persistence.table.*

package object persistence:

  /** Guice module wiring the User persistence singletons. */
  class Module extends IxiasModule:
    def bindings(): Unit =
      singleton[UserTable]
      singleton[UserPasswordTable]
      singleton[UserSessionTable]
      singleton[UserRepository]
      singleton[UserPasswordRepository]
      singleton[UserSessionRepository]
      singleton[RepositoryFacade]

  /** Aggregated repositories for the User domain (injected by app-api). */
  @Singleton
  class RepositoryFacade @Inject()(
    val user:         UserRepository,
    val userPassword: UserPasswordRepository,
    val userSession:  UserSessionRepository,
  )
