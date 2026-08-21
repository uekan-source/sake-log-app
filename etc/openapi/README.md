# etc/openapi

API 契約（OpenAPI 3.1）の置き場です。エントリポイントは `openapi.yaml`。

```
etc/openapi/
├── openapi.yaml               エントリ。paths: から各オペレーションを $ref するだけ
├── paths/<name>.yaml          1 ファイル = 1 オペレーション（メソッド単位）
├── components/responses/      共有エラーレスポンス（400 / 401 / 409 / 500）
├── build.sh                   バンドル + TypeScript 型生成
└── .openapi.yaml              バンドル結果（生成物・gitignore）
```

1 オペレーション 1 ファイルにしているのは、エンドポイントが増えても差分が
レビュー可能なサイズに収まるようにするためです。

## 生成

```bash
$ ./etc/openapi/build.sh
```

1. `@redocly/cli bundle` で `$ref` を解決し `.openapi.yaml` に束ねる
2. `openapi-typescript` で `app/packages/src/api/schema.d.ts` を生成

生成された型は `@app/api` として pnpm ワークスペースで共有され、
`app/web/src/lib/api/client.ts` がリクエスト/レスポンス型をそこから導出します。
**型は手書きしません** — 仕様を変えたら再生成し、ズレていれば型エラーで落ちます。

`schema.d.ts` は生成物ですがコミットします（利用側が生成なしで型チェックできるように）。

## エンドポイントを追加するとき

1. `paths/<service>-<action>.yaml` を作る
2. `openapi.yaml` の `paths:` に `$ref` を足す
3. `./etc/openapi/build.sh`

## 注意：エラーレスポンスの Content-Type

コントローラーが明示的に返す 4xx は `BadRequest("...")` のように文字列を
渡しているため **text/plain** です。`{error, message, code, errors}` の
JSON エンベロープを返すのは `mvc.ErrorHandler` が処理する未捕捉エラー
（ルート不一致の 404 や 500）だけで、両者は別物です。
`components/responses/*.yaml` はこの実態に合わせてあります。
