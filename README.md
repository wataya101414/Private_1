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
