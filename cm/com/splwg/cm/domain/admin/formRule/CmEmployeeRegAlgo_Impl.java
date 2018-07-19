package com.splwg.cm.domain.admin.formRule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.splwg.base.api.businessObject.BusinessObjectDispatcher;
import com.splwg.base.api.businessObject.BusinessObjectInstance;
import com.splwg.base.api.businessObject.BusinessObjectInstanceKey;
import com.splwg.base.api.businessObject.COTSFieldDataAndMD;
import com.splwg.base.api.businessObject.COTSInstanceNode;
import com.splwg.base.api.lookup.BusinessObjectActionLookup;
import com.splwg.base.domain.common.businessObject.BusinessObject;
import com.splwg.base.domain.common.businessObject.BusinessObjectExitStatusAlgorithmSpot;
import com.splwg.shared.logging.Logger;
import com.splwg.shared.logging.LoggerFactory;
import com.splwg.tax.domain.admin.formType.FormType;
import com.splwg.tax.domain.admin.formType.FormType_Id;


/**
 * @author Divakar
 *
@AlgorithmComponent (softParameters = { @AlgorithmSoftParameter (name = fromType, type = string)
 *            , @AlgorithmSoftParameter (name = filePath, type = string)
 *            , @AlgorithmSoftParameter (name = successFilePath, type = string)
 *            , @AlgorithmSoftParameter (name = errorFilePath, type = string)})
 */
public class CmEmployeeRegAlgo_Impl extends CmEmployeeRegAlgo_Gen implements BusinessObjectExitStatusAlgorithmSpot {

	private BusinessObjectInstanceKey boKey;
	private BusinessObjectInstance boInstance;
	private final static Logger log = LoggerFactory.getLogger(CmEmployeeRegAlgo_Impl.class);
	private String fileName = null;

	public static final String AS_CURRENT = "asCurrent";
	//private CmXLSXReaderComponent cmXLSXReader = CmXLSXReaderComponent.Factory.newInstance();
	//private static CmHelper customHelper = new CmHelper();
	//XSSFSheet spreadsheet;
	//private int cellId = 0;

	// private File[] getNewTextFiles() {
	// File dir = new File(this.getFilePath());
	// return dir.listFiles(new FilenameFilter() {
	// @Override
	// public boolean accept(File dir, String name) {
	// return name.toLowerCase().endsWith(".xlsx");
	// }
	// });
	// }

	@Override
	public void invoke() {

		// String bus_id = boKey.getBusinessObject().getId().getIdValue();
		/*
		 * System.out.println("I am In Invoke method " + this.boKey);
		 * this.boInstance = BusinessObjectDispatcher.read(this.boKey, false);
		 * COTSFieldDataAndMD cots =
		 * this.boInstance.getFieldAndMDForPath("employerDetails/ninea"); String
		 * ninea = cots.getValue().toString();
		 */
		String nineaNumber = "488565498";
		System.out.println("Ninea: " + nineaNumber);// 90909090990EMPLR.xlsx/99009099909099EMPLE.xlsx
		fileName = nineaNumber + "EMPLE" + ".csv";
		boolean fileExist = verifyExistFileInFolder(this.getFilePath() + fileName);
		if (fileExist) {
			// this.processLookup();
			readExcelFileAndPostForm(this.getFilePath() + fileName);
			
		} else {
			// createToDo("", nineaNumber, "", fileName);
			// addError(CmMessageRepository90000.MSG_10001());
		}
	}

	private boolean verifyExistFileInFolder(String fileName) {
		Path path = Paths.get(fileName);
		boolean isExits = false;
		if (Files.exists(path)) {
			isExits = true;
		}
		return isExits;

	}

	private void readExcelFileAndPostForm(String regFileName) {
		
		 log.info("I am in readExcelFileAndPostForm: " + regFileName);
		  System.out.println("I am in readExcelFileAndPostForm: " + regFileName);

		  File file = null;
		  file = new File(regFileName);
		  boolean processed = false;
		  List<Object> listValues = new ArrayList<Object>();

		  Scanner scanner = null;
		  try {
		   scanner = new Scanner(file);
		  } catch (FileNotFoundException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		   log.info("File Not Found ");
		   System.out.println("File Not Found ");
		  }
		  
		  int count = 0;
		  while (scanner.hasNextLine()) {
		   
		   //System.out.println("***Reading From File****Line Number****" + scanner.nextLine());
		   String csvValues = scanner.nextLine();
		   csvValues = csvValues.replace("é", "e");
		   csvValues = csvValues.replace("è", "e");
		   csvValues = csvValues.replace("à", "a");
		   System.out.println("***Reading From File****Line Number**** " +csvValues);
		   if(count >= 1) {
		    String[] terms = csvValues.split(",");
		    for(int i = 0; i<terms.length; i++) {
		     /*if(i == 15 || i == 30 || i == 16 || i == 20 || i == 21 || i == 22 || i == 23 || i == 24 || i == 31 || i == 32 || i == 33 || i == 34 || i == 35 ) {
		      terms[i] = encoder(terms[i]);
		     }*/
		     listValues.add(terms[i]); 
		    }
		    
		   }
		   count++;
		  }
		  System.out.println("ArrayList: " + listValues);
		  log.info("ArrayList: " + listValues);
		  processed = formCreator(listValues);
		 
		/*
		 * if (processed) { customHelper.moveFileToProcessedFolder(fileName,
		 * this.getParameters().getPathToMove()); } else {
		 * customHelper.moveFileToFailuireFolder(fileName,
		 * this.getParameters().getErrorFilePathToMove()); }
		 */

		System.out.println("######################## Terminer executeWorkUnit ############################");
	}

	
	private String convertDateFormat(String dateObject) {
		String parsedDate = "";

		try {
			if (dateObject.contains("GMT")) {
				DateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss 'GMT' yyyy");
				java.util.Date date = inputFormat.parse(dateObject);
				DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				parsedDate = outputFormat.format(date);
			} else if(dateObject.contains("UTC")) {
				DateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss 'UTC' yyyy", Locale.ENGLISH);
				java.util.Date date = inputFormat.parse(dateObject);
				DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				parsedDate = outputFormat.format(date);
			} 
			else if(dateObject.contains("CST")) {
                DateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss 'CST' yyyy", Locale.ENGLISH);
                java.util.Date date = inputFormat.parse(dateObject);
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                parsedDate = outputFormat.format(date);
            }
			else if(dateObject.contains("CDT")) {
                DateFormat inputFormat = new SimpleDateFormat("E MMM dd HH:mm:ss 'CDT' yyyy", Locale.ENGLISH);
                java.util.Date date = inputFormat.parse(dateObject);
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                parsedDate = outputFormat.format(date);
            }else {
				DateFormat inputFormat1 = new SimpleDateFormat("dd/MM/yyyy");
				java.util.Date input = inputFormat1.parse(dateObject);
				DateFormat outputFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				parsedDate = outputFormat1.format(input);
			}
		} catch (Exception exception) {
			parsedDate = "invalidate";
			log.error("Error while parsing the date format" + exception);
		}
		return parsedDate;
	}
	
	
	private boolean formCreator(List<Object> valeurs) {

		BusinessObjectInstance boInstance = null;

		boInstance = createFormBOInstance(this.getFromType(), "EMPLOYEE_REG-" + getSystemDateTime().toString());

		COTSInstanceNode employeurSection = boInstance.getGroup("employeur");
		COTSInstanceNode employeeSection = boInstance.getGroup("employe");
		COTSInstanceNode element = employeeSection.getList("employeList").newChild();
		int count = 0;
		while (count == 0) {

			// COTSFieldDataAndMD<?> idEmployeur =
			// employeurSection.getFieldAndMDForPath("employeur/asCurrent");
			// idEmployeur.setXMLValue(valeurs.get(count).toString());
			// count++;

			COTSFieldDataAndMD<?> ninea = employeurSection.getFieldAndMDForPath("ninea/asCurrent");
			ninea.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> nomEmploye = element.getFieldAndMDForPath("nomEmploye/asCurrent");
			nomEmploye.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> prenomEmploye = element.getFieldAndMDForPath("prenomEmploye/asCurrent");
			prenomEmploye.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> sexe = element.getFieldAndMDForPath("sexe/asCurrent");
			sexe.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> etatCivil = element.getFieldAndMDForPath("etatCivil/asCurrent");
			etatCivil.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> dateDeNaissance = element.getFieldAndMDForPath("dateDeNaissance/asCurrent");
			dateDeNaissance.setXMLValue(convertDateFormat(valeurs.get(count).toString()));
			
			count++;
			
			COTSFieldDataAndMD<?> numRegNaiss = element.getFieldAndMDForPath("numRegNaiss/asCurrent");
			numRegNaiss.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> nomPere = element.getFieldAndMDForPath("nomPere/asCurrent");
			nomPere.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> prenomPere = element.getFieldAndMDForPath("prenomPere/asCurrent");
			prenomPere.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> nomMere = element.getFieldAndMDForPath("nomMere/asCurrent");
			nomMere.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> prenomMere = element.getFieldAndMDForPath("prenomMere/asCurrent");
			prenomMere.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> nationalite = element.getFieldAndMDForPath("nationalite/asCurrent");
			nationalite.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> typePieceIdentite = element.getFieldAndMDForPath("typePieceIdentite/asCurrent");
			typePieceIdentite.setXMLValue(valeurs.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> nin = element.getFieldAndMDForPath("nin/asCurrent");
			nin.setXMLValue(valeurs.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> ninCEDEAO = element.getFieldAndMDForPath("ninCEDEAO/asCurrent");
			ninCEDEAO.setXMLValue(valeurs.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> numPieceIdentite = element.getFieldAndMDForPath("numPieceIdentite/asCurrent");
			numPieceIdentite.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> delivreLe = element.getFieldAndMDForPath("delivreLe/asCurrent");
			delivreLe.setXMLValue(convertDateFormat(valeurs.get(count).toString()));
			count++;

			COTSFieldDataAndMD<?> lieuDelivrance = element.getFieldAndMDForPath("lieuDelivrance/asCurrent");
			lieuDelivrance.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> expireLe = element.getFieldAndMDForPath("expireLe/asCurrent");
			expireLe.setXMLValue(convertDateFormat(valeurs.get(count).toString()));
			count++;

			COTSFieldDataAndMD<?> paysDeNaissance = element.getFieldAndMDForPath("paysDeNaissance/asCurrent");
			paysDeNaissance.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> villeDeNaissance = element.getFieldAndMDForPath("villeDeNaissance/asCurrent");
			villeDeNaissance.setXMLValue(valeurs.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> pays = element.getFieldAndMDForPath("pays/asCurrent");
			pays.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> region = element.getFieldAndMDForPath("region/asCurrent");
			region.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> departement = element.getFieldAndMDForPath("departement/asCurrent");
			departement.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> arrondissement = element.getFieldAndMDForPath("arrondissement/asCurrent");
			arrondissement.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> commune = element.getFieldAndMDForPath("commune/asCurrent");
			commune.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> quartier = element.getFieldAndMDForPath("quartier/asCurrent");
			quartier.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> adresse = element.getFieldAndMDForPath("adresse/asCurrent");
			adresse.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> boitePostale = element.getFieldAndMDForPath("boitePostale/asCurrent");
			boitePostale.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> typeMouvement = element.getFieldAndMDForPath("typeMouvement/asCurrent");
			typeMouvement.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> natureContrat = element.getFieldAndMDForPath("natureContrat/asCurrent");
			natureContrat.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> dateDebut = element.getFieldAndMDForPath("dateDebut/asCurrent");
			dateDebut.setXMLValue(convertDateFormat(valeurs.get(count).toString()));
			count++;

			COTSFieldDataAndMD<?> dateFinContrat = element.getFieldAndMDForPath("dateFinContrat/asCurrent");
			dateFinContrat.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> profession = element.getFieldAndMDForPath("profession/asCurrent");
			profession.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> emploi = element.getFieldAndMDForPath("emploi/asCurrent");
			emploi.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> employeCadre = element.getFieldAndMDForPath("nonEmp/asCurrent");
			employeCadre.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> conventionApplicable = element.getFieldAndMDForPath("conventionApplicable/asCurrent");
			conventionApplicable.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> salaireContractuel = element.getFieldAndMDForPath("salaireContractuel/asCurrent");
			salaireContractuel.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> tpsDeTravail = element.getFieldAndMDForPath("tpsDeTravail/asCurrent");
			tpsDeTravail.setXMLValue(valeurs.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> categorie = element.getFieldAndMDForPath("categorie/asCurrent");
			categorie.setXMLValue(valeurs.get(count).toString());
			count++;

		}

		if (boInstance != null) {
			boInstance = validateAndPostForm(boInstance);
		}
		return true;

	}

	/**
	 * Method to create FormBOInstance
	 * 
	 * @param formType
	 * @param string
	 * @return
	 */
	private BusinessObjectInstance createFormBOInstance(String formTypeString, String documentLocator) {

		FormType formType = new FormType_Id(formTypeString).getEntity();
		String formTypeBo = formType.getRelatedTransactionBOId().getTrimmedValue();

		log.info("#### Creating BO for " + formType);

		BusinessObjectInstance boInstance = BusinessObjectInstance.create(formTypeBo);

		log.info("#### Form Type BO MD Schema: " + boInstance.getSchemaMD());

		boInstance.set("bo", formTypeBo);
		boInstance.set("formType", formType.getId().getTrimmedValue());
		boInstance.set("receiveDate", getSystemDateTime().getDate());
		boInstance.set("documentLocator", documentLocator);

		return boInstance;

	}

	private BusinessObjectInstance validateAndPostForm(BusinessObjectInstance boInstance) {

		log.info("#### BO Instance Schema before ADD: " + boInstance.getDocument().asXML());
		boInstance = BusinessObjectDispatcher.add(boInstance);
		log.info("#### BO Instance Schema after ADD: " + boInstance.getDocument().asXML());

		boInstance.set("boStatus", "VALIDATE");
		boInstance = BusinessObjectDispatcher.update(boInstance);
		log.info("#### BO Instance Schema after VALIDATE: " + boInstance.getDocument().asXML());

		boInstance.set("boStatus", "READYFORPOST");
		boInstance = BusinessObjectDispatcher.update(boInstance);
		log.info("#### BO Instance Schema after READYFORPOST: " + boInstance.getDocument().asXML());

		boInstance.set("boStatus", "POSTED");
		boInstance = BusinessObjectDispatcher.update(boInstance);
		log.info("#### BO Instance Schema after POSTED: " + boInstance.getDocument().asXML());

		
		return boInstance;

	}

	@Override
	public boolean getForcePostProcessing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAction(BusinessObjectActionLookup arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBusinessObject(BusinessObject arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBusinessObjectKey(BusinessObjectInstanceKey arg0) {
		// TODO Auto-generated method stub
		this.boKey = arg0;
	}

}
