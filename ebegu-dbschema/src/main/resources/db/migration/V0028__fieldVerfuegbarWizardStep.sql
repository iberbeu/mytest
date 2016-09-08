ALTER TABLE wizard_step
  ADD COLUMN verfuegbar BIT NOT NULL DEFAULT false;

ALTER TABLE wizard_step_aud
  ADD COLUMN verfuegbar BIT;

# Alle alte Gesuche aktualisieren
UPDATE wizard_step SET verfuegbar=false where wizard_step_status = 'UNBESUCHT';
UPDATE wizard_step SET verfuegbar=true where wizard_step_status <> 'UNBESUCHT';

# Fehler korrigieren, in aud tabelle darf nichts required sein
ALTER TABLE gesuchsteller_adresse_aud MODIFY nicht_in_gemeinde bit default false;