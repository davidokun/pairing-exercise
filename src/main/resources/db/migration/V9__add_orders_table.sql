CREATE TABLE IF NOT EXISTS organisations_schema.orders
(
    id              UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    order_date      DATE          NOT NULL,
    total_amount    NUMERIC(9, 2) NOT NULL,
    state           VARCHAR       NOT NULL,
    organisation_id UUID REFERENCES organisations_schema.organisations (id)
);
