/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 * Brandリポジトリ
 */

package edu.user.persistence

import javax.inject.*
import scala.concurrent.Future
import ixias.db.slick.{ SlickBaseRepository, SlickDatabaseContext }
import ixias.core.persistence.HostSpec

import edu.user.model.User
import edu.user.persistence.table.BrandTable

/**
 * Brand の保存・取得の窓口。
 * find / filter / add / update / upsert / delete は SlickBaseRepository が
 * 自動で提供する（実装を書く必要はない）。ここには固有の検索だけ足す。
 */
@Singleton
class BrandRepository @Inject()(
  table: BrandTable,
  ctx:   SlickDatabaseContext
) extends SlickBaseRepository(table, ctx):
  import api.*

  /**
   * 自分の銘柄の一覧（名前順。照合順序が かな・大小・全半角 を吸収する）
   */
  def findAllByUserId(userId: User.Id): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(HostSpec.REPLICA): slick =>
      slick
        .filter(_.uid === userId)
        .sortBy(_.name)
        .result
