# このリポジトリについて

お酒の記録アプリ `sake-log-app` を一から設計・実装するポートフォリオ。ラベル写真やメニューの文字から AI（外部の知識源）が銘柄情報を推定し、ユーザーが確認・修正して記録を貯める。

**発注者はユーザー自身。** 要求の正は `docs/domain/` の `01_requirements.md`。

## 手順と原則の正

設計の手順・原則・書き方は **education-book-scala-app 側の `docs/notes/` が正**（`design_procedure.md` / `design_lessons.md` / `document_format.md`）。2 箇所に置くとズレるため、このリポジトリには写さない。設計を始めるときは向こうの手順書を開くこと。

ローカルパス: `/Volumes/dev/git-dev/education-book-scala-app/docs/notes/`

## ドキュメントの構成

機能ごとに `docs/domain/<機能名>/` を作り、3 ファイルで進める。

```
01_requirements.md  受け取った要求 / 質問リスト / 業務フローと例外ルール
02_analysis.md      思考過程のメモ（提出しない。当時の判断の記録として残す）
03_design.md        最終的な設計。人に見せるのはこれ
```

`02` は**書き換えない**。その時点でどう考えたかの記録なので、後の変更を反映すると価値が失われる。

## 命名・図の規約

education-book-scala-app と同じ。

- テーブル名はコンテキストのパスを頭に付け、単数形
- 区分値は `IS_` 始まり。`code` は正が「生きている」、負が「終わった」
- 状態遷移図・フローチャート・**ER 図**は SVG を手で書いて `docs/domain/<機能名>/images/` に置く（ER 図は当初 mermaid だったが、2026-08-21 に発注者の指定で SVG に統一。箱＝日本語名＋英語名＋バッジ、直角の線に多重度、のスタイル）

## 実装

Scala 3（`app-api` / `app-lib`）+ Svelte（`app`）。**モデル設計のレビューが終わるまで実装コードは書かない。**
