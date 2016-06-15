/*
 * Copyright (c) 2014 DV Bern AG, Switzerland
 *
 * Das vorliegende Dokument, einschliesslich aller seiner Teile, ist urheberrechtlich
 * geschuetzt. Jede Verwertung ist ohne Zustimmung der DV Bern AG unzulaessig. Dies gilt
 * insbesondere fuer Vervielfaeltigungen, die Einspeicherung und Verarbeitung in
 * elektronischer Form. Wird das Dokument einem Kunden im Rahmen der Projektarbeit zur
 * Ansicht uebergeben ist jede weitere Verteilung durch den Kunden an Dritte untersagt.
 */

package ch.dvbern.ebegu.tests.services;

import ch.dvbern.ebegu.entities.EbeguParameter;
import ch.dvbern.ebegu.entities.Gesuchsperiode;
import ch.dvbern.ebegu.enums.EbeguParameterKey;
import ch.dvbern.ebegu.enums.ErrorCodeEnum;
import ch.dvbern.ebegu.errors.EbeguEntityNotFoundException;
import ch.dvbern.ebegu.services.AbstractBaseService;
import ch.dvbern.ebegu.services.EbeguParameterService;
import ch.dvbern.ebegu.types.DateRange;

import javax.annotation.Nonnull;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static ch.dvbern.ebegu.enums.EbeguParameterKey.*;

/**
 * Dummyservice fuer Ebegu Parameters
 */
@Stateless
@Alternative
@Local(EbeguParameterService.class)
public class EbeguDummyParameterServiceBean extends AbstractBaseService implements EbeguParameterService {


	Map<EbeguParameterKey, EbeguParameter> dummyObjects;


	public EbeguDummyParameterServiceBean() {
		this.dummyObjects = new EnumMap<>(EbeguParameterKey.class);

		dummyObjects.put(PARAM_PENSUM_TAGI_MIN, new EbeguParameter(PARAM_PENSUM_TAGI_MIN, "60"));
		dummyObjects.put(PARAM_PENSUM_KITA_MIN, new EbeguParameter(PARAM_PENSUM_KITA_MIN, "10"));
		dummyObjects.put(PARAM_PENSUM_TAGESELTERN_MIN, new EbeguParameter(PARAM_PENSUM_TAGESELTERN_MIN, "20"));
		dummyObjects.put(PARAM_PENSUM_TAGESSCHULE_MIN, new EbeguParameter(PARAM_PENSUM_TAGESSCHULE_MIN, "0"));

	}

	@Override
	@Nonnull
	public EbeguParameter saveEbeguParameter(@Nonnull EbeguParameter ebeguParameter) {
		Objects.requireNonNull(ebeguParameter);
		this.dummyObjects.put(ebeguParameter.getName(), ebeguParameter);
		return ebeguParameter;
	}

	@Override
	@Nonnull
	public Optional<EbeguParameter> findEbeguParameter(@Nonnull String id) {
		Objects.requireNonNull(id, "id muss gesetzt sein");
		return this.dummyObjects.values().stream().filter(ebeguParameter -> ebeguParameter.getId().equals(id)).findFirst();
	}

	@Override
	public void removeEbeguParameter(@Nonnull String id) {
		Objects.requireNonNull(id, "id muss gesetzt sein");
		this.dummyObjects.values().stream().filter(ebeguParameter -> ebeguParameter.getId().equals(id)).findFirst();
		Optional<EbeguParameter> parameterToRemove = findEbeguParameter(id);
		EbeguParameter param = parameterToRemove.orElseThrow(() -> new EbeguEntityNotFoundException("removeEbeguParameter", ErrorCodeEnum.ERROR_ENTITY_NOT_FOUND, id));
		this.dummyObjects.remove(param.getName());
	}

	@Override
	@Nonnull
	public Collection<EbeguParameter> getAllEbeguParameter() {
		return dummyObjects.values();
	}

	@Nonnull
	@Override
	public Collection<EbeguParameter> getAllEbeguParameterByDate(@Nonnull LocalDate date) {
		return dummyObjects.values();
	}

	@Override
	@Nonnull
	public Collection<EbeguParameter> getEbeguParameterByGesuchsperiode(@Nonnull Gesuchsperiode gesuchsperiode) {
		return dummyObjects.values();
	}

	@Override
	@Nonnull
	public Collection<EbeguParameter> getEbeguParameterByJahr(@Nonnull Integer jahr) {

		return dummyObjects.values();
	}

	//wird von validator gebraucht
	@Override
	@Nonnull
	public Optional<EbeguParameter> getEbeguParameterByKeyAndDate(@Nonnull EbeguParameterKey key, @Nonnull LocalDate date) {
		EbeguParameter mockParameter = this.dummyObjects.get(key);
		if (mockParameter != null) {
			return Optional.of(mockParameter);
		}
		return Optional.empty();
	}

	private void createEbeguParameterListForGesuchsperiode(@Nonnull Gesuchsperiode gesuchsperiode) {
		// Die Parameter des letzten Jahres suchen (datumAb -1 Tag)
		Collection<EbeguParameter> paramsOfGesuchsperiode = getAllEbeguParameterByDate(gesuchsperiode.getGueltigkeit().getGueltigAb().minusDays(1));
		paramsOfGesuchsperiode.stream().filter(lastYearParameter -> lastYearParameter.getName().isProGesuchsperiode()).forEach(lastYearParameter -> {
			EbeguParameter newParameter = lastYearParameter.copy(gesuchsperiode.getGueltigkeit());
			saveEbeguParameter(newParameter);
		});
	}

	/**
	 * searches all parameters that were valid at the first of january of the jahr-1. Then go through those parameters and if
	 * the parameter is set "per Gesuchsperiode" then copy it from the previous year and set the daterange for the current year
	 *
	 * @param jahr
	 */
	private void createEbeguParameterListForJahr(@Nonnull Integer jahr) {
		Collection<EbeguParameter> paramsOfYear = getAllEbeguParameterByDate(LocalDate.of(jahr - 1, Month.JANUARY, 1));
		paramsOfYear.stream().filter(lastYearParameter -> !lastYearParameter.getName().isProGesuchsperiode()).forEach(lastYearParameter -> {
			EbeguParameter newParameter = lastYearParameter.copy(new DateRange(jahr));
			saveEbeguParameter(newParameter);
		});
	}
}
