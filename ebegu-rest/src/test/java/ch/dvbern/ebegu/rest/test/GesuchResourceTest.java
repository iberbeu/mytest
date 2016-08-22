package ch.dvbern.ebegu.rest.test;

import ch.dvbern.ebegu.api.converter.JaxBConverter;
import ch.dvbern.ebegu.api.dtos.JaxGesuch;
import ch.dvbern.ebegu.api.dtos.JaxKindContainer;
import ch.dvbern.ebegu.api.resource.GesuchResource;
import ch.dvbern.ebegu.entities.*;
import ch.dvbern.ebegu.enums.Betreuungsstatus;
import ch.dvbern.ebegu.enums.UserRole;
import ch.dvbern.ebegu.errors.EbeguException;
import ch.dvbern.ebegu.services.AuthService;
import ch.dvbern.ebegu.services.BenutzerService;
import ch.dvbern.ebegu.services.InstitutionService;
import ch.dvbern.ebegu.tets.TestDataUtil;
import ch.dvbern.lib.cdipersistence.Persistence;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * Testet GesuchResource
 */
@RunWith(Arquillian.class)
@UsingDataSet("datasets/empty.xml")
@Transactional(TransactionMode.DISABLED)
public class GesuchResourceTest extends AbstractEbeguRestTest {

	@Deployment
	public static Archive<?> createDeploymentEnvironment() {
		return createTestArchive();
	}

	@Inject
	private GesuchResource gesuchResource;
	@Inject
	private AuthService authService;
	@Inject
	private InstitutionService institutionService;
	@Inject
	private BenutzerService benutzerService;
	@Inject
	private Persistence<Gesuch> persistence;
	@Inject
	private JaxBConverter converter;


	@Test
	public void testFindGesuchForInstitution() throws EbeguException {
		final Gesuch gesuch = TestDataUtil.createAndPersistWaeltiDagmarGesuch(institutionService, persistence);
		changeStatusToWarten(gesuch.getKindContainers().iterator().next());
		persistUser(UserRole.SACHBEARBEITER_INSTITUTION,
			gesuch.getKindContainers().iterator().next().getBetreuungen().iterator().next().getInstitutionStammdaten().getInstitution(),
			null, gesuch.getKindContainers().iterator().next().getBetreuungen().iterator().next().getInstitutionStammdaten().getInstitution().getMandant());
		final JaxGesuch gesuchForInstitution = gesuchResource.findGesuchForInstitution(converter.toJaxId(gesuch));

		Assert.assertNull(gesuchForInstitution.getEinkommensverschlechterungInfo());

		Assert.assertNotNull(gesuchForInstitution.getGesuchsteller1());
		Assert.assertNull(gesuchForInstitution.getGesuchsteller1().getEinkommensverschlechterungContainer());
		Assert.assertNull(gesuchForInstitution.getGesuchsteller1().getErwerbspensenContainers());
		Assert.assertNull(gesuchForInstitution.getGesuchsteller1().getFinanzielleSituationContainer());

		Assert.assertNull(gesuchForInstitution.getGesuchsteller2()); //GS2 ist von Anfang an nicht gesetzt

		Assert.assertNotNull(gesuchForInstitution.getKindContainers());
		Assert.assertEquals(1, gesuchForInstitution.getKindContainers().size());

		final Iterator<JaxKindContainer> iterator = gesuchForInstitution.getKindContainers().iterator();
		final JaxKindContainer kind = iterator.next();
		Assert.assertNotNull(kind);
		Assert.assertNotNull(kind.getBetreuungen());
		Assert.assertEquals(1, kind.getBetreuungen().size());
	}

	@Test
	public void testFindGesuchForTraegerschaft() throws EbeguException {
		final Gesuch gesuch = TestDataUtil.createAndPersistWaeltiDagmarGesuch(institutionService, persistence);
		changeStatusToWarten(gesuch.getKindContainers().iterator().next());
		persistUser(UserRole.SACHBEARBEITER_TRAEGERSCHAFT, null,
			gesuch.getKindContainers().iterator().next().getBetreuungen().iterator().next().getInstitutionStammdaten().getInstitution().getTraegerschaft(),
			gesuch.getKindContainers().iterator().next().getBetreuungen().iterator().next().getInstitutionStammdaten().getInstitution().getMandant());

		final JaxGesuch gesuchForInstitution = gesuchResource.findGesuchForInstitution(converter.toJaxId(gesuch));

		Assert.assertNull(gesuchForInstitution.getEinkommensverschlechterungInfo());

		Assert.assertNotNull(gesuchForInstitution.getGesuchsteller1());
		Assert.assertNull(gesuchForInstitution.getGesuchsteller1().getEinkommensverschlechterungContainer());
		Assert.assertNull(gesuchForInstitution.getGesuchsteller1().getErwerbspensenContainers());
		Assert.assertNull(gesuchForInstitution.getGesuchsteller1().getFinanzielleSituationContainer());

		Assert.assertNull(gesuchForInstitution.getGesuchsteller2()); //GS2 ist von Anfang an nicht gesetzt

		Assert.assertNotNull(gesuchForInstitution.getKindContainers());
		Assert.assertEquals(1, gesuchForInstitution.getKindContainers().size());

		final Iterator<JaxKindContainer> iterator = gesuchForInstitution.getKindContainers().iterator();
		final JaxKindContainer kind = iterator.next();
		Assert.assertNotNull(kind);
		Assert.assertNotNull(kind.getBetreuungen());
		Assert.assertEquals(1, kind.getBetreuungen().size());
	}

	@Test
	public void testFindGesuchForOtherRole() throws EbeguException {
		final Gesuch gesuch = TestDataUtil.createAndPersistWaeltiDagmarGesuch(institutionService, persistence);
		persistUser(UserRole.GESUCHSTELLER, null,
			gesuch.getKindContainers().iterator().next().getBetreuungen().iterator().next().getInstitutionStammdaten().getInstitution().getTraegerschaft(),
			gesuch.getKindContainers().iterator().next().getBetreuungen().iterator().next().getInstitutionStammdaten().getInstitution().getMandant());

		final JaxGesuch gesuchForInstitution = gesuchResource.findGesuchForInstitution(converter.toJaxId(gesuch));

		Assert.assertNull(gesuchForInstitution.getEinkommensverschlechterungInfo());

		Assert.assertNotNull(gesuchForInstitution.getGesuchsteller1());
		Assert.assertNull(gesuchForInstitution.getGesuchsteller1().getEinkommensverschlechterungContainer());
		Assert.assertNull(gesuchForInstitution.getGesuchsteller1().getErwerbspensenContainers());
		Assert.assertNull(gesuchForInstitution.getGesuchsteller1().getFinanzielleSituationContainer());

		Assert.assertNull(gesuchForInstitution.getGesuchsteller2()); //GS2 ist von Anfang an nicht gesetzt

		Assert.assertNotNull(gesuchForInstitution.getKindContainers());
		Assert.assertEquals(0, gesuchForInstitution.getKindContainers().size());
	}


	// HELP METHODS

	private void persistUser(final UserRole role, final Institution institution, final Traegerschaft traegerschaft, final Mandant mandant) {
		Benutzer benutzer = TestDataUtil.createDefaultBenutzer();
		benutzer.setRole(role);
		benutzer.setUsername("anonymous");
		benutzer.setInstitution(institution);
		benutzer.setTraegerschaft(traegerschaft);
		benutzer.setMandant(mandant);
		benutzerService.saveBenutzer(benutzer);
	}

	private void changeStatusToWarten(KindContainer kindContainer) {
		final Iterator<Betreuung> iterator = kindContainer.getBetreuungen().iterator();
		while (iterator.hasNext()) {
			Betreuung betreuung = iterator.next();
			betreuung.setBetreuungsstatus(Betreuungsstatus.WARTEN);
			persistence.merge(betreuung);
		}
	}
}