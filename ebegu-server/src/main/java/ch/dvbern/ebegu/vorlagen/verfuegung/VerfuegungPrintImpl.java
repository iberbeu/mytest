package ch.dvbern.ebegu.vorlagen.verfuegung;
/*
* Copyright (c) 2016 DV Bern AG, Switzerland
*
* Das vorliegende Dokument, einschliesslich aller seiner Teile, ist urheberrechtlich
* geschuetzt. Jede Verwertung ist ohne Zustimmung der DV Bern AG unzulaessig. Dies gilt
* insbesondere fuer Vervielfaeltigungen, die Einspeicherung und Verarbeitung in
* elektronischer Form. Wird das Dokument einem Kunden im Rahmen der Projektarbeit zur
* Ansicht uebergeben ist jede weitere Verteilung durch den Kunden an Dritte untersagt.
*
* Ersteller: zeab am: 12.08.2016
*/

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import ch.dvbern.ebegu.entities.Betreuung;
import ch.dvbern.ebegu.entities.Kind;
import ch.dvbern.ebegu.entities.Verfuegung;
import ch.dvbern.ebegu.util.Constants;

/**
 * Transferobjekt
 */
public class VerfuegungPrintImpl implements VerfuegungPrint {

	private Betreuung betreuung;

	/**
	 * @param betreuung
	 */
	public VerfuegungPrintImpl(Betreuung betreuung) {

		this.betreuung = betreuung;
	}

	@Override
	public String getTitel() {

		// TODO ZEAB Implementieren
		return "Verfügung / Bestätigung";
	}

	@Override
	public String getAngebot() {

		// TODO ZEAB Implementieren
		return "Kita";
	}

	@Override
	public String getInstitution() {

		return betreuung.getInstitutionStammdaten().getInstitution().getName();
	}

	/**
	 * @return Gesuchsteller-ReferenzNummer
	 */
	@Override
	public String getReferenznummer() {

		return betreuung.getBGNummer();
	}

	/**
	 * @return Gesuchsteller-Verfuegungsdatum
	 */
	@Override
	public String getVerfuegungsdatum() {

		Optional<Verfuegung> verfuegung = extractVerfuegung();
		if (verfuegung.isPresent()) {
			Verfuegung verfuegung1 = verfuegung.get();
			if (verfuegung1.getTimestampErstellt() != null) {
				// TODO ZEAB ist das Setzen der Verfuegungsdatum Korrekt
				return Constants.DATE_FORMATTER.format(verfuegung1.getTimestampErstellt());
			}
		}
		// TODO ZEAB was muss hier passieren?? Leer ausdrucken??
		return "";
	}

	/**
	 * @return Name Vorname des Kindes
	 */
	@Override
	public String getKindNameVorname() {

		return extractKind().getFullName();
	}

	/**
	 * @return Geburtsdatum des Kindes
	 */
	@Override
	public String getKindGeburtsdatum() {

		return Constants.DATE_FORMATTER.format(betreuung.getKind().getKindJA().getGeburtsdatum());
	}

	/**
	 * @return Kita Name
	 */
	public String getKitaBezeichnung() {

		return betreuung.getInstitutionStammdaten().getInstitution().getName();
	}

	/**
	 * @return AnspruchAb
	 */
	@Override
	public String getAnspruchAb() {

		return Constants.DATE_FORMATTER.format(betreuung.extractGesuchsperiode().getGueltigkeit().getGueltigAb());
	}

	/**
	 * @return AnspruchBis
	 */
	@Override
	public String getAnspruchBis() {

		return Constants.DATE_FORMATTER.format(betreuung.extractGesuchsperiode().getGueltigkeit().getGueltigBis());
	}

	/**
	 * @return VerfuegungZeitabschnitten
	 */
	@Override
	public List<VerfuegungZeitabschnittPrint> getVerfuegungZeitabschnitt() {

		List<VerfuegungZeitabschnittPrint> result = new ArrayList<>();
		Optional<Verfuegung> verfuegung = extractVerfuegung();
		if (verfuegung.isPresent()) {
			result.addAll(verfuegung.get().getZeitabschnitte().stream().map(VerfuegungZeitabschnittPrintImpl::new).collect(Collectors.toList()));
		}
		return result;
	}

	/**
	 * Wenn die Betreuung VERFUEGT ist -> manuelle Bemerkungen Wenn die Betreuung noch nicht VERFUEGT ist -> generated
	 * Bemerkungen
	 *
	 * @return
	 */
	public List<BemerkungPrint> getManuelleBemerkungen() {

		List<BemerkungPrint> bemerkungen = new ArrayList<>();
		Optional<Verfuegung> verfuegung = extractVerfuegung();
		if (verfuegung.isPresent()) {
			if (verfuegung.get().getManuelleBemerkungen() != null && !"".equalsIgnoreCase(verfuegung.get().getManuelleBemerkungen())) {
				bemerkungen.addAll(splitBemerkungen(verfuegung.get().getManuelleBemerkungen()));
			} else if (verfuegung.get().getGeneratedBemerkungen() != null && !"".equalsIgnoreCase(verfuegung.get().getGeneratedBemerkungen())) {
				bemerkungen.addAll(splitBemerkungen((verfuegung.get().getGeneratedBemerkungen())));
			}
		}
		return bemerkungen;
	}

	/**
	 * Zerlegt die Bemerkungen (Delimiter \n) und bereitet die in einer Liste.
	 * @param bemerkungen
	 * @return List mit Bemerkungen
	 */
	private List<BemerkungPrint> splitBemerkungen(String bemerkungen) {

		List<BemerkungPrint> list = new ArrayList<>();
		String[] splitBemerkungen = bemerkungen.split("\n");
		for (String bemerkung : splitBemerkungen) {
			list.add(new BemerkungPrintImpl(bemerkung));
		}
		return list;
	}

	/**
	 * @return true falls Pensum groesser 0 ist
	 */
	@Override
	public boolean isPensumGrosser0() {

		List<VerfuegungZeitabschnittPrint> vzList = getVerfuegungZeitabschnitt();
		int value = 0;
		for (VerfuegungZeitabschnittPrint verfuegungZeitabschnitt : vzList) {
			value = value + verfuegungZeitabschnitt.getBGPensum();
			// BG-Pensum
		}
		return value > 0;
	}

	@Override
	public boolean isPensumIst0() {

		return !isPensumGrosser0();
	}

	/**
	 * @return true falls es sich um eine Mutation handelt
	 */
	public boolean isMutation() {

		// TODO Team: Muss angepasst werden, sobald wir Mutationen unterstuetzen
		return true;
	}

	@Override
	public boolean isPrintManuellebemerkung() {

		return getManuelleBemerkungen().size() > 0;
	}

	@Nonnull
	private Kind extractKind() {

		return betreuung.getKind().getKindJA();
	}

	@Nonnull
	private Optional<Verfuegung> extractVerfuegung() {

		Verfuegung verfuegung = betreuung.getVerfuegung();
		if (verfuegung != null) {
			return Optional.of(verfuegung);
		}
		return Optional.empty();
	}
}
