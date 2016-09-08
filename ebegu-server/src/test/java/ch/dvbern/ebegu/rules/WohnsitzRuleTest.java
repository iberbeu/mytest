package ch.dvbern.ebegu.rules;

import ch.dvbern.ebegu.entities.*;
import ch.dvbern.ebegu.enums.BetreuungsangebotTyp;
import ch.dvbern.ebegu.tets.TestDataUtil;
import ch.dvbern.ebegu.types.DateRange;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Tests für WohnsitzRule
 */
public class WohnsitzRuleTest {

	private final LocalDate START_PERIODE = LocalDate.of(2016, Month.AUGUST, 1);
	private final LocalDate ENDE_PERIODE = LocalDate.of(2017, Month.JULY, 31);


	@Test
	public void testNormalfallBeideAdresseInBern() {
		Betreuung betreuung = createTestdata();
		betreuung.extractGesuch().getGesuchsteller1().addAdresse(createGesuchstellerAdresse(START_PERIODE, ENDE_PERIODE, false));
		List<VerfuegungZeitabschnitt> zeitabschnittList = EbeguRuleTestsHelper.calculate(betreuung);
		Assert.assertNotNull(zeitabschnittList);
		Assert.assertEquals(1, zeitabschnittList.size());
		VerfuegungZeitabschnitt abschnittInBern = zeitabschnittList.get(0);
		Assert.assertEquals(false, abschnittInBern.isWohnsitzNichtInGemeindeGS1());
		Assert.assertEquals(100, abschnittInBern.getAnspruchberechtigtesPensum());
		Assert.assertEquals(100, abschnittInBern.getBgPensum());
	}

	@Test
	public void testZweiGesuchstellerEinerDavonInBern() {
		Betreuung betreuung = createTestdata();
		betreuung.extractGesuch().getGesuchsteller1().addAdresse(createGesuchstellerAdresse(START_PERIODE, ENDE_PERIODE, false));
		betreuung.extractGesuch().getGesuchsteller2().addAdresse(createGesuchstellerAdresse(START_PERIODE, ENDE_PERIODE, true));
		List<VerfuegungZeitabschnitt> zeitabschnittList = EbeguRuleTestsHelper.calculate(betreuung);
		Assert.assertNotNull(zeitabschnittList);
		Assert.assertEquals(1, zeitabschnittList.size());
		VerfuegungZeitabschnitt abschnittInBern = zeitabschnittList.get(0);
		Assert.assertEquals(false, abschnittInBern.isWohnsitzNichtInGemeindeGS1());
		Assert.assertEquals(100, abschnittInBern.getAnspruchberechtigtesPensum());
		Assert.assertEquals(100, abschnittInBern.getBgPensum());
	}

	@Test
	public void testZuzug() {
		LocalDate zuzugsDatum = LocalDate.of(2016, Month.OCTOBER, 16);
		Betreuung betreuung = createTestdata();
		betreuung.extractGesuch().getGesuchsteller1().addAdresse(createGesuchstellerAdresse(START_PERIODE, zuzugsDatum.minusDays(1), true));
		betreuung.extractGesuch().getGesuchsteller1().addAdresse(createGesuchstellerAdresse(zuzugsDatum, ENDE_PERIODE, false));
		betreuung.extractGesuch().getGesuchsteller2().addAdresse(createGesuchstellerAdresse(START_PERIODE, ENDE_PERIODE, true));
		List<VerfuegungZeitabschnitt> zeitabschnittList = EbeguRuleTestsHelper.calculate(betreuung);
		Assert.assertNotNull(zeitabschnittList);
		Assert.assertEquals(2, zeitabschnittList.size());
		VerfuegungZeitabschnitt abschnittNichtInBern = zeitabschnittList.get(0);
		Assert.assertEquals(true, abschnittNichtInBern.isWohnsitzNichtInGemeindeGS1());
		Assert.assertEquals(0, abschnittNichtInBern.getAnspruchberechtigtesPensum());
		Assert.assertEquals(0, abschnittNichtInBern.getBgPensum());
		VerfuegungZeitabschnitt abschnittInBern = zeitabschnittList.get(1);
		Assert.assertEquals(false, abschnittInBern.isWohnsitzNichtInGemeindeGS1());
		Assert.assertEquals(100, abschnittInBern.getAnspruchberechtigtesPensum());
		Assert.assertEquals(100, abschnittInBern.getBgPensum());
	}

	@Test
	public void testWegzug() {
		LocalDate wegzugsDatum = LocalDate.of(2016, Month.OCTOBER, 16);
		Betreuung betreuung = createTestdata();
		betreuung.extractGesuch().getGesuchsteller1().addAdresse(createGesuchstellerAdresse(START_PERIODE, wegzugsDatum.minusDays(1), false));
		betreuung.extractGesuch().getGesuchsteller1().addAdresse(createGesuchstellerAdresse(wegzugsDatum, ENDE_PERIODE, true));
		betreuung.extractGesuch().getGesuchsteller2().addAdresse(createGesuchstellerAdresse(START_PERIODE, ENDE_PERIODE, true));
		List<VerfuegungZeitabschnitt> zeitabschnittList = EbeguRuleTestsHelper.calculate(betreuung);
		Assert.assertNotNull(zeitabschnittList);
		Assert.assertEquals(2, zeitabschnittList.size());
		VerfuegungZeitabschnitt abschnittInBern = zeitabschnittList.get(0);
		Assert.assertEquals(false, abschnittInBern.isWohnsitzNichtInGemeindeGS1());
		Assert.assertEquals(100, abschnittInBern.getAnspruchberechtigtesPensum());
		Assert.assertEquals(100, abschnittInBern.getBgPensum());
		VerfuegungZeitabschnitt abschnittNichtInBern = zeitabschnittList.get(1);
		Assert.assertEquals(true, abschnittNichtInBern.isWohnsitzNichtInGemeindeGS1());
		Assert.assertEquals(0, abschnittNichtInBern.getAnspruchberechtigtesPensum());
		Assert.assertEquals(0, abschnittNichtInBern.getBgPensum());
	}

	private Betreuung createTestdata() {
		Betreuung betreuung = TestDataUtil.createGesuchWithBetreuungspensum(true);
		betreuung.getInstitutionStammdaten().setBetreuungsangebotTyp(BetreuungsangebotTyp.KITA);
		betreuung.setBetreuungspensumContainers(new LinkedHashSet<>());
		BetreuungspensumContainer betreuungspensumContainer = new BetreuungspensumContainer();
		betreuungspensumContainer.setBetreuung(betreuung);
		DateRange gueltigkeit = new DateRange(START_PERIODE, ENDE_PERIODE);
		betreuungspensumContainer.setBetreuungspensumJA(new Betreuungspensum(gueltigkeit));
		betreuungspensumContainer.getBetreuungspensumJA().setPensum(100);
		betreuung.getBetreuungspensumContainers().add(betreuungspensumContainer);
		betreuung.getKind().getKindJA().setWohnhaftImGleichenHaushalt(100);
		betreuung.getKind().getGesuch().getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 100, 0));
		betreuung.getKind().getGesuch().getGesuchsteller2().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 100, 0));
		return betreuung;
	}

	private GesuchstellerAdresse createGesuchstellerAdresse(LocalDate von, LocalDate bis, boolean nichtInGemeinde) {
		GesuchstellerAdresse adresse = TestDataUtil.createDefaultGesuchstellerAdresse();
		adresse.setNichtInGemeinde(nichtInGemeinde);
		adresse.getGueltigkeit().setGueltigAb(von);
		adresse.getGueltigkeit().setGueltigBis(bis);
		return adresse;
	}
}