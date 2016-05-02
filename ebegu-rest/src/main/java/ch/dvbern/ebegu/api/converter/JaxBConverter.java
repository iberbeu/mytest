package ch.dvbern.ebegu.api.converter;

import ch.dvbern.ebegu.api.dtos.*;
import ch.dvbern.ebegu.entities.*;
import ch.dvbern.ebegu.enums.ErrorCodeEnum;
import ch.dvbern.ebegu.errors.EbeguEntityNotFoundException;
import ch.dvbern.ebegu.services.AdresseService;
import ch.dvbern.ebegu.services.FallService;
import ch.dvbern.ebegu.services.FinanzielleSituationService;
import ch.dvbern.ebegu.services.PersonService;
import ch.dvbern.ebegu.util.Constants;
import ch.dvbern.lib.date.DateConvertUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;


@Dependent
@SuppressWarnings({"PMD.NcssTypeCount", "unused"})
public class JaxBConverter {

	@Inject
	private PersonService personService;

	@Inject
	private AdresseService adresseService;

	@Inject
	private FinanzielleSituationService finanzielleSituationService;

	@Inject
	private FallService fallService;

	private static final Logger LOG = LoggerFactory.getLogger(JaxBConverter.class);


	@Nonnull
	public String toResourceId(@Nonnull final AbstractEntity entity) {
		return String.valueOf(Objects.requireNonNull(entity.getId()));
	}

	@Nonnull
	public String toEntityId(@Nonnull final JaxId resourceId) {
		// TODO wahrscheinlich besser manuell auf NULL pruefen und gegebenenfalls eine IllegalArgumentException werfen
		return Objects.requireNonNull(resourceId.getId());
	}

	@Nonnull
	public JaxId toJaxId(@Nonnull final AbstractEntity entity) {
		return new JaxId(entity.getId());
	}

	@Nonnull
	public JaxId toJaxId(@Nonnull final JaxAbstractDTO entity) {
		return new JaxId(entity.getId());
	}

	@Nonnull
	private <T extends JaxAbstractDTO> T convertAbstractFieldsToJAX(@Nonnull final AbstractEntity abstEntity, T jaxDTOToConvertTo) {
		jaxDTOToConvertTo.setTimestampErstellt(abstEntity.getTimestampErstellt());
		jaxDTOToConvertTo.setTimestampMutiert(abstEntity.getTimestampMutiert());
		jaxDTOToConvertTo.setId(checkNotNull(abstEntity.getId()));
		return jaxDTOToConvertTo;
	}

	@Nonnull
	private <T extends AbstractEntity> T convertAbstractFieldsToEntity(JaxAbstractDTO jaxToConvert, @Nonnull final T abstEntityToConvertTo) {
		if (jaxToConvert.getId() != null) {
			abstEntityToConvertTo.setId(jaxToConvert.getId());
			//ACHTUNG hier timestamp erstellt und mutiert NICHT  konvertieren da diese immer auf dem server gesetzt werden muessen
		}

		return abstEntityToConvertTo;
	}

	@Nonnull
	public JaxApplicationProperties applicationPropertieToJAX(@Nonnull final ApplicationProperty applicationProperty) {
		JaxApplicationProperties jaxProperty = new JaxApplicationProperties();
		convertAbstractFieldsToJAX(applicationProperty, jaxProperty);
		jaxProperty.setName(applicationProperty.getName());
		jaxProperty.setValue(applicationProperty.getValue());
		return jaxProperty;
	}

	@Nonnull
	public ApplicationProperty applicationPropertieToEntity(JaxApplicationProperties jaxAP, @Nonnull final ApplicationProperty applicationProperty) {
		Validate.notNull(applicationProperty);
		Validate.notNull(jaxAP);
		convertAbstractFieldsToEntity(jaxAP, applicationProperty);
		applicationProperty.setName(jaxAP.getName());
		applicationProperty.setValue(jaxAP.getValue());

		return applicationProperty;
	}

	@Nonnull
	public Adresse adresseToEntity(@Nonnull JaxAdresse jaxAdresse, @Nonnull final Adresse adresse) {
		Validate.notNull(adresse);
		Validate.notNull(jaxAdresse);
		convertAbstractFieldsToEntity(jaxAdresse, adresse);
		adresse.setStrasse(jaxAdresse.getStrasse());
		adresse.setHausnummer(jaxAdresse.getHausnummer());
		adresse.setZusatzzeile(jaxAdresse.getZusatzzeile());
		adresse.setPlz(jaxAdresse.getPlz());
		adresse.setOrt(jaxAdresse.getOrt());
		adresse.setGemeinde(jaxAdresse.getGemeinde());
		adresse.setLand(jaxAdresse.getLand());
		adresse.setGueltigAb(jaxAdresse.getGueltigAb() == null ? Constants.START_OF_TIME : jaxAdresse.getGueltigAb());
		adresse.setGueltigBis(jaxAdresse.getGueltigBis() == null ? Constants.END_OF_TIME : jaxAdresse.getGueltigBis());
		adresse.setAdresseTyp(jaxAdresse.getAdresseTyp());

		return adresse;
	}

	@Nonnull
	public JaxAdresse adresseToJAX(@Nonnull final Adresse adresse) {
		JaxAdresse jaxAdresse = new JaxAdresse();
		convertAbstractFieldsToJAX(adresse, jaxAdresse);
		jaxAdresse.setStrasse(adresse.getStrasse());
		jaxAdresse.setHausnummer(adresse.getHausnummer());
		jaxAdresse.setZusatzzeile(adresse.getZusatzzeile());
		jaxAdresse.setPlz(adresse.getPlz());
		jaxAdresse.setOrt(adresse.getOrt());
		jaxAdresse.setGemeinde(adresse.getGemeinde());
		jaxAdresse.setLand(adresse.getLand());
		jaxAdresse.setGueltigAb(adresse.getGueltigAb());
		jaxAdresse.setGueltigBis(adresse.getGueltigBis());
		jaxAdresse.setAdresseTyp(adresse.getAdresseTyp());
		return jaxAdresse;
	}

	@Nonnull
	public JaxEnversRevision enversRevisionToJAX(@Nonnull final DefaultRevisionEntity revisionEntity,
												 @Nonnull final AbstractEntity abstractEntity, RevisionType accessType) {

		JaxEnversRevision jaxEnversRevision = new JaxEnversRevision();
		if (abstractEntity instanceof ApplicationProperty) {
			jaxEnversRevision.setEntity(applicationPropertieToJAX((ApplicationProperty) abstractEntity));
		}
		jaxEnversRevision.setRev(revisionEntity.getId());
		jaxEnversRevision.setRevTimeStamp(DateConvertUtils.asLocalDateTime(revisionEntity.getRevisionDate()));
		jaxEnversRevision.setAccessType(accessType);
		return jaxEnversRevision;
	}

	public Person personToEntity(@Nonnull JaxPerson personJAXP, @Nonnull Person person) {
		Validate.notNull(person);
		Validate.notNull(personJAXP);
		Validate.notNull(personJAXP.getWohnAdresse(),"Wohnadresse muss gesetzt sein");
		convertAbstractFieldsToEntity(personJAXP, person);
		person.setNachname(personJAXP.getNachname());
		person.setVorname(personJAXP.getVorname());
		person.setGeburtsdatum(personJAXP.getGeburtsdatum());
		person.setGeschlecht(personJAXP.getGeschlecht());
		person.setMail(personJAXP.getMail());
		person.setTelefon(personJAXP.getTelefon());
		person.setMobile(personJAXP.getMobile());
		person.setTelefonAusland(personJAXP.getTelefonAusland());
		person.setZpvNumber(personJAXP.getZpvNumber());

		//Relationen
		//Wir fuehren derzeit immer maximal  eine alternative Korrespondenzadressse -> diese updaten wenn vorhanden
		if (personJAXP.getAlternativeAdresse() != null) {
			Adresse currentAltAdr = adresseService.getKorrespondenzAdr(person.getId()).orElse(new Adresse());
			Adresse altAddrToMerge = adresseToEntity(personJAXP.getAlternativeAdresse(), currentAltAdr);
			person.addAdresse(altAddrToMerge);
		}
		// Umzug und Wohnadresse
		Adresse umzugAddr = null;
		if (personJAXP.getUmzugAdresse() != null) {
			umzugAddr = toStoreableAddresse(personJAXP.getUmzugAdresse());
			person.addAdresse(umzugAddr);
		}
		//Wohnadresse (abh von Umzug noch datum setzten)
		Adresse wohnAddrToMerge = toStoreableAddresse(personJAXP.getWohnAdresse());
		if (umzugAddr != null) {
			wohnAddrToMerge.setGueltigBis(umzugAddr.getGueltigAb().minusDays(1));
		}
		// Finanzielle Situation
		person.addAdresse(wohnAddrToMerge);
		if (personJAXP.getFinanzielleSituationContainer() != null) {
			person.setFinanzielleSituationContainer(finanzielleSituationContainerToEntity(personJAXP.getFinanzielleSituationContainer(), new FinanzielleSituationContainer()));
		}
		return person;
	}

	@Nonnull
	private Adresse toStoreableAddresse(@Nonnull JaxAdresse adresseToPrepareForSaving) {
		Adresse adrToMergeWith = new Adresse();
		if (adresseToPrepareForSaving.getId() != null ) {

			Optional<Adresse> altAdr = adresseService.findAdresse(adresseToPrepareForSaving.getId());
			//wenn schon vorhanden updaten
			if (altAdr.isPresent()) {
				adrToMergeWith = altAdr.get();
			}
		}
		return  adresseToEntity(adresseToPrepareForSaving, adrToMergeWith);
	}

	public JaxPerson personToJAX(@Nonnull Person persistedPerson) {
		Validate.isTrue(!persistedPerson.isNew(), "Person kann nicht nach REST transformiert werden weil sie noch " +
			"nicht persistiert wurde; Grund dafuer ist, dass wir die aktuelle Wohnadresse aus der Datenbank lesen wollen");
		JaxPerson jaxPerson = new JaxPerson();
		convertAbstractFieldsToJAX(persistedPerson, jaxPerson);
		jaxPerson.setNachname(persistedPerson.getNachname());
		jaxPerson.setVorname(persistedPerson.getVorname());
		jaxPerson.setGeburtsdatum(persistedPerson.getGeburtsdatum());
		jaxPerson.setGeschlecht(persistedPerson.getGeschlecht());
		jaxPerson.setMail(persistedPerson.getMail());
		jaxPerson.setTelefon(persistedPerson.getTelefon());
		jaxPerson.setMobile(persistedPerson.getMobile());
		jaxPerson.setTelefonAusland(persistedPerson.getTelefonAusland());
		jaxPerson.setZpvNumber(persistedPerson.getZpvNumber());
		//relationen laden
		Optional<Adresse> altAdr = adresseService.getKorrespondenzAdr(persistedPerson.getId());
		altAdr.ifPresent(adresse -> jaxPerson.setAlternativeAdresse(adresseToJAX(adresse)));
		Adresse currentWohnadr = adresseService.getCurrentWohnadresse(persistedPerson.getId());
		jaxPerson.setWohnAdresse(adresseToJAX(currentWohnadr));

		//wenn heute gueltige Adresse von der Adresse divergiert die bis End of Time gilt dann wurde ein Umzug angegeben
		Optional<Adresse> maybeUmzugadresse = adresseService.getNewestWohnadresse(persistedPerson.getId());
		maybeUmzugadresse.filter(umzugAdresse -> !currentWohnadr.equals(umzugAdresse))
			.ifPresent(umzugAdr -> jaxPerson.setUmzugAdresse(adresseToJAX(umzugAdr)));
		// Finanzielle Situation
		Optional<FinanzielleSituationContainer> finanzielleSituationForGesuchsteller = finanzielleSituationService.findFinanzielleSituationForGesuchsteller(persistedPerson);
		if (finanzielleSituationForGesuchsteller.isPresent()) {
			JaxFinanzielleSituationContainer jaxFinanzielleSituationContainer = finanzielleSituationContainerToJAX(finanzielleSituationForGesuchsteller.get());
			jaxPerson.setFinanzielleSituationContainer(jaxFinanzielleSituationContainer);
		}
		return jaxPerson;
	}

	public Familiensituation familiensituationToEntity(@Nonnull JaxFamilienSituation familiensituationJAXP, @Nonnull Familiensituation familiensituation) {
		Validate.notNull(familiensituation);
		Validate.notNull(familiensituationJAXP);
		convertAbstractFieldsToEntity(familiensituationJAXP, familiensituation);
		familiensituation.setFamilienstatus(familiensituationJAXP.getFamilienstatus());
		familiensituation.setGesuchstellerKardinalitaet(familiensituationJAXP.getGesuchstellerKardinalitaet());
		familiensituation.setBemerkungen(familiensituationJAXP.getBemerkungen());
		familiensituation.setGesuch(this.gesuchToEntity(familiensituationJAXP.getGesuch(), new Gesuch())); //todo imanol sollte Gesuch nicht aus der DB geholt werden?
		return familiensituation;
	}

	public JaxFamilienSituation familiensituationToJAX(@Nonnull Familiensituation persistedFamiliensituation) {
		JaxFamilienSituation jaxFamiliensituation = new JaxFamilienSituation();
		convertAbstractFieldsToJAX(persistedFamiliensituation, jaxFamiliensituation);
		jaxFamiliensituation.setFamilienstatus(persistedFamiliensituation.getFamilienstatus());
		jaxFamiliensituation.setGesuchstellerKardinalitaet(persistedFamiliensituation.getGesuchstellerKardinalitaet());
		jaxFamiliensituation.setBemerkungen(persistedFamiliensituation.getBemerkungen());
		jaxFamiliensituation.setGesuch(this.gesuchToJAX(persistedFamiliensituation.getGesuch()));
		return jaxFamiliensituation;
	}

	public Fall fallToEntity(@Nonnull JaxFall fallJAXP, @Nonnull Fall fall) {
		Validate.notNull(fall);
		Validate.notNull(fallJAXP);
		convertAbstractFieldsToEntity(fallJAXP, fall);
		return fall;
	}

	public JaxFall fallToJAX(@Nonnull Fall persistedFall) {
		JaxFall jaxFall = new JaxFall();
		convertAbstractFieldsToJAX(persistedFall, jaxFall);
		return jaxFall;
	}

	public Gesuch gesuchToEntity(@Nonnull JaxGesuch gesuchJAXP, @Nonnull Gesuch gesuch) {
		Validate.notNull(gesuch);
		Validate.notNull(gesuchJAXP);
		convertAbstractFieldsToEntity(gesuchJAXP, gesuch);

		Optional<Fall> fallFromDB =  fallService.findFall(gesuchJAXP.getFall().getId());
		if (fallFromDB.isPresent()) {
			gesuch.setFall(this.fallToEntity(gesuchJAXP.getFall(), fallFromDB.get()));
		} else {
			throw new EbeguEntityNotFoundException("gesuchToEntity", ErrorCodeEnum.ERROR_ENTITY_NOT_FOUND, gesuchJAXP.getFall());
		}
		if (gesuchJAXP.getGesuchsteller1() != null && gesuchJAXP.getGesuchsteller1().getId() != null) {
			Optional<Person> gesuchsteller1 = personService.findPerson(gesuchJAXP.getGesuchsteller1().getId());
			if (gesuchsteller1.isPresent()) {
				gesuch.setGesuchsteller1(personToEntity(gesuchJAXP.getGesuchsteller1(), gesuchsteller1.get()));
			} else {
				throw new EbeguEntityNotFoundException("gesuchToEntity", ErrorCodeEnum.ERROR_ENTITY_NOT_FOUND, gesuchJAXP.getGesuchsteller1());
			}
		}
		if (gesuchJAXP.getGesuchsteller2() != null && gesuchJAXP.getGesuchsteller2().getId() != null) {
			Optional<Person> gesuchsteller2 = personService.findPerson(gesuchJAXP.getGesuchsteller2().getId());
			if (gesuchsteller2.isPresent()){
				gesuch.setGesuchsteller2(personToEntity(gesuchJAXP.getGesuchsteller2(), gesuchsteller2.get()));
			} else {
				throw new EbeguEntityNotFoundException("gesuchToEntity", ErrorCodeEnum.ERROR_ENTITY_NOT_FOUND, gesuchJAXP.getGesuchsteller2().getId());
			}
		}
		return gesuch;
	}

	public JaxGesuch gesuchToJAX(@Nonnull Gesuch persistedGesuch) {
		JaxGesuch jaxGesuch = new JaxGesuch();
		convertAbstractFieldsToJAX(persistedGesuch, jaxGesuch);
		jaxGesuch.setFall(this.fallToJAX(persistedGesuch.getFall()));
		if(persistedGesuch.getGesuchsteller1() != null) {
			jaxGesuch.setGesuchsteller1(this.personToJAX(persistedGesuch.getGesuchsteller1()));
		}
		if(persistedGesuch.getGesuchsteller2() != null) {
			jaxGesuch.setGesuchsteller2(this.personToJAX(persistedGesuch.getGesuchsteller2()));
		}
		return jaxGesuch;
	}

	public Fachstelle fachstelleToEntity(JaxFachstelle fachstelleJAXP, Fachstelle fachstelle) {
		Validate.notNull(fachstelleJAXP);
		Validate.notNull(fachstelle);
		convertAbstractFieldsToEntity(fachstelleJAXP, fachstelle);
		fachstelle.setName(fachstelleJAXP.getName());
		fachstelle.setBeschreibung(fachstelleJAXP.getBeschreibung());
		fachstelle.setBehinderungsbestaetigung(fachstelleJAXP.isBehinderungsbestaetigung());
		return fachstelle;
	}

	public JaxFachstelle fachstelleToJAX(@Nonnull Fachstelle persistedFachstelle) {
		JaxFachstelle jaxFachstelle = new JaxFachstelle();
		convertAbstractFieldsToJAX(persistedFachstelle, jaxFachstelle);
		jaxFachstelle.setName(persistedFachstelle.getName());
		jaxFachstelle.setBeschreibung(persistedFachstelle.getBeschreibung());
		jaxFachstelle.setBehinderungsbestaetigung(persistedFachstelle.isBehinderungsbestaetigung());
		return jaxFachstelle;
	}

	public FinanzielleSituationContainer finanzielleSituationContainerToEntity(@Nonnull JaxFinanzielleSituationContainer containerJAX,
																			   @Nonnull FinanzielleSituationContainer container) {
		Validate.notNull(container);
		Validate.notNull(containerJAX);
		convertAbstractFieldsToEntity(containerJAX, container);
		container.setJahr(containerJAX.getJahr());
		if (containerJAX.getFinanzielleSituationGS() != null) {
			container.setFinanzielleSituationGS(finanzielleSituationToEntity(containerJAX.getFinanzielleSituationGS(), new FinanzielleSituation()));
		}
		if (containerJAX.getFinanzielleSituationJA() != null) {
			container.setFinanzielleSituationJA(finanzielleSituationToEntity(containerJAX.getFinanzielleSituationJA(), new FinanzielleSituation()));
		}
		if (containerJAX.getFinanzielleSituationSV() != null) {
			container.setFinanzielleSituationSV(finanzielleSituationToEntity(containerJAX.getFinanzielleSituationSV(), new FinanzielleSituation()));
		}
		return container;
	}

	public JaxFinanzielleSituationContainer finanzielleSituationContainerToJAX(FinanzielleSituationContainer persistedFinanzielleSituation) {
		JaxFinanzielleSituationContainer jaxPerson = new JaxFinanzielleSituationContainer();
		convertAbstractFieldsToJAX(persistedFinanzielleSituation, jaxPerson);
		jaxPerson.setJahr(persistedFinanzielleSituation.getJahr());
		jaxPerson.setFinanzielleSituationGS(finanzielleSituationToJAX(persistedFinanzielleSituation.getFinanzielleSituationGS()));
		jaxPerson.setFinanzielleSituationJA(finanzielleSituationToJAX(persistedFinanzielleSituation.getFinanzielleSituationJA()));
		jaxPerson.setFinanzielleSituationSV(finanzielleSituationToJAX(persistedFinanzielleSituation.getFinanzielleSituationSV()));
		return jaxPerson;
	}

	public FinanzielleSituation finanzielleSituationToEntity(@Nonnull JaxFinanzielleSituation finanzielleSituationJAXP, @Nonnull FinanzielleSituation finanzielleSituation) {
		Validate.notNull(finanzielleSituation);
		Validate.notNull(finanzielleSituationJAXP);
		convertAbstractFieldsToEntity(finanzielleSituationJAXP, finanzielleSituation);
		finanzielleSituation.setSteuerveranlagungErhalten(finanzielleSituationJAXP.getSteuerveranlagungErhalten());
		finanzielleSituation.setSteuererklaerungAusgefuellt(finanzielleSituationJAXP.getSteuererklaerungAusgefuellt());
		finanzielleSituation.setNettolohn(finanzielleSituationJAXP.getNettolohn());
		finanzielleSituation.setFamilienzulage(finanzielleSituationJAXP.getFamilienzulage());
		finanzielleSituation.setErsatzeinkommen(finanzielleSituationJAXP.getErsatzeinkommen());
		finanzielleSituation.setErhalteneAlimente(finanzielleSituationJAXP.getErhalteneAlimente());
		finanzielleSituation.setBruttovermoegen(finanzielleSituationJAXP.getBruttovermoegen());
		finanzielleSituation.setSchulden(finanzielleSituationJAXP.getSchulden());
		finanzielleSituation.setSelbstaendig(finanzielleSituationJAXP.getSelbstaendig());
		finanzielleSituation.setGeschaeftsgewinnBasisjahrMinus2(finanzielleSituationJAXP.getGeschaeftsgewinnBasisjahrMinus2());
		finanzielleSituation.setGeschaeftsgewinnBasisjahrMinus1(finanzielleSituationJAXP.getGeschaeftsgewinnBasisjahrMinus1());
		finanzielleSituation.setGeschaeftsgewinnBasisjahr(finanzielleSituationJAXP.getGeschaeftsgewinnBasisjahr());
		finanzielleSituation.setGeleisteteAlimente(finanzielleSituationJAXP.getGeleisteteAlimente());
		return finanzielleSituation;
	}

	private JaxFinanzielleSituation finanzielleSituationToJAX(@Nullable FinanzielleSituation persistedFinanzielleSituation) {
		if (persistedFinanzielleSituation != null) {
			JaxFinanzielleSituation jaxPerson = new JaxFinanzielleSituation();
			convertAbstractFieldsToJAX(persistedFinanzielleSituation, jaxPerson);
			jaxPerson.setSteuerveranlagungErhalten(persistedFinanzielleSituation.getSteuerveranlagungErhalten());
			jaxPerson.setSteuererklaerungAusgefuellt(persistedFinanzielleSituation.getSteuererklaerungAusgefuellt());
			jaxPerson.setNettolohn(persistedFinanzielleSituation.getNettolohn());
			jaxPerson.setFamilienzulage(persistedFinanzielleSituation.getFamilienzulage());
			jaxPerson.setErsatzeinkommen(persistedFinanzielleSituation.getErsatzeinkommen());
			jaxPerson.setErhalteneAlimente(persistedFinanzielleSituation.getErhalteneAlimente());
			jaxPerson.setBruttovermoegen(persistedFinanzielleSituation.getBruttovermoegen());
			jaxPerson.setSchulden(persistedFinanzielleSituation.getSchulden());
			jaxPerson.setSelbstaendig(persistedFinanzielleSituation.getSelbstaendig());
			jaxPerson.setGeschaeftsgewinnBasisjahrMinus2(persistedFinanzielleSituation.getGeschaeftsgewinnBasisjahrMinus2());
			jaxPerson.setGeschaeftsgewinnBasisjahrMinus1(persistedFinanzielleSituation.getGeschaeftsgewinnBasisjahrMinus1());
			jaxPerson.setGeschaeftsgewinnBasisjahr(persistedFinanzielleSituation.getGeschaeftsgewinnBasisjahr());
			jaxPerson.setGeleisteteAlimente(persistedFinanzielleSituation.getGeleisteteAlimente());
			return jaxPerson;
		}
		return null;
	}
}
