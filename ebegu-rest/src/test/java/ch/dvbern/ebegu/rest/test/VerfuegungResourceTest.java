package ch.dvbern.ebegu.rest.test;

import ch.dvbern.ebegu.api.dtos.JaxId;
import ch.dvbern.ebegu.api.dtos.JaxVerfuegung;
import ch.dvbern.ebegu.api.resource.VerfuegungResource;
import ch.dvbern.ebegu.entities.Gesuch;
import ch.dvbern.ebegu.errors.EbeguException;
import ch.dvbern.ebegu.services.InstitutionService;
import ch.dvbern.ebegu.tets.TestDataUtil;
import ch.dvbern.lib.cdipersistence.Persistence;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * Testet VerfuegungResource
 */
@RunWith(Arquillian.class)
@UsingDataSet("datasets/empty.xml")
@Transactional(TransactionMode.DISABLED)
public class VerfuegungResourceTest extends AbstractEbeguRestTest {

	@Inject
	private VerfuegungResource verfuegungResource;
	@Inject
	private InstitutionService instService;
	@Inject
	private Persistence<Gesuch> persistence;

	@Test
	public void saveVerfuegungTest() throws EbeguException {
		final Gesuch gesuch = TestDataUtil.createAndPersistWaeltiDagmarGesuch(instService, persistence);
		final String betreuungId = gesuch.getKindContainers().iterator().next().getBetreuungen().iterator().next().getId();

		final JaxVerfuegung verfuegungJax = new JaxVerfuegung();
		verfuegungJax.setGeneratedBemerkungen("genBemerkung");
		verfuegungJax.setManuelleBemerkungen("manBemerkung");

		final JaxVerfuegung persistedVerfuegung = verfuegungResource.saveVerfuegung(new JaxId(gesuch.getId()), new JaxId(betreuungId), verfuegungJax);

		Assert.assertEquals(verfuegungJax.getGeneratedBemerkungen(), persistedVerfuegung.getGeneratedBemerkungen());
		Assert.assertEquals(verfuegungJax.getManuelleBemerkungen(), persistedVerfuegung.getManuelleBemerkungen());
	}
}