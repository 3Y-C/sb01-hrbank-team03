CREATE TYPE "file_category_enum" AS ENUM ('IMAGE', 'DOCUMENT');

CREATE TABLE IF NOT EXISTS "file_meta_data" (
    "id" BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL,
    "file_category" "file_category_enum" NOT NULL,  -- ENUM 타입을 미리 정의해야 합니다.
    "type" VARCHAR(50) NOT NULL,
    "size" BIGINT NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "Departments" (
    "id" BIGINT NOT NULL PRIMARY KEY,
    "name" VARCHAR(255) NOT NULL UNIQUE,
    "description" VARCHAR(255) NULL,
    "established_date" DATE NOT NULL DEFAULT CURRENT_DATE,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP NULL
);

CREATE TYPE "employee_status_enum" AS ENUM ('ACTIVE', 'ON_LEAVE', 'RESIGNED');

CREATE TABLE IF NOT EXISTS "Employees" (
    "id" BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
     "department_id" BIGINT NULL,
    "profile_id" BIGINT NOT NULL,
    "name" VARCHAR(100) NOT NULL,
    "email" VARCHAR(255) NOT NULL UNIQUE,
    "employee_number" VARCHAR(255) NOT NULL UNIQUE,
    "position" VARCHAR(100) NOT NULL,
    "hire_date" DATE NOT NULL,
    "status" "employee_status_enum" DEFAULT 'ACTIVE',
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "update_at" TIMESTAMP NULL,
    FOREIGN KEY ("department_id") REFERENCES "Departments"("id") ON DELETE SET NULL
);

CREATE TYPE "history_type_enum" AS ENUM ('CREATED', 'UPDATED', 'DELETED');

CREATE TABLE IF NOT EXISTS "employee_historys" (
    "id" BIGINT NOT NULL PRIMARY KEY,
    "employee_number" VARCHAR(255) NOT NULL,
    "type" "history_type_enum" NULL,
    "memo" VARCHAR(255) NULL,
    "ip_address" VARCHAR(15) NOT NULL,
    "edited_at" TIMESTAMP NOT NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY ("employee_number") REFERENCES "Employees"("employee_number")
);


CREATE TABLE IF NOT EXISTS "employee_change_details" (
    "id" BIGINT NOT NULL PRIMARY KEY,
    "history_id" BIGINT NOT NULL,
    "propertyName" VARCHAR(10) NULL,
    "before" VARCHAR(255) NULL,
    "after" VARCHAR(255) NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY ("history_id") REFERENCES "employee_historys"("id") ON DELETE CASCADE
);

CREATE TYPE "backup_status_enum" AS ENUM ('IN_PROGRESS', 'COMPLETED', 'FAILED', 'SKIPPED');

CREATE TABLE IF NOT EXISTS "backups" (
    "id" BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "file_id" BIGINT NULL,
    "worker_ip" VARCHAR(255) NOT NULL,
    "status" "backup_status_enum" NOT NULL,
    "start_at" TIMESTAMP NOT NULL,
    "end_at" TIMESTAMP NULL,
    "created_at" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY ("file_id") REFERENCES "file_meta_data"("id")
);
