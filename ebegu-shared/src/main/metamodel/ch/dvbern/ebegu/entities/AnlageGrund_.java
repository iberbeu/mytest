package ch.dvbern.ebegu.entities;

import ch.dvbern.ebegu.enums.DokumentGrundTyp;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DokumentGrund.class)
public abstract class AnlageGrund_ extends ch.dvbern.ebegu.entities.AbstractEntity_ {

	public static volatile SingularAttribute<DokumentGrund, String> tag1;
	public static volatile SingularAttribute<DokumentGrund, DokumentGrundTyp> anlageGrundTyp;
	public static volatile SetAttribute<DokumentGrund, Dokument> anlageDokumente;
	public static volatile SingularAttribute<DokumentGrund, Gesuch> gesuch;
	public static volatile SingularAttribute<DokumentGrund, String> tag2;

}
