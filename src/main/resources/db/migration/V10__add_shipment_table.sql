CREATE TABLE IF NOT EXISTS organisations_schema.shipments
(
    id              UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    order_id        UUID REFERENCES organisations_schema.orders (id),
    shipment_date   DATE          NOT NULL,
    amount          NUMERIC(9, 2) NOT NULL
);
