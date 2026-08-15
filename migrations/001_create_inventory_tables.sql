-- MySQL 8.0+
-- Run this migration against the private_1_db database.
-- Connection credentials are intentionally not stored in this repository.

CREATE TABLE categories (
  id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
  code VARCHAR(32) NOT NULL,
  name VARCHAR(50) NOT NULL,
  display_order SMALLINT UNSIGNED NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_categories_code (code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE inventory_items (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  category_id TINYINT UNSIGNED NOT NULL,
  name VARCHAR(100) NOT NULL,
  quantity INT UNSIGNED NOT NULL DEFAULT 0,
  unit VARCHAR(20) NOT NULL DEFAULT '個',
  emoji VARCHAR(32) NULL,
  display_order SMALLINT UNSIGNED NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_inventory_items_category
    FOREIGN KEY (category_id) REFERENCES categories (id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  UNIQUE KEY uq_inventory_items_category_name (category_id, name),
  KEY idx_inventory_items_category_display (category_id, display_order, id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO categories (code, name, display_order)
VALUES
  ('seasoning', '調味料', 1),
  ('food', '食材', 2);
