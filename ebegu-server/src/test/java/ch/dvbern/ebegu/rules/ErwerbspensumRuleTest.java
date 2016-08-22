package ch.dvbern.ebegu.rules;

import ch.dvbern.ebegu.entities.Betreuung;
import ch.dvbern.ebegu.entities.Gesuch;
import ch.dvbern.ebegu.entities.VerfuegungZeitabschnitt;
import ch.dvbern.ebegu.tets.TestDataUtil;
import ch.dvbern.ebegu.util.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Tests für ErwerbspensumRule
 */
public class ErwerbspensumRuleTest {

	private final ErwerbspensumRule erwerbspensumRule = new ErwerbspensumRule(Constants.DEFAULT_GUELTIGKEIT);

	private final LocalDate START_PERIODE = LocalDate.of(2016, Month.AUGUST, 1);
	private final LocalDate ENDE_PERIODE = LocalDate.of(2017, Month.JULY, 31);


	@Test
	public void testKeinErwerbspensum() {
		Betreuung betreuung = TestDataUtil.createGesuchWithBetreuungspensum(true);

		List<VerfuegungZeitabschnitt> result = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertNotNull(result);
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testNormalfallZweiGesuchsteller() {
		Betreuung betreuung = TestDataUtil.createGesuchWithBetreuungspensum(true);
		Gesuch gesuch = betreuung.extractGesuch();

		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 100, 0));
		gesuch.getGesuchsteller2().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 40, 0));

		List<VerfuegungZeitabschnitt> result = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(40, result.get(0).getAnspruchberechtigtesPensum());
		Assert.assertTrue(result.get(0).getBemerkungen().isEmpty());
	}

	@Test
	public void testNormalfallEinGesuchsteller() {
		Betreuung betreuung = TestDataUtil.createGesuchWithBetreuungspensum(false);
		Gesuch gesuch = betreuung.extractGesuch();

		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 60, 0));

		List<VerfuegungZeitabschnitt> result = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(60, result.get(0).getAnspruchberechtigtesPensum());
		Assert.assertTrue(result.get(0).getBemerkungen().isEmpty());
	}

	@Test
	public void testNurEinErwerbspensumBeiZweiGesuchstellern() throws Exception {
		Betreuung betreuung = TestDataUtil.createGesuchWithBetreuungspensum(true);
		Gesuch gesuch = betreuung.extractGesuch();

		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 80, 0));

		List<VerfuegungZeitabschnitt> result = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(0, result.get(0).getAnspruchberechtigtesPensum());
		Assert.assertFalse(result.get(0).getBemerkungen().isEmpty());
	}

	@Test
	public void testMehrAls100ProzentBeiEinemGesuchsteller() {
		Betreuung betreuung = TestDataUtil.createGesuchWithBetreuungspensum(false);
		Gesuch gesuch = betreuung.extractGesuch();

		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 100, 10));

		List<VerfuegungZeitabschnitt> result = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(100, result.get(0).getAnspruchberechtigtesPensum());
		Assert.assertFalse(result.get(0).getBemerkungen().isEmpty());
	}

	/**
	 * das Pensum muss wie folgt abgerundet werden:
	 * X0 - X4 = X0
	 * X5 - X9 = Y0, wo Y=X+1
	 * @throws Exception
     */
	@Test
	public void testRoundToTens() throws Exception {
		Betreuung betreuung = TestDataUtil.createGesuchWithBetreuungspensum(false);
		Gesuch gesuch = betreuung.extractGesuch();

		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, -1, 0));
		List<VerfuegungZeitabschnitt> result = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(0, result.get(0).getAnspruchberechtigtesPensum());

		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 0, 0));
		List<VerfuegungZeitabschnitt> result2 = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(0, result2.get(0).getAnspruchberechtigtesPensum());

		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 1, 0));
		List<VerfuegungZeitabschnitt> result3 = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(0, result3.get(0).getAnspruchberechtigtesPensum());

		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 50, 0));
		List<VerfuegungZeitabschnitt> result4 = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(50, result4.get(0).getAnspruchberechtigtesPensum());

		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 51, 0));
		List<VerfuegungZeitabschnitt> result5 = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(50, result5.get(0).getAnspruchberechtigtesPensum());

		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 54, 0));
		List<VerfuegungZeitabschnitt> result6 = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(50, result6.get(0).getAnspruchberechtigtesPensum());

		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 55, 0));
		List<VerfuegungZeitabschnitt> result7 = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(60, result7.get(0).getAnspruchberechtigtesPensum());

		//mit zuschlag
		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 50, 5));
		List<VerfuegungZeitabschnitt> result8 = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(60, result8.get(0).getAnspruchberechtigtesPensum());

		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 59, 0));
		List<VerfuegungZeitabschnitt> result9 = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(60, result9.get(0).getAnspruchberechtigtesPensum());

		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 99, 0));
		List<VerfuegungZeitabschnitt> result10 = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(100, result10.get(0).getAnspruchberechtigtesPensum());

		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 100, 0));
		List<VerfuegungZeitabschnitt> result11 = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(100, result11.get(0).getAnspruchberechtigtesPensum());

		gesuch.getGesuchsteller1().setErwerbspensenContainers(new HashSet<>());
		gesuch.getGesuchsteller1().addErwerbspensumContainer(TestDataUtil.createErwerbspensum(START_PERIODE, ENDE_PERIODE, 101, 0));
		List<VerfuegungZeitabschnitt> result12 = erwerbspensumRule.calculate(betreuung, new ArrayList<>());
		Assert.assertEquals(100, result12.get(0).getAnspruchberechtigtesPensum());

	}
}