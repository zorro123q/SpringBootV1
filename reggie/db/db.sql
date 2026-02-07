-- Patch script for existing reggie schema.
-- Safe for fresh databases and older MySQL variants.

SET @db_name := IFNULL(DATABASE(), 'reggie');

SET @table_exists := (
  SELECT COUNT(*)
  FROM information_schema.tables
  WHERE table_schema = @db_name
    AND table_name = 'employee'
);

SET @column_exists := (
  SELECT COUNT(*)
  FROM information_schema.columns
  WHERE table_schema = @db_name
    AND table_name = 'employee'
    AND column_name = 'type'
);

SET @ddl := IF(
  @table_exists = 1 AND @column_exists = 0,
  'ALTER TABLE employee ADD COLUMN type INT DEFAULT 0',
  'SELECT "skip: employee missing or type column already exists" AS msg'
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
