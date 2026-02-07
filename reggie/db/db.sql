-- Patch script for existing reggie schema.
-- It is safe to run even when table `employee` does not exist yet.

SET @table_exists := (
  SELECT COUNT(*)
  FROM information_schema.tables
  WHERE table_schema = DATABASE()
    AND table_name = 'employee'
);

SET @ddl := IF(
  @table_exists = 1,
  'ALTER TABLE employee ADD COLUMN IF NOT EXISTS type INT DEFAULT 0',
  'SELECT "skip: table employee does not exist, please import full schema first" AS msg'
);
PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @dml := IF(
  @table_exists = 1,
  'UPDATE employee SET type = 1 WHERE id = 1965287957074952193',
  'SELECT "skip: update not executed because employee table is missing" AS msg'
);
PREPARE stmt2 FROM @dml;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;
