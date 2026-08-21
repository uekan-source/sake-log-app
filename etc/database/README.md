# etc/database

DB スキーママイグレーション（flyway）の置き場です。

```
etc/database/migration/<db>/common/V<日付>_<連番>__<説明>.sql
```

- 本テンプレートの DB 名は `app`（`etc/database/migration/app/common/`）。
- 命名規則：`V<日付>_<連番>__<説明>.sql`（例 `V20260717_01__create_xxx.sql`）。
- 適用は `app-api` 側の sbt タスク：

```bash
$ cd app-api
$ sbt
sbt:education-book-app-api> migrateAll                # 全 DB へ適用
sbt:education-book-app-api> migrateApp/flywayMigrate  # app DB のみ
sbt:education-book-app-api> migrateApp/flywayInfo     # 適用状況の確認
```

flyway-sbt は 1 プロジェクト = 1 接続しか持てないため、`app-api/build.sbt` では
**DB ごとに `target/migration/<db>` のサブプロジェクト**を定義し、`migrateAll` が
それらの `flywayMigrate` を `Def.sequential` で順に呼びます。
`flywayInfo` / `flywayClean` など flyway-sbt の他タスクも
`migrate<Db>/` スコープでそのまま使えます。

接続情報とマイグレーション設定は `app-api/conf/application.conf` の
`db.<db>`（`driver` / `url` / `username` / `password` / `migration.table` /
`migration.locations`）から読み取ります。

## DB を増やすとき

1. `etc/database/migration/<db>/common/` を作り、SQL を置く
2. `app-api/conf/application.conf` に `db.<db>` ブロックを追加
3. `app-api/build.sbt` にサブプロジェクトを追加し、`migrateAll` に 1 行足す

```scala
lazy val migrateXxx = (project in file("target/migration/xxx"))
  .enablePlugins(FlywayPlugin)
  .settings(migrationSettings("xxx"))
migrateAll := Def
  .sequential(
    migrateApp / flywayMigrate,
    migrateXxx / flywayMigrate,   // ← 追加
  )
  .value
```
