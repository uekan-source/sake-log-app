/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * アプリ全体の窓口(Facade) ドメインが増えたらここに追記もする
 */

package mvc

import javax.inject.{ Inject, Singleton }

/**
 * Aggregated repositories exposed to controllers (via [[AppControllerComponents]]).
 *
 * Add one field per domain as you build them in app-lib.
 */
@Singleton
class AppRepositoryFacade @Inject() (
  val udb: edu.udb.persistence.RepositoryFacade,
)
