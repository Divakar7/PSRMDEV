package com.splwg.cm.domain.batch;

import java.util.HashMap;

public final class CmEmployerRegConstant {

	
	
	public static final String DÉPARTEMENT = "Département";
	//Message Number
	public static final String EMPLOYER_EMAIL_INVALID = "301";
	public static final String EMPLOYER_EMAIL_EXIST = "302";
	public static final String TRN_EXIST = "303";
	public static final String TRN_INVALID = "304";
	public static final String NINET_INVALID = "305";
	public static final String TIN_INVALID = "306";
	public static final String DATE_LESSEQUAL_TODAY_VALID= "307";
	public static final String DATE_SAT_SUN_VALID= "308";
	public static final String DATE_EST_GREAT_IMM= "309";
	public static final String DATE_EMP_GREAT_EST= "310";
	public static final String DATE_EMP_GREAT_IMM= "311";
	public static final String INVALID_DATE= "312";
	public static final String NINEA_EXIST= "313";
	public static final String NINEA_INVALID = "314";
	public static final String TELEPHONE_INVALID = "315"; 
	public static final String NAME_INVALID = "324";
	public static final String NAME_LETTER_CHECK = "325";
	public static final String NIN_INVALID = "326";
	public static final String EMAIL_INVALID = "327";
	public static final String EMPTY = "328";
	public static final String DATE_DEL_GREAT_EXP= "329";
	
	//String
	public static final String NINEA_PREFIX ="00";
	public static final String UTF ="UTF-8";
	public static final String INVALID_DATE_STRING  = "invalidate";
	public static final String IMMATRICULATION_DATE = "Date d’immatriculation au registre de commerce";
	public static final String ESTABLISHMENT_DATE = "Date Ouverture Établissement";
	public static final String PHONE = "Téléphone fixe";
	public static final String TAX_IDENTIFY_NUM = "Code d'identification fiscale";
	public static final String EMAIL_EMPLOYER = "Email de l'employeur";
	public static final String TRADE_REG_NUM = "Numéro de registre du commerce";
	public static final String PREMIER_EMP_DATE = "Date d’embauche du premier salarié";
	
	//String empty check
	public static final String TYPE_D_EMPLOYEUR = "Type d'employeur";
	public static final String TYPE_D_EST = "Type d'établissement";
	public static final String RAISON_SOCIALE = "Raison Sociale";
	public static final String NINEA = "NINEA";
	public static final String EMPLOYE = "Employé";
	public static final String NOM_EMPLOYE = "Nom de l'employé";
	public static final String PRENOM_EMPLOYE = "Prénom de l'employé";
	public static final String NINET = "NINET";
	public static final String FORME_JURIDIQUE = "Forme Juridique";
	public static final String DATE_DE_CREATION = "Date de création";
	public static final String DATE_IDENTIFICATION_FISCALE = "Date d'identification fiscale";
	public static final String NUMERO_REGISTER_DE_COMMERCE = "Numéro du registre de commerce";
	public static final String DATE_IMM_REGISTER_DE_COMMERCE = "Date d’immatriculation au registre de commerce";
	public static final String DATE_OUVERTURE_EST = "Date Ouverture Etablissement";
	public static final String DATE_EMBAUCHE_PREMIER_SALARY = "Date d’embauche du premier salarié";
	public static final String SECTEUR_ACTIVITIES = "Secteur d’Activités";
	public static final String ACTIVATE_PRINCIPAL = "Activité principale";
	public static final String TAUX_AT = "Taux AT";
	public static final String NOMBRE_TRAVAIL_REGIME_GENERAL  = "Nombre de travailleurs en régime général";
	public static final String NOMBRE_TRAVAIL_REGIME_CADRE  = "Nombre de travailleurs en régime cadre";
	public static final String REGION  = "Région";
	//public static final String DEPARTMENT  = "Département";
	public static final String ARONDISSEMENT = "Arondissement";
	public static final String COMMUNE = "Commune";
	public static final String QUARTIER  = "Quartier";
	public static final String ADDRESS  = "Adresse";
	public static final String TELEPHONE = "Téléphone";
	public static final String EMAIL = "Email";
	public static final String ZONE_GEOGRAPHIQUE_CSS = "Zones géographiques CSS";
	public static final String ZONE_GEOGRAPHIQUE_IPRES = "Zones géographiques IPRES";
	public static final String SECTOR_GEOGRAPHIC_CSS = "Secteurs géographiques CSS";
	public static final String SECTOR_GEOGRAPHIC_IPRES = "Secteurs géographiques IPRES";
	public static final String AGENCE_CSS = "Agence CSS";
	public static final String AGENCE_IPRES = "Agence IPRES";
	public static final String LEGAL_REPRESENTANT = "Représentant légal";
	public static final String LAST_NAME = "Nom de famille";
	public static final String FIRST_NAME = "Prénom";
	//public static final String DATE_DE_NAISSANCE = "Date de naissance";
	public static final String NATIONALITE = "Nationalité";
	public static final String SEXE = "Sexe";
	public static final String ETAT_CIVIL = "Etat Civil";
	public static final String DATE_DE_NAISSANCE = "Date de Naissance";
	public static final String NUM_ID_CSS = "Numéro d'identification CSS";
	public static final String NUM_ID_IPRES = "numéro d'identification IPRES";
	public static final String NOM_DU_PERE = "Nom du père";
	public static final String PRENOM_DU_PERE = "Prénom du père";
	public static final String LEGAL_REP_NIN = "NIN représentant légal";
	public static final String EMPLOYEE_NIN = "NIN de l'employé";
	public static final String PAYS_DE_NAISSANCE = "Pays de naissance";
	public static final String NOM_DE_LA_MERE = "Nom de la mère";
	public static final String PRENOM_DE_LA_MERE  = "Prénom de la mère";
	public static final String NIN = "NIN";
	public static final String PAYS_NAIS  = "Pays de naissance";
	public static final String REGION_NAIS  = "Région de naissance";
    public static final String DEPARTMENT_NAIS  = "Département de naissance";
    public static final String ARONDISSEMENT_NAIS = "Arondissement de naissance";
    public static final String COMMUNE_NAIS = "Commune de naissance";
    public static final String QUARTIER_NAIS  = "Quartier de naissance";
	public static final String TYPE_PIECE_IDENTITE = "Type de pièce d’identité";
	public static final String NUMERO_PIECE_IDENTITE = "Numéro pièce d'identité";
	public static final String DATE_DE_DELIVRANCE = "Date de délivrance";
  public static final String LIEU_DE_DELIVRANCE = "Lieu de délivrance";
	public static final String DATE_DE_EXPIRATION = "Date d’expiration"; 	
    public static final String PAYS_RES  = "Pays";
    public static final String REGION_RES  = "Région";
    public static final String DEPARTMENT_RES  = "Département";
    public static final String ARONDISSEMENT_RES = "Arondissement";
    public static final String COMMUNE_RES = "Commune";
    public static final String QUARTIER_RES  = "Quartier";
    public static final String ADDRESS_RES  = "Adresse";
	public static final String BOITE_POSTALE  = "Boite postale";
	public static final String TYPE_MOUVEMENT  = "Type de mouvement";
	public static final String NATURE_CONTRAT = "Nature du contrat";
	public static final String MOBILE_NUM = "Numéro mobile";
	
	public static final String PROFESSION = "Profession";
	public static final String EMPLOI = "Emploi";
	public static final String EMPLOYE_CADRE = "Employé cadre";
	public static final String CONV_APPLICABLE = "Convention applicable";
	public static final String SALAIRE_CONTRACTUEL = "Salaire contractuel";
	public static final String TEMPS_DE_TRAVAIL = "Temps de travail";
	public static final String EMPLOYE_AT_MP = "Employe AT/MP Secteur des affaires";
	public static final String CATEGORIE = "Catégorie";
	public static final String DATE_DEBUT_CONTRAT = "Date de début du contrat";
	public static final String DATE_FIN_CONTRAT = "Date de fin du contrat";
	
	public static final String HEADER_SQL_QUERY = "INSERT INTO CI_FORM_BATCH_HDR (FORM_BATCH_HDR_ID,EXT_FORM_BATCH_ID,BUS_OBJ_CD,BO_STATUS_CD,"
			+ " STATUS_UPD_DTTM,CRE_DTTM,TOT_PAY_AMT,TOT_FORMS_CNT,VERSION,C1_FORM_SRCE_CD,BO_DATA_AREA) VALUES"
			+ " (:FORM_BATCH_HDR_ID,:EXT_FORM_BATCH_ID,:BUS_OBJ_CD,:BO_STATUS_CD,:STATUS_UPD_DTTM,:CRE_DTTM,:TOT_PAY_AMT,:TOT_FORMS_CNT,:VERSION,"
			+ ":C1_FORM_SRCE_CD,:BO_DATA_AREA)";
	
	public static final String STAGING_SQL_QUERY = "INSERT into CI_FORM_UPLD_STG (C1_FORM_UPLD_STG_ID,FORM_BATCH_HDR_ID,"
			+ "C1_EXT_FORM_SUBM_SEQ,C1_FORM_SRCE_CD,EXT_FORM_TYPE,C1_FORM_YEAR,C1_FORM_UPLD_STG_TYP_CD,BUS_OBJ_CD,"
			+ "BO_STATUS_CD,C1_FORM_PYMNT_FLG, C1_FORM_PYMNT_AMT,VERSION,STATUS_UPD_DTTM,CRE_DTTM,BO_DATA_AREA) values (:C1_FORM_UPLD_STG_ID,"
			+ ":FORM_BATCH_HDR_ID,:C1_EXT_FORM_SUBM_SEQ,:C1_FORM_SRCE_CD,:EXT_FORM_TYPE,:C1_FORM_YEAR,:C1_FORM_UPLD_STG_TYP_CD,"
			+ ":BUS_OBJ_CD,:BO_STATUS_CD,:C1_FORM_PYMNT_FLG,:C1_FORM_PYMNT_AMT,"
			+ ":VERSION,to_date(:STATUS_UPD_DTTM,'DD/MM/YYYY'),to_date(:CRE_DTTM,'DD/MM/YYYY'),:BO_DATA_AREA)";
	
	
	public static final HashMap<String, String> lookUpConstant = new HashMap<String, String>();
	public HashMap<String, String> getLookUpConstanst() {
		lookUpConstant.put(SECTEUR_ACTIVITIES, "CMSECTEURACTIVITES_L");
		lookUpConstant.put(ACTIVATE_PRINCIPAL, "CMACTIVITESPRINCIPAL_L");
		lookUpConstant.put("REGION", "CMREGION_L");
		lookUpConstant.put(COMMUNE, "CMCOMMUNE_L");
		lookUpConstant.put(QUARTIER, "CMQUARTIER_L");
		lookUpConstant.put(ZONE_GEOGRAPHIQUE_CSS, "CMZONE_L");
		lookUpConstant.put(ZONE_GEOGRAPHIQUE_IPRES, "CMZONE_L");
		lookUpConstant.put(SECTOR_GEOGRAPHIC_CSS, "CMSECTEUR_L");
		lookUpConstant.put(SECTOR_GEOGRAPHIC_IPRES, "CMSECTEUR_L");
		lookUpConstant.put(AGENCE_CSS, "CMAGENCE_L");
		lookUpConstant.put(AGENCE_IPRES, "CMAGENCE_L");
		lookUpConstant.put("DEPARTMENT", "CMDEPARTEMENT_L");
		lookUpConstant.put(ARONDISSEMENT, "CMARRONDISSEMENT_L");
		lookUpConstant.put(TAUX_AT, "CMATRATE_L"); 
		
		return lookUpConstant;
	}
	

	
	//Regular Expression
	
	public static final String VALIDATE_PHONE_NUMBER =  "^(?:33|70|76|77){0,2}\\d{7}$";
	public static final String VALIDATE_NINEA_NUMBER = "\\d{9}$";
	public static final String VALIDATE_NIN_NUMBER = "^[1-2]{1}[0-9]{3}[0-9]{4}[0-9]{5,6}$";
	public static final String VALIDATE_ALPHABETS_ONLY = "[A-Za-z\\s]+";
	public static final String VALIDATE_NINET_NUMBER = "\\d{13}$";
	public static final String VALIDATE_TAX_IDENFICATION_NUMBER = "^[0-9]{1}[A-Z]{1}[0-9]{1}$";
	public static final String VALIDATE_COMMERCIAL_REGISTER = "^(?:SN)\\.[A-Za-z0-9]{3}\\.[0-9]{4}\\.(?:A|B|C|E|M){1}\\.[0-9]{1,5}$";
	
}