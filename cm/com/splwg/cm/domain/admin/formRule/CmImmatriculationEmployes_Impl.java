package com.splwg.cm.domain.admin.formRule;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ibm.icu.math.BigDecimal;
import com.splwg.base.api.QueryIterator;
import com.splwg.base.api.businessObject.BusinessObjectDispatcher;
import com.splwg.base.api.businessObject.BusinessObjectInstance;
import com.splwg.base.api.businessObject.COTSInstanceListNode;
import com.splwg.base.api.businessObject.COTSInstanceNode;
import com.splwg.base.api.businessService.BusinessServiceDispatcher;
import com.splwg.base.api.businessService.BusinessServiceInstance;
import com.splwg.base.api.datatypes.Bool;
import com.splwg.base.api.datatypes.Date;
import com.splwg.base.api.datatypes.Money;
import com.splwg.base.api.sql.PreparedStatement;
import com.splwg.base.api.sql.SQLResultRow;
import com.splwg.base.domain.common.businessObject.BusinessObject_Id;
import com.splwg.base.domain.common.extendedLookupValue.ExtendedLookupValue;
import com.splwg.base.domain.common.installation.InstallationHelper;
import com.splwg.base.domain.security.accessGroup.AccessGroup;
import com.splwg.base.domain.todo.role.Role;
import com.splwg.base.domain.todo.role.Role_Id;
import com.splwg.cm.domain.common.businessComponent.CmAccountRegistrationComponent;
import com.splwg.cm.domain.common.businessComponent.CmPersonSearchComponent;
import com.splwg.cm.domain.common.businessComponent.CmTaxRoleRegistrationComponent;
import com.splwg.shared.logging.Logger;
import com.splwg.shared.logging.LoggerFactory;
import com.splwg.tax.api.lookup.DeliverableLookup;
import com.splwg.tax.api.lookup.NameTypeLookup;
import com.splwg.tax.domain.admin.accountRelationshipType.AccountRelationshipType;
import com.splwg.tax.domain.admin.customerClass.CustomerClass;
import com.splwg.tax.domain.admin.formRule.ApplyFormRuleAlgorithmInputData;
import com.splwg.tax.domain.admin.formRule.ApplyFormRuleAlgorithmInputOutputData;
import com.splwg.tax.domain.admin.formRule.FormRule;
import com.splwg.tax.domain.admin.formRule.FormRuleBORuleProcessingAlgorithmSpot;
import com.splwg.tax.domain.admin.idType.IdType;
import com.splwg.tax.domain.admin.idType.IdType_Id;
import com.splwg.tax.domain.admin.personRelationshipType.PersonRelationshipType_Id;
import com.splwg.tax.domain.admin.personType.PersonType;
import com.splwg.tax.domain.admin.serviceType.ServiceType;
import com.splwg.tax.domain.customerinfo.person.Person;
import com.splwg.tax.domain.customerinfo.person.PersonName;
import com.splwg.tax.domain.customerinfo.person.PersonName_DTO;
import com.splwg.tax.domain.customerinfo.person.PersonName_Id;
import com.splwg.tax.domain.customerinfo.person.PersonPerson;
import com.splwg.tax.domain.customerinfo.person.PersonPerson_DTO;
import com.splwg.tax.domain.customerinfo.person.PersonPerson_Id;
import com.splwg.tax.domain.customerinfo.person.Person_Id;

/**
 * @author CISSYS
 *
 * @AlgorithmComponent ()
 */
public class CmImmatriculationEmployes_Impl extends CmImmatriculationEmployes_Gen
		implements FormRuleBORuleProcessingAlgorithmSpot {
	Logger logger = LoggerFactory.getLogger(CmImmatriculationEmployes_Impl.class);

	private ApplyFormRuleAlgorithmInputData applyFormRuleAlgorithmInputData;
	private ApplyFormRuleAlgorithmInputOutputData applyFormRuleAlgorithmInputOutputData;

	private Person getPersonById(String personId) {
		// log.info("*****Starting getpersonId");
		CmPersonSearchComponent perSearch = new CmPersonSearchComponent.Factory().newInstance();
		return perSearch.searchPerson(personId);
	}

	private Person getPersonByNinea(String IdType, String idNumber) {
		// log.info("*****Starting getpersonId");
		CmPersonSearchComponent perSearch = new CmPersonSearchComponent.Factory().newInstance();
		IdType_Id idType = new IdType_Id(IdType);
		// log.info("*****ID Type: " + idType.getTrimmedValue());
		return perSearch.searchPerson(idType.getEntity(), idNumber);
	}

	/**
	 * @param person1
	 * @param person2
	 * @return Date Cette methode permet de recuperer la date de debut du
	 *         contrat de l'employe
	 */
	private Date getDateDebutwithDatefinNull(Person_Id person1, Person_Id person2) {
		String query = "SELECT START_DT FROM CI_PER_PER WHERE PER_ID1=:perID1 AND PER_ID2=:perID2 AND END_DT IS NULL";
		PreparedStatement preparedStatement = createPreparedStatement(query);
		preparedStatement.bindId("perID1", person1);
		preparedStatement.bindId("perID2", person2);
		SQLResultRow sqlResultRow = preparedStatement.firstRow();

		if (sqlResultRow != null) {
			Date startDate = sqlResultRow.getDate("START_DT");
			System.out.println("RESULTAT SQL= " + startDate);
			return startDate;
		} else
			return null;

	}

	/**
	 * @param personId1
	 * @param personId2
	 * @param date
	 *            Cette methode permet de creer une relation entre deux
	 *            personnes
	 * 
	 */
	private void createRelationEmpl_Emp(String personId1, String personId2, Date date, String typeRel) {
		Person_Id person1 = new Person_Id(personId1);
		Person_Id person2 = new Person_Id(personId2);
		PersonRelationshipType_Id perRel = new PersonRelationshipType_Id(typeRel);
		PersonPerson_DTO perperDTO = (PersonPerson_DTO) createDTO(PersonPerson.class);
		PersonPerson_Id person = new PersonPerson_Id(perRel, person1, person2, date);
		perperDTO.setId(person);
		PersonPerson perperReg = perperDTO.newEntity();
	}

	/**
	 * @param personId
	 * @return List<PersonPerson> Cette methode permet de verifier si l'employe
	 *         est en relation avec un ou plusieurs employeurs.
	 */
	private List<PersonPerson> verifyRelationship(String personId) {
		List<PersonPerson> listePerPersonnes = new ArrayList<>();
		PersonPerson pp;
		PersonRelationshipType_Id personRelationshipType = null;
		String query = "SELECT * FROM CI_PER_PER WHERE PER_ID2=:perID2 AND PER_REL_TYPE_CD=:perRel AND END_DT IS NULL";
		Person_Id personId2 = new Person_Id(personId);
		personRelationshipType = new PersonRelationshipType_Id("EMPL-EMP");
		PreparedStatement preparedStatement = createPreparedStatement(query);
		preparedStatement.bindId("perID2", personId2);
		preparedStatement.bindId("perRel", personRelationshipType);
		// SQLResultRow sqlResultRow = preparedStatement.firstRow();

		QueryIterator<SQLResultRow> perResultIterator = preparedStatement.iterate();
		// int count=0;
		while (perResultIterator.hasNext()) {
			SQLResultRow result = (SQLResultRow) perResultIterator.next();
			Person_Id personId1 = new Person_Id(result.getString("PER_ID1"));
			personRelationshipType = new PersonRelationshipType_Id("EMPL-EMP");
			pp = new PersonPerson_Id(personRelationshipType, personId1, personId2, result.getDate("START_DT"))
					.getEntity();
			listePerPersonnes.add(pp);

		}
		return listePerPersonnes;
	}

	private void createToDo(String messageNumber, String oldEmp, String employe, String newEmp, String paramName) {
		startChanges();
		// BusinessService_Id businessServiceId=new
		// BusinessService_Id("F1-AddToDoEntry");
		BusinessServiceInstance businessServiceInstance = BusinessServiceInstance.create("F1-AddToDoEntry");
		Role_Id toDoRoleId = new Role_Id("CM-DMTTODO");
		Role toDoRole = toDoRoleId.getEntity();
		// businessServiceInstance.getFieldAndMDForPath("sendTo").setXMLValue("SNDR");
		// businessServiceInstance.getFieldAndMDForPath("subject").setXMLValue("Batch
		// Update from PSRM");
		businessServiceInstance.getFieldAndMDForPath("toDoType").setXMLValue("CM-DMTTO");
		businessServiceInstance.getFieldAndMDForPath("toDoRole").setXMLValue(toDoRole.getId().getTrimmedValue());
		businessServiceInstance.getFieldAndMDForPath("drillKey1").setXMLValue(paramName);
		businessServiceInstance.getFieldAndMDForPath("messageCategory").setXMLValue("90007");
		businessServiceInstance.getFieldAndMDForPath("messageNumber").setXMLValue(messageNumber);
		businessServiceInstance.getFieldAndMDForPath("messageParm1").setXMLValue(oldEmp);
		businessServiceInstance.getFieldAndMDForPath("messageParm2").setXMLValue(employe);
		businessServiceInstance.getFieldAndMDForPath("messageParm3").setXMLValue(newEmp);
		businessServiceInstance.getFieldAndMDForPath("sortKey1").setXMLValue(paramName);

		BusinessServiceDispatcher.execute(businessServiceInstance);
		saveChanges();
		// getSession().commit();
	}

	@Override
	public void invoke() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		// Form Data BO Instance

		BusinessObjectInstance formBoInstance = (BusinessObjectInstance) applyFormRuleAlgorithmInputOutputData
				.getFormBusinessObject();
		logger.info("Input Form BO: " + formBoInstance.getDocument().asXML());
		logger.info("formBoInstance: " + formBoInstance.getSchemaName());
		// Form Rule
		FormRule formRule = applyFormRuleAlgorithmInputData.getFormRuleId().getEntity();
		//
		// logger.info("Input FormRule: " + formRule);
		// logger.info("Input Name: " + formRule.entityName());
		// // Reading Form Rule Information
		BusinessObjectInstance formRuleBoInstance = BusinessObjectInstance.create(formRule.getBusinessObject());
		logger.info("formRuleBoInstance: " + formRuleBoInstance.getSchemaName());
		logger.info("formRuleBoInstance: " + formRuleBoInstance.getDocument().asXML());
		formRuleBoInstance.set("bo", formRule.getBusinessObject().getId().getTrimmedValue());
		formRuleBoInstance.set("formRuleGroup", formRule.getId().getFormRuleGroup().getId().getTrimmedValue());
		formRuleBoInstance.set("formRule", formRule.getId().getFormRule());
		formRuleBoInstance.set("sequence", BigDecimal.valueOf(formRule.getSequence().longValue()));
		formRuleBoInstance = BusinessObjectDispatcher.read(formRuleBoInstance);
		logger.info("After form rule BO");
		// Form Rule Details Group
		COTSInstanceNode ruleDetails = formRuleBoInstance.getGroup("ruleDetails");

		PersonType personType = ruleDetails.getEntity("personType", PersonType.class);
		//AccessGroup accessgroup=ruleDetails.getEntity("accessGroup", AccessGroup.class);

		// Retrieve the Related Transaction BO from Person Type
		BusinessObject_Id relatedTransactionBOId = personType.getRelatedTransactionBOId();

		// Transaction BO must exist in order to create the Person
		if (notNull(relatedTransactionBOId)) {
			logger.info("Inside IF");
			COTSInstanceNode group = formBoInstance.getGroupFromPath("employe");
			Iterator<COTSInstanceListNode> iterator = group.getList("employeList").iterator();
			BigDecimal nineaDec = (BigDecimal) formBoInstance.getFieldAndMDForPath("employeur/ninea/asCurrent")
					.getValue();
			String ninea = nineaDec.toString();
			String idEmployeur = getPersonByNinea("SCI", ninea).getId().getIdValue();
			while (iterator.hasNext()) {
				COTSInstanceListNode nextElt = iterator.next();
				if (nextElt != null) {
					String typeMouvement = (String) nextElt.getFieldAndMDForPath("typeMouvement/asCurrent").getValue();
					Date dateDebutContrat = null;
					BusinessObjectInstance personBoInstance = null;
					BusinessObjectInstance addresBoInstance = null;
					BusinessObjectInstance histoBoInstance = null;
					COTSInstanceListNode personAddressInstance = null;
					String idTravailleur = (String) nextElt.getFieldAndMDForPath("rechercheEmploye/asCurrent")
							.getValue();
					logger.info("before if");
					if (typeMouvement.equals("EMBAUCHE")) { // TYPE_MOUVEMENT=Embauche
						if (idTravailleur != null && getPersonById(idTravailleur) != null) { // idTravaileur
																								// non
																								// null
																								// et
																								// la
																								// personne
																								// exite
																								// en
																								// base

							// RELATION ENTRE ET L'EMPLOYE
							logger.info("before relation");
							List<PersonPerson> personRel = verifyRelationship(idTravailleur);
							if (personRel == null) { // la relation n'existe pas
														// on la cree
								logger.info("inside if" + personRel.size());
								dateDebutContrat = (Date) nextElt.getFieldAndMDForPath("dateDebut/asCurrent")
										.getValue();
								logger.info("before emp relation: ");
								createRelationEmpl_Emp(idEmployeur, idTravailleur, dateDebutContrat, "EMPL-EMP");
								createRelationEmpl_Emp(idTravailleur, idEmployeur, dateDebutContrat, "EMPL-EMP");

							} else { // la relation existe, on la ferme et on //
										// cree une autre.

								// Recherche des relations existantes

								// fermeture de la relation
								Date dateFinContrat = (Date) nextElt.getFieldAndMDForPath("dateFinContrat/asCurrent")
										.getValue();
								dateDebutContrat = (Date) nextElt.getFieldAndMDForPath("dateDebut/asCurrent")
										.getValue();
								boolean ok = false;
								for (PersonPerson pp : personRel) {
									if (pp.getId().getPersonId1Id().getIdValue().equals(idEmployeur)) {
										ok = true;
									}
									personBoInstance = BusinessObjectInstance
											.create(relatedTransactionBOId.getEntity());
									COTSInstanceListNode personPersonInstance = personBoInstance.getList("personPerson")
											.newChild();
									personPersonInstance.set("personId1", pp.getId().getPersonId1Id().getIdValue());
									personPersonInstance.set("personId2", pp.getId().getPersonId2Id().getIdValue());
									personPersonInstance.set("personRelationshipType",
											pp.getId().getPersonRelationshipTypeId().getIdValue());
									personPersonInstance.set("startDate", pp.getId().getStartDate());
									personPersonInstance.set("endDate", dateFinContrat);
									personBoInstance.set("personId", pp.getId().getPersonId1Id().getIdValue());
									BusinessObjectDispatcher.update(personBoInstance);
									personPersonInstance.set("personId1", pp.getId().getPersonId2Id().getIdValue());
									personPersonInstance.set("personId2", pp.getId().getPersonId1Id().getIdValue());
									personPersonInstance.set("personRelationshipType",
											pp.getId().getPersonRelationshipTypeId().getIdValue());
									personPersonInstance.set("startDate", pp.getId().getStartDate());
									personPersonInstance.set("endDate", dateFinContrat);
									personBoInstance.set("personId", pp.getId().getPersonId2Id().getIdValue());
									BusinessObjectDispatcher.update(personBoInstance);

								}
								logger.info("idEmployeur1=: " + idEmployeur);
								logger.info("valeur ok=: " + ok);
								if (ok == true) {
									// Informations contractuelles

									String prenomEmployeeDmt = (String) nextElt
											.getFieldAndMDForPath("profession/asCurrent").getValue();
									String nomEmployeeDmt = (String) nextElt
											.getFieldAndMDForPath("profession/asCurrent").getValue();
									String profession = (String) nextElt.getFieldAndMDForPath("profession/asCurrent")
											.getValue();
									String emploi = (String) nextElt.getFieldAndMDForPath("emploi/asCurrent")
											.getValue();
									String conventionApplicable = (String) nextElt
											.getFieldAndMDForPath("conventionApplicable/asCurrent").getValue();
									String categorie = (String) nextElt.getFieldAndMDForPath("categorie/asCurrent")
											.getValue();

									Money salaireContractuel = (Money) nextElt
											.getFieldAndMDForPath("salaireContractuel/asCurrent").getValue();

									String tempsDeTravail = (String) nextElt
											.getFieldAndMDForPath("tpsDeTravail/asCurrent").getValue();

									Bool employeCadre = (Bool) nextElt.getFieldAndMDForPath("employeCadre/asCurrent")
											.getValue();
									String employeCadreTostr = employeCadre.toString();

									histoBoInstance = BusinessObjectInstance.create("CMDMT_HistoriqueBO");
									histoBoInstance.set("cmTypeMouvement", typeMouvement);
									histoBoInstance.set("cmIdTravailleur", personBoInstance.getString("personId"));
									// histoBoInstance.set("cmNIN", cni);
									histoBoInstance.set("cmNom", nomEmployeeDmt);
									histoBoInstance.set("cmPrenom", prenomEmployeeDmt);
									histoBoInstance.set("cmProfession", profession);
									histoBoInstance.set("cmEmploi", emploi);
									histoBoInstance.set("cmConventionCollective", conventionApplicable);
									histoBoInstance.set("cmCategorie", categorie);
									histoBoInstance.set("cmNatureContrat", "CDI");
									histoBoInstance.set("cmDateDebutContrat", dateDebutContrat);
									histoBoInstance.set("cmSalaireContractuel", salaireContractuel);
									histoBoInstance.set("cmTpsDeTravail", tempsDeTravail);
									histoBoInstance.set("cmEmployeCadre", employeCadreTostr);
									histoBoInstance.set("cmIdEmployeur", idEmployeur);
									histoBoInstance = BusinessObjectDispatcher.add(histoBoInstance);
								} else {
									String raisonSociale = (String) formBoInstance
											.getFieldAndMDForPath("employeur/raisonSociale/asCurrent").getValue();
									String nomEmployeeDmt = (String) nextElt
											.getFieldAndMDForPath("nomEmploye/asCurrent").getValue();
									String prenomEmployeeDmt = (String) nextElt
											.getFieldAndMDForPath("prenomEmploye/asCurrent").getValue();
									String nomComplet = prenomEmployeeDmt + " " + nomEmployeeDmt;
									String idOldEmployeur = personRel.get(0).getId().getPersonId1Id().getIdValue();
									logger.info("idOldEmployeur " + idOldEmployeur);
									Person person = new Person_Id(idOldEmployeur).getEntity();
									logger.info("personxxx " + person);
									PersonName personName = null;
									personName = new PersonName_Id(person, BigInteger.valueOf(1)).getEntity();
									if (personName == null) {
										personName = new PersonName_Id(person, BigInteger.valueOf(2)).getEntity();
									}
									logger.info("personNamexxx " + personName);
									PersonName_DTO personNameDTO = personName.getDTO();
									String nomOldEmp = personNameDTO.getEntityName();

									// create To Do
									createToDo("323", nomOldEmp, nomComplet, raisonSociale, idTravailleur);
									// Creation de la relation
									createRelationEmpl_Emp(idTravailleur, idEmployeur, dateDebutContrat, "EMPL-EMP");
									createRelationEmpl_Emp(idEmployeur, idTravailleur, dateDebutContrat, "EMPL-EMP");
									logger.info("After emp relation");

									logger.info("After Todo");

								}

							}
						} else { // id null ou la personne n'existe pas

							personBoInstance = BusinessObjectInstance.create(relatedTransactionBOId.getEntity());
							// Set Person Main Information
							personBoInstance.set("personType", personType);
							IdType idType = null;

							// IDENTITE DE L'EMPLOYE
							String nomEmployeeDmt = (String) nextElt.getFieldAndMDForPath("nomEmploye/asCurrent")
									.getValue();
							String prenomEmployeeDmt = (String) nextElt.getFieldAndMDForPath("prenomEmploye/asCurrent")
									.getValue();
							String sexe = (String) nextElt.getFieldAndMDForPath("sexe/asCurrent").getValue();
							String etatCivil = (String) nextElt.getFieldAndMDForPath("etatCivil/asCurrent").getValue();
							Date dateNaissance = (Date) nextElt.getFieldAndMDForPath("dateDeNaissance/asCurrent")
									.getValue();

							// INFO PERE
							String nomPere = (String) nextElt.getFieldAndMDForPath("nomPere/asCurrent").getValue();
							String prenomPere = (String) nextElt.getFieldAndMDForPath("prenomPere/asCurrent")
									.getValue();

							// INFO Mere
							String nomMere = (String) nextElt.getFieldAndMDForPath("nomMere/asCurrent").getValue();
							String prenomMere = (String) nextElt.getFieldAndMDForPath("prenomMere/asCurrent")
									.getValue();
							String nationalite = (String) nextElt.getFieldAndMDForPath("nationalite/asCurrent")
									.getValue();

							dateDebutContrat = (Date) nextElt.getFieldAndMDForPath("dateDebut/asCurrent").getValue();

							Date dateLivraison = (Date) nextElt.getFieldAndMDForPath("delivreLe/asCurrent").getValue();

							String lieuDelivrance = (String) nextElt.getFieldAndMDForPath("lieuDelivrance/asCurrent")
									.getValue();
							Date dateExpiration = (Date) nextElt.getFieldAndMDForPath("expireLe/asCurrent").getValue();

							BigDecimal numeroRegNaissance = (BigDecimal) nextElt
									.getFieldAndMDForPath("numRegNaiss/asCurrent").getValue();

							// Set Primary Person Name pour l'employe
							COTSInstanceListNode personCharInstance = null;
							COTSInstanceListNode personNameInstance = personBoInstance.getList("personName").newChild();
							personNameInstance.set("nameType", NameTypeLookup.constants.PRIMARY);
							personNameInstance.set("isPrimaryName", Bool.TRUE);
							if (notNull(prenomEmployeeDmt) && notNull(nomEmployeeDmt)) {
								personNameInstance.set("firstName", prenomEmployeeDmt);
								personNameInstance.set("lastName", nomEmployeeDmt);
							}

							if (sexe != null) {
								personCharInstance = personBoInstance.getList("personChar").newChild(); 
								personCharInstance.set("charTypeCD", "SEX");
								personCharInstance.set("charVal", sexe);
								personCharInstance.set("searchCharVal", sexe);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());
							}

							if (etatCivil != null) {
								personCharInstance = personBoInstance.getList("personChar").newChild();
								personCharInstance.set("charTypeCD", "ETATCIV");
								personCharInstance.set("adhocCharVal", etatCivil);
								personCharInstance.set("searchCharVal", etatCivil);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());
							}

							if (dateNaissance != null) {
								String dateNaiss = dateNaissance.toString();
								personCharInstance = personBoInstance.getList("personChar").newChild();
								personCharInstance.set("charTypeCD", "CM-DOB");
								personCharInstance.set("adhocCharVal", dateNaiss);
								personCharInstance.set("searchCharVal", dateNaiss);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());
							}

							if (dateLivraison != null) {
								String dateLivraisonTostr = dateLivraison.toString();
								personCharInstance = personBoInstance.getList("personChar").newChild();
								personCharInstance.set("charTypeCD", "CM-ISSDT");
								personCharInstance.set("adhocCharVal", dateLivraisonTostr);
								personCharInstance.set("searchCharVal", dateLivraisonTostr);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());
							}

							if (lieuDelivrance != null) {
								personCharInstance = personBoInstance.getList("personChar").newChild();
								personCharInstance.set("charTypeCD", "LIEULIV");
								personCharInstance.set("adhocCharVal", lieuDelivrance);
								personCharInstance.set("searchCharVal", lieuDelivrance);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());
							}

							if (dateExpiration != null) {
								String dateExpirationTostr = dateExpiration.toString();
								personCharInstance = personBoInstance.getList("personChar").newChild();
								personCharInstance.set("charTypeCD", "CM-EXPDT");
								personCharInstance.set("adhocCharVal", dateExpirationTostr);
								personCharInstance.set("searchCharVal", dateExpirationTostr);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());
							}

							if (nationalite != null) {
								personCharInstance = personBoInstance.getList("personChar").newChild();
								personCharInstance.set("charTypeCD", "NATION");
								personCharInstance.set("adhocCharVal", nationalite);
								personCharInstance.set("searchCharVal", nationalite);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());
							}

							if (numeroRegNaissance != null) {
								String numeroRegNaissanceToStr = numeroRegNaissance.toString();
								personCharInstance = personBoInstance.getList("personChar").newChild();
								personCharInstance.set("charTypeCD", "NUMNAIS");
								personCharInstance.set("adhocCharVal", numeroRegNaissanceToStr);
								personCharInstance.set("searchCharVal", numeroRegNaissanceToStr);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());
							}

							String numIdentification = null;

							String typePieceId = (String) nextElt.getFieldAndMDForPath("typePieceIdentite/asCurrent")
									.getValue();
							if (typePieceId.equals("NIN")) {
								BigDecimal cni = (BigDecimal) nextElt.getFieldAndMDForPath("nin/asCurrent").getValue();
								numIdentification = cni.toString();
							} else if (typePieceId.equals("CDAO")) {
								BigDecimal cedeao	 = (BigDecimal) nextElt.getFieldAndMDForPath("ninCEDEAO/asCurrent")
										.getValue();
								 numIdentification=cedeao.toString();
							} else{ // CONC ou PASS
								numIdentification = (String) nextElt.getFieldAndMDForPath("numPieceIdentite/asCurrent")
										.getValue();
							}

							idType = new IdType_Id(typePieceId).getEntity();

							if (notNull(idType) && !isBlankOrNull(numIdentification)) {
								COTSInstanceListNode personIdInstance = personBoInstance.getList("personIds")
										.newChild();
								personIdInstance.set("isPrimaryId", Bool.TRUE);
								personIdInstance.set("idType", idType);
								personIdInstance.set("personIdNumber", numIdentification);
							}

							// CREATION DU PERE

							if (notNull(prenomPere) && notNull(nomPere)) {
								personCharInstance = personBoInstance.getList("personChar").newChild();
								personCharInstance.set("charTypeCD", "PRE_PERE");
								personCharInstance.set("adhocCharVal", prenomPere);
								personCharInstance.set("searchCharVal", prenomPere);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());

								personCharInstance = personBoInstance.getList("personChar").newChild();
								personCharInstance.set("charTypeCD", "NOM_PERE");
								personCharInstance.set("adhocCharVal", nomPere);
								personCharInstance.set("searchCharVal", nomPere);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());

							}

							// CREATION DE LA MERE
							if (notNull(prenomMere) && notNull(nomMere)) {
								personCharInstance = personBoInstance.getList("personChar").newChild();
								personCharInstance.set("charTypeCD", "PRE_MERE");
								personCharInstance.set("adhocCharVal", prenomMere);
								personCharInstance.set("searchCharVal", prenomMere);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());

								personCharInstance = personBoInstance.getList("personChar").newChild();
								personCharInstance.set("charTypeCD", "NOM_MERE");
								personCharInstance.set("adhocCharVal", nomMere);
								personCharInstance.set("searchCharVal", nomMere);
								personCharInstance.set("effectiveDate", getSystemDateTime().getDate());

							}

							// ADRESSES DE NAISSANCE L'EMPLOYE

							ExtendedLookupValue addressType = ruleDetails.getExtendedLookupId("addressType")
									.getEntity();
							//
							String paysDenaissance = (String) nextElt.getFieldAndMDForPath("paysDeNaissance/asCurrent")
									.getValue();
							String villeDeNaissance = (String) nextElt
									.getFieldAndMDForPath("villeDeNaissance/asCurrent").getValue();

							// ADRESSES DE RESIDENCE DE L'EMPLOYE

							String paysDeResidence = (String) nextElt.getFieldAndMDForPath("pays/asCurrent").getValue();
							String regionDeResidence = (String) nextElt.getFieldAndMDForPath("region/asCurrent")
									.getValue();
							String departementDeResidence = (String) nextElt
									.getFieldAndMDForPath("departement/asCurrent").getValue();
							String arrondissementResidence = (String) nextElt
									.getFieldAndMDForPath("arrondissement/asCurrent").getValue();
							String communeDeResidence = (String) nextElt.getFieldAndMDForPath("commune/asCurrent")
									.getValue();
							String quartierResidence = (String) nextElt.getFieldAndMDForPath("quartier/asCurrent")
									.getValue();

							String adresseResidence = (String) nextElt.getFieldAndMDForPath("adresse/asCurrent")
									.getValue();
							BigDecimal boitePostale = (BigDecimal) nextElt
									.getFieldAndMDForPath("boitePostale/asCurrent").getValue();
							addresBoInstance = BusinessObjectInstance.create("CM-AddressDUP");
							logger.info("Creating addresBoInstance create: " + addresBoInstance.getDocument().asXML());

							COTSInstanceListNode addresscharInstance = null;

							if (paysDenaissance != null) {
								addresscharInstance = addresBoInstance.getList("addressChar").newChild();
								addresscharInstance.set("charTypeCD", "PAYSNAIS");
								addresscharInstance.set("adhocCharVal", paysDenaissance);
								addresscharInstance.set("searchCharVal", paysDenaissance);
							}

							if (villeDeNaissance != null) {
								addresscharInstance = addresBoInstance.getList("addressChar").newChild();
								addresscharInstance.set("charTypeCD", "VILENAIS");
								addresscharInstance.set("adhocCharVal", villeDeNaissance);
								addresscharInstance.set("searchCharVal", villeDeNaissance);
							}

							addresBoInstance.set("bo", addresBoInstance.getBusinessObject());
							addresBoInstance.set("country", paysDeResidence);
							addresBoInstance.set("state", regionDeResidence);
							addresBoInstance.set("county", departementDeResidence);
							addresBoInstance.set("city", arrondissementResidence);
							addresBoInstance.set("address1", communeDeResidence);
							addresBoInstance.set("address2", quartierResidence);
							addresBoInstance.set("address3", adresseResidence);
							if (boitePostale != null) {
								String boitePostaleTostr = boitePostale.toString();
								addresBoInstance.set("postal", boitePostaleTostr);
							}
							addresBoInstance = BusinessObjectDispatcher.add(addresBoInstance);

							// Setting Address Information
							personAddressInstance = personBoInstance.getList("personAddress").newChild();
							personAddressInstance.set("addressId", addresBoInstance.getString("addressId"));
							personAddressInstance.set("addressType", addressType.getId());
							personAddressInstance.set("startDate", getSystemDateTime().getDate());
							personAddressInstance.set("deliverable", DeliverableLookup.constants.YES);

							// Invoke BO for Person Creation
							personBoInstance = BusinessObjectDispatcher.add(personBoInstance);
							nextElt.setXMLFieldStringFromPath("rechercheEmploye/asCurrent",
									personBoInstance.getString("personId"));

							// RELATION ENTRE L'EMPLOYEUR ET L'EMPLOYE
							createRelationEmpl_Emp(idEmployeur, personBoInstance.getString("personId"),
									dateDebutContrat, "EMPL-EMP");
							createRelationEmpl_Emp(personBoInstance.getString("personId"), idEmployeur,
									dateDebutContrat, "EMPL-EMP");
							
							CmAccountRegistrationComponent accountRegistrationHelper=null;

							// // #################################################################################
							// Set Account Information PF PH
							accountRegistrationHelper = CmAccountRegistrationComponent.Factory
									.newInstance();
							accountRegistrationHelper
									.setAccountType(ruleDetails.getEntity("accountTypePFPH", CustomerClass.class));
							accountRegistrationHelper
									.setAccessGroup(ruleDetails.getEntity("accessGroup", AccessGroup.class));
							accountRegistrationHelper.setAccountRelationshipType(
									ruleDetails.getEntity("accountRelationshipType", AccountRelationshipType.class));
							accountRegistrationHelper
									.setMainCustomerSwitch(ruleDetails.getBoolean("mainCustomerSwitch"));
							accountRegistrationHelper.setFinancialResponsibleSwitch(
									ruleDetails.getBoolean("financialResponsibleSwitch"));
							accountRegistrationHelper.setCanReceiveNotificationSwitch(
									ruleDetails.getBoolean("canReceiveNotificationSwitch"));
							accountRegistrationHelper.setCanReceiveCopyOfBillSwitch(
									ruleDetails.getBoolean("canReceiveCopyOfBillSwitch"));
							accountRegistrationHelper.setBillAddressSource(ruleDetails.getLookup("billAddressSource"));
							accountRegistrationHelper.setPersonIdString(personBoInstance.getString("personId"));
							//
							// // Creating Account PF PH
							BusinessObjectInstance accountInstance = accountRegistrationHelper.createAccount(); 
							//
							logger.info("Creating Account BO: " + accountInstance.getDocument().asXML());

							// Setting Tax Role information
//							CmTaxRoleRegistrationComponent taxRoleRegistrationHelper = CmTaxRoleRegistrationComponent.Factory
//									.newInstance();
//							taxRoleRegistrationHelper
//									.setServiceType(ruleDetails.getEntity("taxType", ServiceType.class));
//							taxRoleRegistrationHelper.setStartDate(getSystemDateTime().getDate());
//							taxRoleRegistrationHelper.setAccountIdString(accountInstance.getString("accountId"));
//							taxRoleRegistrationHelper
//									.setFormType(applyFormRuleAlgorithmInputData.getFormTypeId().getEntity());
//							//
//							// // Creating Tax Role
//							BusinessObjectInstance taxRoleBoInstance = taxRoleRegistrationHelper.createTaxRoleBO();
//
//							logger.info("Creating Tax Role BO: " + taxRoleBoInstance.getDocument().asXML());
							
							// #################################################################################
							// Set Account Main Information AT/MP PH
							accountRegistrationHelper = CmAccountRegistrationComponent.Factory
									.newInstance();
							accountRegistrationHelper
									.setAccountType(ruleDetails.getEntity("accountTypeATMPPH", CustomerClass.class));
							accountRegistrationHelper
									.setAccessGroup(ruleDetails.getEntity("accessGroup", AccessGroup.class));
							accountRegistrationHelper.setAccountRelationshipType(
									ruleDetails.getEntity("accountRelationshipType", AccountRelationshipType.class));
							accountRegistrationHelper
									.setMainCustomerSwitch(ruleDetails.getBoolean("mainCustomerSwitch"));
							accountRegistrationHelper.setFinancialResponsibleSwitch(
									ruleDetails.getBoolean("financialResponsibleSwitch"));
							accountRegistrationHelper.setCanReceiveNotificationSwitch(
									ruleDetails.getBoolean("canReceiveNotificationSwitch"));
							accountRegistrationHelper.setCanReceiveCopyOfBillSwitch(
									ruleDetails.getBoolean("canReceiveCopyOfBillSwitch"));
							accountRegistrationHelper.setBillAddressSource(ruleDetails.getLookup("billAddressSource"));
							accountRegistrationHelper.setPersonIdString(personBoInstance.getString("personId"));
							//
							// // Creating Account AT/MP PH
							BusinessObjectInstance accountInstance2 = accountRegistrationHelper.createAccount();
							//
							logger.info("Creating Account BO: " + accountInstance2.getDocument().asXML());
														
							// #################################################################################
							// Set Account Main Information Vieillesse
							accountRegistrationHelper = CmAccountRegistrationComponent.Factory
									.newInstance();
							accountRegistrationHelper
									.setAccountType(ruleDetails.getEntity("accountTypeVIEPH", CustomerClass.class));
							accountRegistrationHelper
									.setAccessGroup(ruleDetails.getEntity("accessGroup", AccessGroup.class));
							accountRegistrationHelper.setAccountRelationshipType(
									ruleDetails.getEntity("accountRelationshipType", AccountRelationshipType.class));
							accountRegistrationHelper
									.setMainCustomerSwitch(ruleDetails.getBoolean("mainCustomerSwitch"));
							accountRegistrationHelper.setFinancialResponsibleSwitch(
									ruleDetails.getBoolean("financialResponsibleSwitch"));
							accountRegistrationHelper.setCanReceiveNotificationSwitch(
									ruleDetails.getBoolean("canReceiveNotificationSwitch"));
							accountRegistrationHelper.setCanReceiveCopyOfBillSwitch(
									ruleDetails.getBoolean("canReceiveCopyOfBillSwitch"));
							accountRegistrationHelper.setBillAddressSource(ruleDetails.getLookup("billAddressSource"));
							accountRegistrationHelper.setPersonIdString(personBoInstance.getString("personId"));
							//
							// // Creating Account Vieillesse PH
							BusinessObjectInstance accountInstance3 = accountRegistrationHelper.createAccount();
							//
							logger.info("Creating Account BO: " + accountInstance3.getDocument().asXML());

							// ------------------------------Insertion des
							// donnees dans
							// la
							// table
							// CM_DMT_HISTORIQUE----------------------------------

							// -------------------------------Recuperation des
							// donnees-------------------------------------------------------------------

							// Informations contractuelles

							String profession = (String) nextElt.getFieldAndMDForPath("profession/asCurrent")
									.getValue();
							String emploi = (String) nextElt.getFieldAndMDForPath("emploi/asCurrent").getValue();
							String conventionApplicable = (String) nextElt
									.getFieldAndMDForPath("conventionApplicable/asCurrent").getValue();
							String categorie = (String) nextElt.getFieldAndMDForPath("categorie/asCurrent").getValue();
							String natureContrat = (String) nextElt.getFieldAndMDForPath("natureContrat/asCurrent")
									.getValue();

							Date dateFinContrat = (Date) nextElt.getFieldAndMDForPath("dateFinContrat/asCurrent")
									.getValue();

							Money salaireContractuel = (Money) nextElt
									.getFieldAndMDForPath("salaireContractuel/asCurrent").getValue();

							String tempsDeTravail = (String) nextElt.getFieldAndMDForPath("tpsDeTravail/asCurrent")
									.getValue();

							Bool employeCadreOui = (Bool) nextElt.getFieldAndMDForPath("ouiEmp/asCurrent").getValue();
							Bool employeCadreNon = (Bool) nextElt.getFieldAndMDForPath("nonEmp/asCurrent").getValue();
							String employeCadreTostr = null;

							if (employeCadreOui != null) {
								employeCadreTostr = employeCadreOui.toString();
							} else if (employeCadreNon != null) {
								employeCadreTostr = employeCadreNon.toString();
							}

							histoBoInstance = BusinessObjectInstance.create("CMDMT_HistoriqueBO"); 
							histoBoInstance.set("cmTypeMouvement", typeMouvement);
							histoBoInstance.set("cmIdTravailleur", personBoInstance.getString("personId"));
							histoBoInstance.set("cmTypePiece", typePieceId);
							histoBoInstance.set("cmNumero", numIdentification);
							histoBoInstance.set("cmNom", nomEmployeeDmt);
							histoBoInstance.set("cmPrenom", prenomEmployeeDmt);
							histoBoInstance.set("cmProfession", profession);
							if (emploi != null) {
								histoBoInstance.set("cmEmploi", emploi);
							}
							histoBoInstance.set("cmConventionCollective", conventionApplicable);
							histoBoInstance.set("cmCategorie", categorie);
							histoBoInstance.set("cmNatureContrat", natureContrat);
							histoBoInstance.set("cmDateDebutContrat", dateDebutContrat);
							if (!natureContrat.equals("CDI")) {
								histoBoInstance.set("cmDateFinContrat", dateFinContrat);
							}
							histoBoInstance.set("cmSalaireContractuel", salaireContractuel);
							histoBoInstance.set("cmTpsDeTravail", tempsDeTravail);
							histoBoInstance.set("cmEmployeCadre", employeCadreTostr);
							histoBoInstance.set("cmIdEmployeur", idEmployeur);
							histoBoInstance = BusinessObjectDispatcher.add(histoBoInstance);
							nextElt.setXMLFieldStringFromPath("rechercheEmploye/asCurrent",
									histoBoInstance.getString("cmIdTravailleur"));

						}
					} else if (typeMouvement.equals("LICENCIEMENT")) { // TYPE_MOUVEMENT=Licenciement
						personBoInstance = BusinessObjectInstance.create(relatedTransactionBOId.getEntity());
						histoBoInstance = BusinessObjectInstance.create("CMDMT_HistoriqueBO");
						logger.info("Entree dans ELSE: ");
						Date dateFinContrat = (Date) nextElt.getFieldAndMDForPath("dateFinContrat/asCurrent")
								.getValue();
						dateDebutContrat = (Date) nextElt.getFieldAndMDForPath("dateDebut/asCurrent").getValue();
						COTSInstanceListNode personPersonInstance = personBoInstance.getList("personPerson").newChild();
						Person_Id person1 = new Person_Id(idEmployeur);
						Person_Id person2 = new Person_Id(idTravailleur);
						Date dateDebutRelationship1 = getDateDebutwithDatefinNull(person1, person2);
						personPersonInstance.set("personId1", idEmployeur);
						personPersonInstance.set("personId2", idTravailleur);
						personPersonInstance.set("personRelationshipType", "EMPL-EMP");
						personPersonInstance.set("startDate", dateDebutRelationship1);
						personPersonInstance.set("endDate", dateFinContrat);
						personBoInstance.set("personId", idEmployeur);

						BusinessObjectDispatcher.update(personBoInstance);
						histoBoInstance.set("cmIdTravailleur", idTravailleur);
						histoBoInstance.set("cmIdEmployeur", idEmployeur);
						histoBoInstance.set("cmDateDebutContrat", dateDebutRelationship1);
						histoBoInstance.set("cmTypeMouvement", typeMouvement);
						histoBoInstance.set("cmDateFinContrat", dateFinContrat);
						histoBoInstance = BusinessObjectDispatcher.update(histoBoInstance);
						personPersonInstance.set("personId1", idTravailleur);
						personPersonInstance.set("personId2", idEmployeur);
						personPersonInstance.set("personRelationshipType", "EMPL-EMP");
						personPersonInstance.set("startDate", dateDebutRelationship1);
						personPersonInstance.set("endDate", dateFinContrat);
						personBoInstance.set("personId", idTravailleur);
						BusinessObjectDispatcher.update(personBoInstance);

						nextElt.setXMLFieldStringFromPath("rechercheEmploye/asCurrent", idTravailleur);
					}
				}
			}
		}
	}

	@Override
	public void setApplyFormRuleAlgorithmInputData(
			ApplyFormRuleAlgorithmInputData paramApplyFormRuleAlgorithmInputData) {
		applyFormRuleAlgorithmInputData = paramApplyFormRuleAlgorithmInputData;
	}

	@Override
	public void setApplyFormRuleAlgorithmInputOutputData(
			ApplyFormRuleAlgorithmInputOutputData paramApplyFormRuleAlgorithmInputOutputData) {
		applyFormRuleAlgorithmInputOutputData = paramApplyFormRuleAlgorithmInputOutputData;
	}

	@Override
	public ApplyFormRuleAlgorithmInputOutputData getApplyFormRuleAlgorithmInputOutputData() {
		return applyFormRuleAlgorithmInputOutputData;
	}

}
