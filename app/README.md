# app (frontend)

SvelteKit (Svelte 5) の pnpm ワークスペースです。実務プロジェクトの `app/` を
最小化したもので、本テンプレートでは 1 アプリ（`web`）のみを含みます。

```
app/
├── package.json           # ワークスペースのルート (pnpm -r ...)
├── pnpm-workspace.yaml    # packages: ["packages", "web"]
├── packages/              # @app/api — OpenAPI から生成した型 + 共有クライアント
└── web/                   # SvelteKit アプリ (@app/web)
```

> 実務プロジェクトはアプリを `app/app/*` 配下に置きますが、本テンプレートは
> 1 アプリのみで深さも抑えるため `app/web` に直接置いています。
> アプリを増やすときは `app/<name>` を足し、`pnpm-workspace.yaml` に追記します。

## セットアップ / 起動

```bash
# Node 24 系（.nvmrc）
$ corepack enable          # pnpm を有効化
$ pnpm install
$ pnpm dev                 # http://localhost:3000
```

`vite.config.ts` の `server.proxy` で `/ping` `/user` を Play バックエンド
（`app-api`, http://localhost:9000）へ転送します。先に `app-api` を起動してください。
API のプレフィックスを増やしたら、proxy にも追記します。

同一オリジン扱いになるため、バックエンドが発行する session Cookie がそのまま効きます。

## API の型

リクエスト/レスポンスの型は **手書きしません**。`etc/openapi` の仕様から生成し、
`@app/api`（`packages/`）としてワークスペース内で共有します。

```bash
$ ../etc/openapi/build.sh   # 仕様を変えたら再生成
$ pnpm -r check             # 型チェック
```

- `packages/src/api/client.ts` … `openapi-fetch` のクライアントと共通のエラー処理
- `web/src/lib/user.ts` … このアプリが叩くエンドポイント（ドメイン単位で 1 ファイル）

詳細は [../etc/openapi/README.md](../etc/openapi/README.md) と
[packages/README.md](packages/README.md) を参照してください。

> SMUI(Material) / i18n などは「最小」を優先して省いています。
