-- ALTER TABLE

ALTER TABLE "order"
    ADD COLUMN delivery_date TIMESTAMP;

UPDATE "order"
    SET delivery_date = created_on + INTERVAL '1 DAY';

ALTER TABLE "order"
    ALTER COLUMN delivery_date SET NOT NULL;
