package ch.dvbern.ebegu.vorlagen.finanziellesituation;
/*
* Copyright (c) 2016 DV Bern AG, Switzerland
*
* Das vorliegende Dokument, einschliesslich aller seiner Teile, ist urheberrechtlich
* geschuetzt. Jede Verwertung ist ohne Zustimmung der DV Bern AG unzulaessig. Dies gilt
* insbesondere fuer Vervielfaeltigungen, die Einspeicherung und Verarbeitung in
* elektronischer Form. Wird das Dokument einem Kunden im Rahmen der Projektarbeit zur
* Ansicht uebergeben ist jede weitere Verteilung durch den Kunden an Dritte untersagt.
*
* Ersteller: zeab am: 22.08.2016
*/

import ch.dvbern.ebegu.entities.AbstractFinanzielleSituation;
import ch.dvbern.ebegu.entities.Einkommensverschlechterung;
import ch.dvbern.ebegu.util.FinanzielleSituationRechner;

import java.math.BigDecimal;

/**
 * Implementiert den {@link EinkommensverschlechterungPrint}
 */
public class EinkommensverschlechterungPrintImpl extends FinanzDatenPrintImpl implements EinkommensverschlechterungPrint {

	private String einkommensverschlechterungJahr;
	private String ereigniseintritt;
	private String grund;
	private Einkommensverschlechterung ev1;
	private Einkommensverschlechterung ev2;

	/**
	 * Konstruktor
	 *
	 * @param fsGesuchsteller1 das {@link FinanzSituationGesuchsteller1}
	 * @param fsGesuchsteller2 das {@link FinanzSituationGesuchsteller2}
	 * @param einkommensverschlechterungJahr das Jahr des Einkommenverschleschterung
	 * @param ereigniseintritt Ereingis datum
	 * @param grund Grund
	 */
	public EinkommensverschlechterungPrintImpl(FinanzSituationPrintGesuchsteller fsGesuchsteller1, FinanzSituationPrintGesuchsteller fsGesuchsteller2,
			String einkommensverschlechterungJahr, String ereigniseintritt, String grund) {

		super(fsGesuchsteller1, fsGesuchsteller2);

		this.einkommensverschlechterungJahr = einkommensverschlechterungJahr;
		this.ereigniseintritt = ereigniseintritt;
		this.grund = grund;
		this.ev1 = fsGesuchsteller1.getEinkommensverschlechterung1();
		if (fsGesuchsteller2 != null && fsGesuchsteller2.getEinkommensverschlechterung2() != null) {
			this.ev2 = fsGesuchsteller2.getEinkommensverschlechterung2();
		}

	}

	@Override
	public String getEinkommensverschlechterungJahr() {

		return einkommensverschlechterungJahr;
	}

	@Override
	public String getEreigniseintritt() {

		return ereigniseintritt;
	}

	@Override
	public String getGrund() {

		return grund;
	}



	@Override
	public BigDecimal getGeschaeftsgewinnG1() {
		return FinanzielleSituationRechner.calcGeschaeftsgewinnDurchschnitt(ev1, this.fsGesuchsteller1.getFinanzielleSituation());
	}

	@Override
	public BigDecimal getGeschaeftsgewinnG2() {
		//hier muessen zum berechnen die Einkommensverschlechterung und die finanzielle Situation benutzt werden
		if (fsGesuchsteller2 != null ) {
			return FinanzielleSituationRechner.calcGeschaeftsgewinnDurchschnitt(ev2, this.fsGesuchsteller2.getFinanzielleSituation());
		}
		return null;
	}

	@Override
	protected AbstractFinanzielleSituation getFinanzSituationGS1() {
		return ev1;
	}

	@Override
	protected AbstractFinanzielleSituation getFinanzSituationGS2() {
		return ev2;
	}


}
