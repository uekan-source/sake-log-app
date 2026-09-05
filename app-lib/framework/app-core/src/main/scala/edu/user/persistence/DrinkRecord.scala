/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * DrinkRecordリポジトリ
 */

package edu.user.persistence

import javax.inject.*
import scala.concurrent.Future
import ixias.db.slick.{ SlickBaseRepository, SlickDatabaseContext }
import ixias.core.persistence.HostSpec

import edu.user.model.User
import edu.user.persistence.table.DrinkRecordTable

/**
 * DrinkRecord の保存・取得の窓口。
 * find / filter / add / update / upsert / delete は SlickBaseRepository が
 * 自動で提供する（実装を書く必要はない）。ここには固有の検索だけ足す。
 */
@Singleton
class DrinkRecordRepository @Inject()(
  table: DrinkRecordTable,
  ctx:   SlickDatabaseContext
) extends SlickBaseRepository(table, ctx):
  import api.*

  /**
   * 自分の記録の一覧（新しい順）
   */
  def findAllByUserId(userId: User.Id): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(HostSpec.REPLICA): slick =>
      slick
        .filter(_.uid === userId)
        .sortBy(_.createdAt.desc)
        .result
