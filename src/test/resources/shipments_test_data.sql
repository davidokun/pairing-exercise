-- Contact Details
INSERT INTO organisations_schema.contact_details (id, phone_number, fax, email)
VALUES ('b0e83a91-5d9d-4c9e-b225-496fd16402ce', '+443700100222', '', 'yourquestions@bbc.co.uk');

-- Organisations
INSERT INTO organisations_schema.organisations (id, name, date_founded, country_code, vat_number, registration_number, legal_entity_type, contact_details_id)
VALUES ('c39918e0-7b7d-49d9-a092-bae8be8deae5', 'BBC', '1922-10-18', 'GB', '333289454', '3686147', 'NONPROFIT_ORGANIZATION', 'b0e83a91-5d9d-4c9e-b225-496fd16402ce');

-- Orders
INSERT INTO organisations_schema.orders (id, order_date, total_amount, state, organisation_id)
VALUES ('635bbb2f-abb3-434d-b24a-fd238cbe02c9', '2023-11-20', 100.00, 'PENDING', 'c39918e0-7b7d-49d9-a092-bae8be8deae5');
INSERT INTO organisations_schema.orders (id, order_date, total_amount, state, organisation_id)
VALUES ('ffe27d9b-bfe9-4db1-884a-75630da6aeb1', '2023-11-20', 100.00, 'PENDING', 'c39918e0-7b7d-49d9-a092-bae8be8deae5');
INSERT INTO organisations_schema.orders (id, order_date, total_amount, state, organisation_id)
VALUES ('482152c5-e92d-4fa7-bee3-a8ac2031a701', '2023-11-20', 150.00, 'COMPLETED', 'c39918e0-7b7d-49d9-a092-bae8be8deae5');
INSERT INTO organisations_schema.orders (id, order_date, total_amount, state, organisation_id)
VALUES ('c3f6fe81-adf5-4003-b941-2ee476693c04', '2023-11-20', 100.00, 'PENDING', 'c39918e0-7b7d-49d9-a092-bae8be8deae5');

-- Shipments
INSERT INTO organisations_schema.shipments (id, order_id, shipment_date, amount)
VALUES ('345abe3c-fbc0-433e-a92f-76c440b79a5d', 'ffe27d9b-bfe9-4db1-884a-75630da6aeb1', '2023-11-17', 30.00);
INSERT INTO organisations_schema.shipments (id, order_id, shipment_date, amount)
VALUES ('2d19c463-25f3-4cb5-8cea-7cb6e6429416', 'ffe27d9b-bfe9-4db1-884a-75630da6aeb1', '2023-11-17', 30.00);
INSERT INTO organisations_schema.shipments (id, order_id, shipment_date, amount)
VALUES ('be8a015b-1b8d-4098-8274-bc09bbcb1833', 'ffe27d9b-bfe9-4db1-884a-75630da6aeb1', '2023-11-17', 30.00);


