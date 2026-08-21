# @app/api

`etc/openapi` の仕様から生成した TypeScript 型だけを持つ、型専用パッケージです。
ランタイムコードは含みません（`src/api/schema.d.ts` のみ）。

## 生成

```bash
$ ./etc/openapi/build.sh                  # or: pnpm --filter @app/api generate
```

`src/api/schema.d.ts` は **生成物ですがコミットします**（利用側が生成なしで
型チェックできるようにするため）。仕様を変えたら再生成して一緒にコミットしてください。

## 使い方

```ts
import type { paths } from '@app/api';

type Me = paths['/user/api/me']['get']['responses'][200]['content']['application/json'];
```

フロントアプリを増やしたら、その `package.json` の `devDependencies` に
`"@app/api": "workspace:*"` を足せば同じ型を共有できます。
