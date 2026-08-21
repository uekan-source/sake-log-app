# sake-log-app

お酒の記録アプリ。ラベルの写真やメニューの文字から AI が銘柄情報を推定し、自分で確認・修正して記録を貯める。

研修（要件定義〜モデル設計〜実装）の成果物として、一から設計・実装するポートフォリオ。

## 現在の状態

**要件定義・モデル設計フェーズ。** 実装はモデル設計のレビュー後に始める。

## 構成

| ディレクトリ | 内容 |
|---|---|
| `docs/domain/` | 機能ごとの設計文書（`01_requirements` / `02_analysis` / `03_design`） |
| `docs/notes/` | 作業用ノート |
| `app/` | フロントエンド（SvelteKit）。雛形の認証画面まで入っている |
| `app-api/` | API（Scala 3 / Play）。雛形の認証 API（登録・ログイン・ログアウト）まで入っている |
| `app-lib/` | フレームワークライブラリ（ixiaS。`edu.user` の User / UserPassword / UserSession を含む） |
| `etc/` | database（マイグレーション）/ docker / openapi |

雛形（認証まわり）は education-book-scala-app から流用。**アプリ固有の機能実装は、モデル設計のレビュー後に始める。**

構成は [education-book-scala-app](https://github.com/uekan-source/education-book-scala-app) に倣う。
