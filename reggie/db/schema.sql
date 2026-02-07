-- Full schema bootstrap for Reggie project
-- Usage:
--   source /workspace/SpringBootV1/reggie/db/schema.sql;

CREATE DATABASE IF NOT EXISTS reggie DEFAULT CHARACTER SET utf8mb4;
USE reggie;

CREATE TABLE IF NOT EXISTS employee (
  id BIGINT PRIMARY KEY,
  username VARCHAR(64) NOT NULL UNIQUE,
  name VARCHAR(64) NOT NULL,
  password VARCHAR(128) NOT NULL,
  phone VARCHAR(20),
  sex VARCHAR(8),
  id_number VARCHAR(30),
  status INT DEFAULT 1,
  type INT DEFAULT 0 COMMENT '0 管理员 1 列车员工',
  create_time DATETIME,
  update_time DATETIME,
  create_user BIGINT,
  update_user BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS user (
  id BIGINT PRIMARY KEY,
  name VARCHAR(64),
  phone VARCHAR(20) UNIQUE,
  sex VARCHAR(8),
  id_number VARCHAR(30),
  avatar VARCHAR(255),
  status INT DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS category (
  id BIGINT PRIMARY KEY,
  type INT NOT NULL COMMENT '1 菜品分类 2 套餐分类',
  name VARCHAR(64) NOT NULL,
  sort INT DEFAULT 0,
  create_time DATETIME,
  update_time DATETIME,
  create_user BIGINT,
  update_user BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS dish (
  id BIGINT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  category_id BIGINT NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  code VARCHAR(64),
  image VARCHAR(255),
  description VARCHAR(512),
  status INT DEFAULT 1,
  sort INT DEFAULT 0,
  create_time DATETIME,
  update_time DATETIME,
  create_user BIGINT,
  update_user BIGINT,
  INDEX idx_dish_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS dish_flavor (
  id BIGINT PRIMARY KEY,
  dish_id BIGINT NOT NULL,
  name VARCHAR(64) NOT NULL,
  value TEXT,
  create_time DATETIME,
  update_time DATETIME,
  create_user BIGINT,
  update_user BIGINT,
  is_deleted INT DEFAULT 0,
  INDEX idx_dish_flavor_dish_id (dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS setmeal (
  id BIGINT PRIMARY KEY,
  category_id BIGINT NOT NULL,
  name VARCHAR(64) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  status INT DEFAULT 1,
  code VARCHAR(64),
  description VARCHAR(512),
  image VARCHAR(255),
  create_time DATETIME,
  update_time DATETIME,
  create_user BIGINT,
  update_user BIGINT,
  INDEX idx_setmeal_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS setmeal_dish (
  id BIGINT PRIMARY KEY,
  setmeal_id BIGINT NOT NULL,
  dish_id BIGINT NOT NULL,
  name VARCHAR(64),
  price DECIMAL(10,2),
  copies INT DEFAULT 1,
  sort INT DEFAULT 0,
  create_time DATETIME,
  update_time DATETIME,
  create_user BIGINT,
  update_user BIGINT,
  is_deleted INT DEFAULT 0,
  INDEX idx_setmeal_dish_setmeal_id (setmeal_id),
  INDEX idx_setmeal_dish_dish_id (dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS address_book (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  consignee VARCHAR(64),
  phone VARCHAR(20),
  sex VARCHAR(8),
  province_code VARCHAR(20),
  province_name VARCHAR(64),
  city_code VARCHAR(20),
  city_name VARCHAR(64),
  district_code VARCHAR(20),
  district_name VARCHAR(64),
  detail VARCHAR(255),
  label VARCHAR(64),
  is_default INT DEFAULT 0,
  create_time DATETIME,
  update_time DATETIME,
  create_user BIGINT,
  update_user BIGINT,
  is_deleted INT DEFAULT 0,
  INDEX idx_address_book_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS shopping_cart (
  id BIGINT PRIMARY KEY,
  name VARCHAR(64),
  user_id BIGINT NOT NULL,
  dish_id BIGINT,
  setmeal_id BIGINT,
  dish_flavor VARCHAR(255),
  number INT DEFAULT 1,
  amount DECIMAL(10,2),
  image VARCHAR(255),
  create_time DATETIME,
  INDEX idx_shopping_cart_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS orders (
  id BIGINT PRIMARY KEY,
  number VARCHAR(64) UNIQUE,
  status INT,
  user_id BIGINT NOT NULL,
  address_book_id BIGINT,
  order_time DATETIME,
  checkout_time DATETIME,
  pay_method INT,
  amount DECIMAL(10,2),
  remark VARCHAR(255),
  user_name VARCHAR(64),
  phone VARCHAR(20),
  address VARCHAR(255),
  consignee VARCHAR(64),
  INDEX idx_orders_user_id (user_id),
  INDEX idx_orders_order_time (order_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS order_detail (
  id BIGINT PRIMARY KEY,
  name VARCHAR(64),
  order_id BIGINT NOT NULL,
  dish_id BIGINT,
  setmeal_id BIGINT,
  dish_flavor VARCHAR(255),
  number INT,
  amount DECIMAL(10,2),
  image VARCHAR(255),
  INDEX idx_order_detail_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
