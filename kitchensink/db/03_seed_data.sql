-- Net32 Dental Supply — seed data
-- Idempotent: uses OVERRIDING SYSTEM VALUE and ON CONFLICT DO NOTHING

-- Reset sequences to safe starting points if reseeding
-- Vendors
INSERT INTO vendors (id, name, fulfillment_rating, avg_shipping_days, contact_email)
OVERRIDING SYSTEM VALUE VALUES
(1, 'Henry Schein Dental',  4.9, 1, 'orders@henryschein.com'),
(2, 'Patterson Dental',     4.7, 2, 'dental@pattersondental.com'),
(3, 'Darby Dental Supply',  4.4, 3, 'orders@darbydental.com'),
(4, 'Benco Dental',         4.2, 4, 'sales@bencodental.com'),
(5, 'Safco Dental Supply',  3.8, 5, 'info@safcodental.com')
ON CONFLICT (id) DO NOTHING;

SELECT setval('vendors_id_seq', (SELECT MAX(id) FROM vendors));

-- Products (10 dental supply products)
INSERT INTO products (id, name, sku, base_price, weight_lbs, category)
OVERRIDING SYSTEM VALUE VALUES
(1,  'Latex Exam Gloves Medium 100ct',                    'GLV-LTX-M-100',      8.49,  0.5500, 'PPE'),
(2,  'Nitrile Exam Gloves Large 100ct',                   'GLV-NTR-L-100',      11.99, 0.6000, 'PPE'),
(3,  'Surgical Masks 50ct',                               'MSK-SRG-50',         6.99,  0.3000, 'PPE'),
(4,  '3cc Aspirating Syringes 100ct',                     'SYR-ASP-3CC-100',    24.99, 0.8000, 'Anesthetic'),
(5,  'Composite Resin A2 4g',                             'CMP-RSN-A2-4G',      34.99, 0.0500, 'Restorative'),
(6,  'Dental Bonding Agent 6mL',                          'BND-AGT-6ML',        42.99, 0.1000, 'Restorative'),
(7,  'Vinyl Polysiloxane Impression Material 50mL',       'IMP-VPS-50ML',       67.49, 0.3500, 'Impression'),
(8,  'Carbide Burs FG #245 10pk',                         'BUR-CB-FG245-10',    18.99, 0.0400, 'Rotary'),
(9,  'High-Speed Handpiece',                              'HPC-HS-STD',         389.00,0.4500, 'Equipment'),
(10, 'Self-Seal Sterilization Pouches 3.5x10in 200ct',   'STR-PCH-35X10-200',  14.49, 0.9000, 'Sterilization')
ON CONFLICT (id) DO NOTHING;

SELECT setval('products_id_seq', (SELECT MAX(id) FROM products));

-- Vendor inventory: all 5 vendors carry all 10 products
-- Markup ranges 5-25%, quantity ranges 50-500
INSERT INTO vendor_inventory (vendor_id, product_id, markup_percent, quantity_available)
VALUES
-- Henry Schein (vendor 1)
(1, 1,  8.00, 500), (1, 2,  8.00, 500), (1, 3,  7.00, 500),
(1, 4, 10.00, 300), (1, 5, 15.00, 200), (1, 6, 15.00, 200),
(1, 7, 12.00, 150), (1, 8, 10.00, 400), (1, 9, 18.00,  80), (1, 10, 7.00, 500),
-- Patterson Dental (vendor 2)
(2, 1,  9.00, 450), (2, 2,  9.00, 450), (2, 3,  8.00, 450),
(2, 4, 11.00, 280), (2, 5, 16.00, 180), (2, 6, 16.00, 180),
(2, 7, 13.00, 130), (2, 8, 11.00, 380), (2, 9, 19.00,  70), (2, 10, 8.00, 450),
-- Darby Dental Supply (vendor 3)
(3, 1, 10.00, 400), (3, 2, 10.00, 400), (3, 3,  9.00, 400),
(3, 4, 12.00, 250), (3, 5, 17.00, 160), (3, 6, 17.00, 160),
(3, 7, 14.00, 110), (3, 8, 12.00, 350), (3, 9, 20.00,  60), (3, 10, 9.00, 400),
-- Benco Dental (vendor 4)
(4, 1, 12.00, 350), (4, 2, 12.00, 350), (4, 3, 11.00, 350),
(4, 4, 14.00, 200), (4, 5, 19.00, 140), (4, 6, 19.00, 140),
(4, 7, 16.00,  90), (4, 8, 14.00, 300), (4, 9, 22.00,  50), (4, 10, 11.00, 350),
-- Safco Dental Supply (vendor 5)
(5, 1, 15.00, 300), (5, 2, 15.00, 300), (5, 3, 14.00, 300),
(5, 4, 17.00, 150), (5, 5, 22.00, 100), (5, 6, 22.00, 100),
(5, 7, 19.00,  70), (5, 8, 17.00, 250), (5, 9, 25.00,  50), (5, 10, 14.00, 300)
ON CONFLICT (vendor_id, product_id) DO NOTHING;

-- Shipping zones (5 zones by ZIP prefix range)
INSERT INTO shipping_zones (id, zone_name, zip_range_start, zip_range_end, base_rate_per_lb, min_days, max_days)
OVERRIDING SYSTEM VALUE VALUES
(1, 'Northeast',    '00000', '19999', 0.8500, 1, 3),
(2, 'Southeast',    '20000', '39999', 0.9500, 2, 4),
(3, 'Midwest',      '40000', '69999', 1.0500, 2, 5),
(4, 'Southwest',    '70000', '79999', 1.1500, 3, 6),
(5, 'West_Pacific', '80000', '99999', 1.2500, 3, 7)
ON CONFLICT (id) DO NOTHING;

SELECT setval('shipping_zones_id_seq', (SELECT MAX(id) FROM shipping_zones));

-- Members
INSERT INTO member (id, name, email, phone_number, tier, total_spend)
OVERRIDING SYSTEM VALUE VALUES
(1, 'Jane Smith',    'jane.smith@dentalclinic.com', '9195550101', 'GOLD',   3250.00),
(2, 'Robert Torres', 'rtorres@orthosmile.com',      '9195550102', 'SILVER',  875.50),
(3, 'Emily Chen',    'echen@brightsmiles.com',      '9195550103', 'BRONZE',  124.00)
ON CONFLICT (id) DO NOTHING;

SELECT setval('member_id_seq', (SELECT MAX(id) FROM member));
