-- TAGI: Villa Tagi (11111111-1111-1111-1111-111111111169) / leolea (11111111-1111-1111-1111-111111111114)
INSERT INTO adresse (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, gemeinde, gueltig_ab, gueltig_bis, hausnummer, land, ort, plz, strasse, zusatzzeile, organisation)
VALUES ('11111111-1111-1111-1111-111111111169', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, null, '1000-01-01', '9999-12-31', '11', 'CH', 'Bern', '3007', 'Seftigenstrasse', null, null);

INSERT INTO institution (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, mandant_id, traegerschaft_id, active)
VALUES ('11111111-1111-1111-1111-111111111169', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, 'Villa Tagi', 'e3736eb8-6eef-40ef-9e52-96ab48d8f220', '11111111-1111-1111-1111-111111111114', true);

INSERT INTO institution_stammdaten (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, gueltig_ab, gueltig_bis, betreuungsangebot_typ, iban, oeffnungsstunden, oeffnungstage, institution_id, adresse_id)
VALUES ('11111111-1111-1111-1111-111111111169', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, '1000-01-01', '9999-12-31', 'TAGI', null, null, null, '11111111-1111-1111-1111-111111111169', '11111111-1111-1111-1111-111111111169');


-- TAGI: Tagi Länggasse (11111111-1111-1111-1111-111111111170) / Stadt Bern (11111111-1111-1111-1111-111111111113)
INSERT INTO adresse (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, gemeinde, gueltig_ab, gueltig_bis, hausnummer, land, ort, plz, strasse, zusatzzeile, organisation)
VALUES ('11111111-1111-1111-1111-111111111170', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, null, '1000-01-01', '9999-12-31', '62', 'CH', 'Bern', '3006', 'Länggassstrasse', null, null);

INSERT INTO institution (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, mandant_id, traegerschaft_id, active)
VALUES ('11111111-1111-1111-1111-111111111170', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, 'Tagi Länggasse', 'e3736eb8-6eef-40ef-9e52-96ab48d8f220', '11111111-1111-1111-1111-111111111113', true);

INSERT INTO institution_stammdaten (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, gueltig_ab, gueltig_bis, betreuungsangebot_typ, iban, oeffnungsstunden, oeffnungstage, institution_id, adresse_id)
VALUES ('11111111-1111-1111-1111-111111111170', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, '1000-01-01', '9999-12-31', 'TAGI', null, null, null, '11111111-1111-1111-1111-111111111170', '11111111-1111-1111-1111-111111111170');


-- TAGI: Tagi Kleefeld (11111111-1111-1111-1111-111111111171) / Stadt Bern (11111111-1111-1111-1111-111111111113)
INSERT INTO adresse (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, gemeinde, gueltig_ab, gueltig_bis, hausnummer, land, ort, plz, strasse, zusatzzeile, organisation)
VALUES ('11111111-1111-1111-1111-111111111171', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, null, '1000-01-01', '9999-12-31', '2', 'CH', 'Bern', '3018', 'Freieckweg', null, null);

INSERT INTO institution (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, mandant_id, traegerschaft_id, active)
VALUES ('11111111-1111-1111-1111-111111111171', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, 'Tagi Kleefeld', 'e3736eb8-6eef-40ef-9e52-96ab48d8f220', '11111111-1111-1111-1111-111111111113', true);

INSERT INTO institution_stammdaten (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, gueltig_ab, gueltig_bis, betreuungsangebot_typ, iban, oeffnungsstunden, oeffnungstage, institution_id, adresse_id)
VALUES ('11111111-1111-1111-1111-111111111171', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, '1000-01-01', '9999-12-31', 'TAGI', null, null, null, '11111111-1111-1111-1111-111111111171', '11111111-1111-1111-1111-111111111171');


-- TAGESELTERN: LeoLea (11111111-1111-1111-1111-111111111172) / leolea (11111111-1111-1111-1111-111111111114)
INSERT INTO adresse (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, gemeinde, gueltig_ab, gueltig_bis, hausnummer, land, ort, plz, strasse, zusatzzeile, organisation)
VALUES ('11111111-1111-1111-1111-111111111172', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, null, '1000-01-01', '9999-12-31', '4', 'CH', 'Bern', '3005', 'Gasstrasse', null, null);

INSERT INTO adresse (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, gemeinde, gueltig_ab, gueltig_bis, hausnummer, land, ort, plz, strasse, zusatzzeile, organisation)
VALUES ('11111111-1111-1111-1111-111111111173', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, null, '1000-01-01', '9999-12-31', '4', 'CH', 'Bern', '3005', 'Gasstrasse', null, null);

INSERT INTO institution (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, name, mandant_id, traegerschaft_id, active)
VALUES ('11111111-1111-1111-1111-111111111172', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, 'Tageseltern LeoLea', 'e3736eb8-6eef-40ef-9e52-96ab48d8f220', '11111111-1111-1111-1111-111111111114', true);

INSERT INTO institution_stammdaten (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, gueltig_ab, gueltig_bis, betreuungsangebot_typ, iban, oeffnungsstunden, oeffnungstage, institution_id, adresse_id)
VALUES ('11111111-1111-1111-1111-111111111172', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, '1000-01-01', '9999-12-31', 'TAGESELTERN_KLEINKIND', null, null, null, '11111111-1111-1111-1111-111111111172', '11111111-1111-1111-1111-111111111172');

INSERT INTO institution_stammdaten (id, timestamp_erstellt, timestamp_mutiert, user_erstellt, user_mutiert, version, gueltig_ab, gueltig_bis, betreuungsangebot_typ, iban, oeffnungsstunden, oeffnungstage, institution_id, adresse_id)
VALUES ('11111111-1111-1111-1111-111111111173', '2016-09-27 00:00:00', '2016-09-27 00:00:00', 'flyway', 'flyway', 0, '1000-01-01', '9999-12-31', 'TAGESELTERN_SCHULKIND', null, null, null, '11111111-1111-1111-1111-111111111172', '11111111-1111-1111-1111-111111111173');