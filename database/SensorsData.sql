/**
* To initialize SensorsData database
 */

-- ----------------------------
--  Clean
-- ----------------------------
\connect "template1";

DROP OWNED BY "smartcampus";
DROP OWNED BY "dataaccessor";
DROP OWNED BY "dataprocessor";

DROP DATABASE IF EXISTS "SensorsData";

DROP USER dataprocessor;
DROP USER dataaccessor;

-- ----------------------------
--  Users
-- ----------------------------

CREATE USER "smartcampus" CREATEDB CREATEUSER LOGIN PASSWORD 'smartcampus' CREATEROLE;
CREATE USER "dataaccessor" LOGIN PASSWORD 'dataaccessor';
CREATE USER "dataprocessor" LOGIN PASSWORD 'dataprocessor';

-- ----------------------------
--  Database
-- ----------------------------
CREATE DATABASE "SensorsData"  WITH OWNER "smartcampus" ENCODING 'UTF8';

-- ----------------------------
--  Table structure for SensorsData
-- ----------------------------
\connect "SensorsData";

DROP TABLE IF EXISTS "public"."SensorsData";
CREATE TABLE "public"."SensorsData" (
	"sensor_id" varchar NOT NULL COLLATE "default",
	"sensor_date" varchar NOT NULL COLLATE "default",
	"sensor_value" varchar NOT NULL COLLATE "default"
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."SensorsData" OWNER TO "smartcampus";

-- ----------------------------
--  Grant
-- ----------------------------
GRANT SELECT ON "SensorsData" TO "dataaccessor";

GRANT SELECT ON "SensorsData" TO "dataprocessor";
GRANT INSERT ON "SensorsData" TO "dataprocessor";
GRANT UPDATE ON "SensorsData" TO "dataprocessor";
GRANT DELETE ON "SensorsData" TO "dataprocessor";

-- ----------------------------
--  Primary key structure for table SensorsData
-- ----------------------------
ALTER TABLE "public"."SensorsData" ADD PRIMARY KEY ("id", "date") NOT DEFERRABLE INITIALLY IMMEDIATE;
