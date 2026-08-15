# 綿谷家の冷蔵庫管理

MySQLに在庫を保存するJava（Spring Boot）アプリケーションです。

## 前提条件

- Java 17以上
- Maven 3.9以上
- MySQL 8.0以上
- データベース `private_1_db`

## 起動方法

接続情報は環境変数で渡します。パスワードはソースコード、Git、設定ファイルに保存しません。

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/private_1_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Tokyo"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "<MySQLのパスワード>"
mvn spring-boot:run
```

初回起動時にFlywayが `categories` と `inventory_items` を作成し、調味料・食材のカテゴリとサンプル在庫を投入します。起動後、`http://localhost:8080/` を開いてください。

## API

- `GET /api/categories` — カテゴリと在庫一覧を取得
- `POST /api/inventory-items` — 在庫を追加
- `PATCH /api/inventory-items/{id}/quantity` — 数量を増減

手動適用向けSQLは `migrations/001_create_inventory_tables.sql` にあります。Spring Boot起動時は `src/main/resources/db/migration/V1__create_inventory_tables.sql` が自動適用されます。

## Render / Cloudflare Pagesへのデプロイ

RenderのWeb Serviceでは `Docker` を選択し、Root Directoryは空欄、Dockerfile Pathは `Dockerfile` にします。Renderが設定する `PORT` 環境変数をアプリケーションが自動的に利用します。

Renderには次の環境変数を設定してください。

```text
DB_URL=jdbc:mysql://<公開可能なMySQLホスト>:3306/private_1_db?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC
DB_USERNAME=<MySQLユーザー名>
DB_PASSWORD=<MySQLパスワード>
CORS_ALLOWED_ORIGINS=https://<Cloudflare Pagesの公開ドメイン>
```

RenderからローカルPCのMySQLには接続できません。Render内または外部ホスティングのMySQLを用意してください。DBとWeb Serviceは同じリージョンに配置します。

Renderの公開URLが決まったら、Cloudflare Pagesにデプロイする前に `src/config.js` の `API_BASE_URL` をそのURLへ変更します。例: `https://private-1-api.onrender.com`。このファイルにはパスワード等の秘密情報を書き込みません。
