INSERT INTO Discount (id, DTYPE, description) VALUES (1, 'NoDiscount', 'Sem desconto');
INSERT INTO Discount (id, DTYPE, description) VALUES (2, 'ThresholdPercentageDiscount', 'Percentagem do Total (acima de limiar)');
INSERT INTO Discount (id, DTYPE, description) VALUES (3, 'EligibleProductsDiscount', 'Percentagem do Total Elegivel');
INSERT INTO Unit (id, description, abbreviation) VALUES (1, 'Quilogramas', 'Kg');
INSERT INTO Unit (id, description, abbreviation) VALUES (2, 'Unidades', 'un');
INSERT INTO Product (id, prodCod, description, faceValue, qty, discountEligibility, unit_id, version) VALUES (1, 123, 'Prod 1', 100, 500, 0, 1, 1);
INSERT INTO Product (id, prodCod, description, faceValue, qty, discountEligibility, unit_id, version) VALUES (2, 124, 'Prod 2', 35, 1000, 1, 2, 1);