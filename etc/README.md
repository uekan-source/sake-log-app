# etc/

モジュール横断のインフラ・契約(スキーマ)関連ファイルを集約します。

| ディレクトリ | 内容 |
|---|---|
| `docker/`   | ローカル開発用の Docker 資材（MySQL の `init.sql` / `my.cnf` など）。ルートの `docker-compose.yml` から参照されます |
| `database/` | DB スキーママイグレーション（flyway）。`migration/<db>/common/*.sql`。`app-api` の `sbt migrateAll` が適用します |
| `openapi/`  | API 契約（OpenAPI 仕様）。フロントの型/クライアント生成の元にします |
