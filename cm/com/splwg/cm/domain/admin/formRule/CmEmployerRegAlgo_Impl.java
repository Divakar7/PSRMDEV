package com.splwg.cm.domain.admin.formRule;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import com.splwg.base.api.businessObject.BusinessObjectDispatcher;
import com.splwg.base.api.businessObject.BusinessObjectInstance;
import com.splwg.base.api.businessObject.BusinessObjectInstanceKey;
import com.splwg.base.api.businessObject.COTSFieldDataAndMD;
import com.splwg.base.api.businessObject.COTSInstanceNode;
import com.splwg.base.api.businessService.BusinessServiceDispatcher;
import com.splwg.base.api.businessService.BusinessServiceInstance;
import com.splwg.base.api.lookup.BusinessObjectActionLookup;
import com.splwg.base.domain.common.businessObject.BusinessObject;
import com.splwg.base.domain.common.businessObject.BusinessObjectExitStatusAlgorithmSpot;
import com.splwg.base.domain.todo.role.Role;
import com.splwg.base.domain.todo.role.Role_Id;
import com.splwg.cm.domain.batch.CmEmployerRegConstant;
import com.splwg.cm.domain.batch.CmEmployerRegHelper;
import com.splwg.shared.logging.Logger;
import com.splwg.shared.logging.LoggerFactory;
import com.splwg.tax.domain.admin.formType.FormType;
import com.splwg.tax.domain.admin.formType.FormType_Id;

/**
 * @author Denash Kumar M
 *
@AlgorithmComponent (softParameters = { @AlgorithmSoftParameter (name = formType, type = string)
 *            , @AlgorithmSoftParameter (name = filePath, type = string)
 *            , @AlgorithmSoftParameter (name = successFilePath, type = string)
 *            , @AlgorithmSoftParameter (name = errorFilePath, type = string)})
 */
public class CmEmployerRegAlgo_Impl extends CmEmployerRegAlgo_Gen implements BusinessObjectExitStatusAlgorithmSpot {

	private final static Logger log = LoggerFactory.getLogger(CmEmployerRegAlgo_Impl.class);
	private BusinessObjectInstanceKey boKey;
	private BusinessObjectInstance boInstance;
	String fileName = null;
	private final static CmEmployerRegHelper customHelper = new CmEmployerRegHelper();
	private final static CmEmployerRegConstant cmConstants = new CmEmployerRegConstant();
	final static HashMap<String, String> regionMap = new HashMap<String, String>();
	final static HashMap<String, String> deptMap =  new HashMap<String, String>();
	final static HashMap<String, String> arrondMap =  new HashMap<String, String>();
	final static HashMap<String, String> communeMap =  new HashMap<String, String>();
	final static HashMap<String, String> qartierMap = new HashMap<String, String>();
	final static HashMap<String, String> agenceMap =  new HashMap<String, String>();
	final static HashMap<String, String> zoneMap =  new HashMap<String, String>();
	final static HashMap<String, String> sectorMap =  new HashMap<String, String>();
	final static HashMap<String, String> sectorActMap =  new HashMap<String, String>();
	final static HashMap<String, String> actPrinceMap =  new HashMap<String, String>();
	final static HashMap<String, String> atRateMap =  new HashMap<String, String>();
	
	//Fields for Constants
	
	public static final String AS_CURRENT = "asCurrent";
	
	Calendar calInstacne = Calendar.getInstance();
	Date statusUploadDate = new Date();
	
	@Override
	public void invoke() {
		// TODO Auto-generated method stub
		//String bus_id = boKey.getBusinessObject().getId().getIdValue();
		System.out.println("I am In Invoke method " + this.boKey);
		log.info("I am In Invoke method BO intance Key " + this.boKey);
		this.boInstance = BusinessObjectDispatcher.read(this.boKey, false);
		log.info("I am In Invoke method BO intance " + this.boInstance);
		COTSFieldDataAndMD cots = this.boInstance.getFieldAndMDForPath("employerDetails/ninea");
		String nineaNumber = cots.getValue().toString();
		//String nineaNumber = "458343243";
		System.out.println("Ninea: " + nineaNumber);//90909090990EMPLR.xlsx/99009099909099EMPLE.xlsx
		log.info("Ninea: " + nineaNumber);
		fileName = nineaNumber+"EMPLR"+".csv";
		boolean fileExist = verifyExistFileInFolder(this.getFilePath()+fileName);
		if(fileExist) {
			this.processLookup();
			readExcelFileAndPostForm(this.getFilePath()+fileName);
		} else {
			createToDo("", nineaNumber, "", fileName);
			//addError(CmMessageRepository90000.MSG_10001());       
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
	
	/**
	 * Method to get the Files
	 * 
	 * @param ninea
	 * @return
	 *//*
	private File[] getNewTextFiles(String ninea) {
		File dir = new File(this.getFilePath());
		return dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(ninea+"EMPLR"+".xlsx");
			}
		});
	}*/
	
	
	/**
	 * Method to read Excel File
	 * 
	 * @param fileName2
	 */
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
		}
		
		int count = 0;
		while (scanner.hasNextLine()) {
			
			//System.out.println("***Reading From File****Line Number****" + scanner.nextLine());
			String csvValues = scanner.nextLine();
			System.out.println("***Reading From File****Line Number**** " +csvValues);
			if(count >= 1) {
				String[] terms = csvValues.split(",");
				for(int i = 0; i<terms.length; i++) {
					listValues.add(terms[i]);	
				}
				
			}
			count++;
		}
		System.out.println("ArrayList: " + listValues);
		log.info("ArrayList: " + listValues);
		processed = formCreator(listValues);
		if (processed) {
			customHelper.moveFileToProcessedFolder(fileName, this.getSuccessFilePath());
		} else {
			customHelper.moveFileToFailuireFolder(fileName, this.getErrorFilePath());
		}
	
	}
	
	
	/**
	 * Method to validate Date Format
	 * 
	 * @param dateObject
	 * @return
	 */
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
	
	/**
	 * Method Form Creator
	 * 
	 * @param fileName2
	 * @param listesValues
	 * @return
	 */
	private boolean formCreator(List<Object> listesValues) {
		log.info("I am Inside Bo Creator Size: " +  listesValues.size());
		System.out.println("I am Inside Bo Creator Size: " + listesValues.size());
		BusinessObjectInstance boInstance = null;

		boInstance = createFormBOInstance(this.getFormType(), "T-REG-" + getSystemDateTime().toString());

		COTSInstanceNode employerQuery = boInstance.getGroup("employerQuery");
		COTSInstanceNode mainRegistrationForm = boInstance.getGroup("mainRegistrationForm");
		COTSInstanceNode legalRepresentativeForm = boInstance.getGroup("legalRepresentativeForm");

		int count = 0;
		while (count == 0) {

			COTSFieldDataAndMD<?> regType = employerQuery.getFieldAndMDForPath("regType/asCurrent");
			regType.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> employerType = employerQuery.getFieldAndMDForPath("employerType/asCurrent");
			employerType.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> estType = employerQuery.getFieldAndMDForPath("estType/asCurrent");
			estType.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> employerName = employerQuery.getFieldAndMDForPath("employerName/asCurrent");
			employerName.setXMLValue(listesValues.get(count).toString());
			count++;// Moved from second section
			
			COTSFieldDataAndMD<?> hqId = employerQuery.getFieldAndMDForPath("hqId/asCurrent");
			hqId.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> nineaNumber = employerQuery.getFieldAndMDForPath("nineaNumber/asCurrent");
			nineaNumber.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> ninetNumber = employerQuery.getFieldAndMDForPath("ninetNumber/asCurrent");
			ninetNumber.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> companyOriginId = employerQuery.getFieldAndMDForPath("companyOriginId/asCurrent");
			companyOriginId.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> legalStatus = employerQuery.getFieldAndMDForPath("legalStatus/asCurrent");
			legalStatus.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> taxId = employerQuery.getFieldAndMDForPath("taxId/asCurrent");
			taxId.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> taxIdDate = employerQuery.getFieldAndMDForPath("taxIdDate/asCurrent");
			taxIdDate.setXMLValue(convertDateFormat(listesValues.get(count).toString()));
			count++;

			COTSFieldDataAndMD<?> tradeRegisterNumber = employerQuery.getFieldAndMDForPath("tradeRegisterNumber/asCurrent");
			tradeRegisterNumber.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> tradeRegisterDate = employerQuery.getFieldAndMDForPath("tradeRegisterDate/asCurrent");
			tradeRegisterDate.setXMLValue(convertDateFormat(listesValues.get(count).toString()));
			count++;
			// --------------------------*************------------------------------------------------------------------------------//

			// ******Main Registration Form BO
			// Creation*********************************//
			COTSFieldDataAndMD<?> dateOfInspection = mainRegistrationForm.getFieldAndMDForPath("dateOfInspection/asCurrent");
			dateOfInspection.setXMLValue(convertDateFormat(listesValues.get(count).toString()));
			count++;

			COTSFieldDataAndMD<?> dateOfFirstHire = mainRegistrationForm.getFieldAndMDForPath("dateOfFirstHire/asCurrent");
			dateOfFirstHire.setXMLValue(convertDateFormat(listesValues.get(count).toString()));
			count++;

			/*COTSFieldDataAndMD<?> shortName = mainRegistrationForm.getFieldAndMDForPath("shortName/asCurrent");
			shortName.setXMLValue(listesValues.get(count).toString());
			count++;*/

			COTSFieldDataAndMD<?> businessSector = mainRegistrationForm.getFieldAndMDForPath("businessSector/asCurrent");
			businessSector.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> mainLineOfBusiness = mainRegistrationForm.getFieldAndMDForPath("mainLineOfBusiness/asCurrent");
			mainLineOfBusiness.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> atRate = mainRegistrationForm.getFieldAndMDForPath("atRate/asCurrent");
			atRate.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> noOfWorkersInGenScheme = mainRegistrationForm.getFieldAndMDForPath("noOfWorkersInGenScheme/asCurrent");
			noOfWorkersInGenScheme.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> noOfWorkersInBasicScheme = mainRegistrationForm.getFieldAndMDForPath("noOfWorkersInBasicScheme/asCurrent");
			noOfWorkersInBasicScheme.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> region = mainRegistrationForm.getFieldAndMDForPath("region/asCurrent");
			region.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> department = mainRegistrationForm.getFieldAndMDForPath("department/asCurrent");
			department.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> arondissement = mainRegistrationForm.getFieldAndMDForPath("arondissement/asCurrent");
			arondissement.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> commune = mainRegistrationForm.getFieldAndMDForPath("commune/asCurrent");
			commune.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> qartier = mainRegistrationForm.getFieldAndMDForPath("qartier/asCurrent");
			qartier.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> address = mainRegistrationForm.getFieldAndMDForPath("address/asCurrent");
			address.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> postboxNumber = mainRegistrationForm.getFieldAndMDForPath("postboxNo/asCurrent");
			postboxNumber.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> telephone = mainRegistrationForm.getFieldAndMDForPath("telephone/asCurrent");
			telephone.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> email = mainRegistrationForm.getFieldAndMDForPath("email/asCurrent");
			email.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> website = mainRegistrationForm.getFieldAndMDForPath("website/asCurrent");
			website.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> zoneCss = mainRegistrationForm.getFieldAndMDForPath("zoneCss/asCurrent");
			zoneCss.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> zoneIpres = mainRegistrationForm.getFieldAndMDForPath("zoneIpres/asCurrent");
			zoneIpres.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> sectorCss = mainRegistrationForm.getFieldAndMDForPath("sectorCss/asCurrent");
			sectorCss.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> sectorIpres = mainRegistrationForm.getFieldAndMDForPath("sectorIpres/asCurrent");
			sectorIpres.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> agencyCss = mainRegistrationForm.getFieldAndMDForPath("agencyCss/asCurrent");
			agencyCss.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> agencyIpres = mainRegistrationForm.getFieldAndMDForPath("agencyIpres/asCurrent");
			agencyIpres.setXMLValue(listesValues.get(count).toString());
			count++;

			// ------------------------------LegalRepresentativeForm BO
			// Creation----------------------------------------------------//

			COTSFieldDataAndMD<?> legalRepPerson = legalRepresentativeForm.getFieldAndMDForPath("legalRepPerson/asCurrent");
			legalRepPerson.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> lastName = legalRepresentativeForm.getFieldAndMDForPath("lastName/asCurrent");
			lastName.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> firstName = legalRepresentativeForm.getFieldAndMDForPath("firstName/asCurrent");
			firstName.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> birthDate = legalRepresentativeForm.getFieldAndMDForPath("birthDate/asCurrent");
			birthDate.setXMLValue(convertDateFormat(listesValues.get(count).toString()));
			count++;

			COTSFieldDataAndMD<?> nationality = legalRepresentativeForm.getFieldAndMDForPath("nationality/asCurrent");
			nationality.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> nin = legalRepresentativeForm.getFieldAndMDForPath("nin/asCurrent");
			nin.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> placeOfBirth = legalRepresentativeForm.getFieldAndMDForPath("placeOfBirth/asCurrent");
			placeOfBirth.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> cityOfBirth = legalRepresentativeForm.getFieldAndMDForPath("cityOfBirth/asCurrent");
			cityOfBirth.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> typeOfIdentity = legalRepresentativeForm.getFieldAndMDForPath("typeOfIdentity/asCurrent");
			typeOfIdentity.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> identityIdNumber = legalRepresentativeForm.getFieldAndMDForPath("identityIdNumber/asCurrent");
			identityIdNumber.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> ninCedeo = legalRepresentativeForm.getFieldAndMDForPath("ninCedeo/asCurrent");
			ninCedeo.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> dateOfIssue = legalRepresentativeForm.getFieldAndMDForPath("issuedDate/asCurrent");
			dateOfIssue.setXMLValue(convertDateFormat(listesValues.get(count).toString()));
			count++;

			COTSFieldDataAndMD<?> expirationDate = legalRepresentativeForm.getFieldAndMDForPath("expiryDate/asCurrent");
			expirationDate.setXMLValue(convertDateFormat(listesValues.get(count).toString()));
			count++;

			COTSFieldDataAndMD<?> legalRegion = legalRepresentativeForm.getFieldAndMDForPath("region/asCurrent");
			legalRegion.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> legalDepartment = legalRepresentativeForm.getFieldAndMDForPath("department/asCurrent");
			legalDepartment.setXMLValue(listesValues.get(count).toString());
			count++;
			
			COTSFieldDataAndMD<?> arondissementLegal = legalRepresentativeForm.getFieldAndMDForPath("arondissement/asCurrent");
			arondissementLegal.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> communeLegal = legalRepresentativeForm.getFieldAndMDForPath("commune/asCurrent");
			communeLegal.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> legalqartier = legalRepresentativeForm.getFieldAndMDForPath("qartier/asCurrent");
			legalqartier.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> legaladdress = legalRepresentativeForm.getFieldAndMDForPath("address/asCurrent");
			legaladdress.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> landLineNumber = legalRepresentativeForm.getFieldAndMDForPath("landLineNumber/asCurrent");
			landLineNumber.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> mobileNumber = legalRepresentativeForm.getFieldAndMDForPath("mobileNumber/asCurrent");
			mobileNumber.setXMLValue(listesValues.get(count).toString());
			count++;

			COTSFieldDataAndMD<?> legalRepresentativeEmail = legalRepresentativeForm.getFieldAndMDForPath("email/asCurrent");
			legalRepresentativeEmail.setXMLValue(listesValues.get(count).toString());
			count++;

			// --------------------------*************------------------------------------------------------------------------------//

		}

		if (boInstance != null) {
			boInstance = validateAndPostForm(boInstance);
		}
		return true;
	}
	
	/**
	 * Method to create Form BO Instance
	 * 
	 * @param formTypeString
	 * @param documentLocator
	 * @return
	 */
	private BusinessObjectInstance createFormBOInstance(String formTypeString, String documentLocator) {

		FormType formType = new FormType_Id(formTypeString).getEntity();
		String formTypeBo = formType.getRelatedTransactionBOId().getTrimmedValue();

		log.info("#### Creating BO for " + formType);
		System.out.println("#### Creating BO for " + formType);

		BusinessObjectInstance boInstance = BusinessObjectInstance.create(formTypeBo);

		log.info("#### Form Type BO MD Schema: " + boInstance.getSchemaMD());
		System.out.println("#### Form Type BO MD Schema: " + boInstance.getSchemaMD());

		boInstance.set("bo", formTypeBo);
		boInstance.set("formType", formType.getId().getTrimmedValue());
		boInstance.set("receiveDate", getSystemDateTime().getDate());
		boInstance.set("documentLocator", documentLocator);

		return boInstance;

	}
	
	/**
	 * Method to validate and post the form
	 * 
	 * @param boInstance
	 * @return
	 */
	private BusinessObjectInstance validateAndPostForm(BusinessObjectInstance boInstance) {

		  log.info("#### BO Instance Schema before ADD: " + boInstance.getDocument().asXML());
		  System.out.println("#### BO Instance Schema before ADD: " + boInstance.getDocument().asXML());
		  boInstance = BusinessObjectDispatcher.add(boInstance);
		  log.info("#### BO Instance Schema after ADD: " + boInstance.getDocument().asXML());

		  boInstance.set("boStatus", "VALIDATE");
		  boInstance = BusinessObjectDispatcher.update(boInstance);
		  log.info("#### BO Instance Schema after VALIDATE: " +boInstance.getDocument().asXML());
		  
		  boInstance.set("boStatus", "READYFORPOST");
		  boInstance = BusinessObjectDispatcher.update(boInstance);
		  log.info("#### BO Instance Schema after READYFORPOST: " +
		  boInstance.getDocument().asXML());
		  
		  boInstance.set("boStatus", "POSTED");
		  boInstance = BusinessObjectDispatcher.update(boInstance);
		  log.info("#### BO Instance Schema after POSTED: " + boInstance.getDocument().asXML());
		  System.out.println("#### BO Instance Schema after POSTED: " + boInstance.getDocument().asXML());

		return boInstance;
	}
	
	/**
	 * Method to get the LookUpValues
	 */
	private void processLookup() {
		Map<String, String> getLookUpValuesMap;
		
		HashMap<String, String> lookUpConstantmap = cmConstants.getLookUpConstanst();
		for (Map.Entry<String, String> entry : lookUpConstantmap.entrySet()) {
			if(entry.getValue().equalsIgnoreCase("CMREGION_L")) {
				getLookUpValuesMap = customHelper.getLookUpValues("REGION", entry.getValue());
				regionMap.putAll(getLookUpValuesMap);
			} else if(entry.getValue().equalsIgnoreCase("CMDEPARTEMENT_L")) {
				getLookUpValuesMap = customHelper.getLookUpValues("DEPARTEMENT", entry.getValue());
				deptMap.putAll(getLookUpValuesMap);
			} else if(entry.getValue().equalsIgnoreCase("CMARRONDISSEMENT_L")) {
				getLookUpValuesMap = customHelper.getLookUpValues("ARRONDISSEMENT", entry.getValue());
				arrondMap.putAll(getLookUpValuesMap);
			} else if(entry.getValue().equalsIgnoreCase("CMCOMMUNE_L")) {
				getLookUpValuesMap = customHelper.getLookUpValues("COMMUNE", entry.getValue());
				communeMap.putAll(getLookUpValuesMap);
			} else if(entry.getValue().equalsIgnoreCase("CMQUARTIER_L")) {
				getLookUpValuesMap = customHelper.getLookUpValues("QUARTIER", entry.getValue());
				qartierMap.putAll(getLookUpValuesMap);
			} else if(entry.getValue().equalsIgnoreCase("CMAGENCE_L")) {
				getLookUpValuesMap = customHelper.getLookUpValues("AGENCE", entry.getValue());
				agenceMap.putAll(getLookUpValuesMap);
			} else if(entry.getValue().equalsIgnoreCase("CMZONE_L")) {
				getLookUpValuesMap = customHelper.getLookUpValues("ZONE", entry.getValue());
				zoneMap.putAll(getLookUpValuesMap);
			} else if(entry.getValue().equalsIgnoreCase("CMSECTEUR_L")) {
				getLookUpValuesMap = customHelper.getLookUpValues("SECTEUR", entry.getValue());
				sectorMap.putAll(getLookUpValuesMap);
			} else if(entry.getValue().equalsIgnoreCase("CMSECTEURACTIVITES_L")) {
				getLookUpValuesMap = customHelper.getLookUpValues("SECTEURACTIVITES", entry.getValue());
				sectorActMap.putAll(getLookUpValuesMap);
			} else if(entry.getValue().equalsIgnoreCase("CMACTIVITESPRINCIPAL_L")) {
				getLookUpValuesMap = customHelper.getLookUpValues("ACTIVITESPRINCIPAL", entry.getValue());
				actPrinceMap.putAll(getLookUpValuesMap);
			} else if(entry.getValue().equalsIgnoreCase("CMATRATE_L")) {
				getLookUpValuesMap = customHelper.getLookUpValues("ATRATE", entry.getValue());
				atRateMap.putAll(getLookUpValuesMap);
			} 
		}
	}
	
	/**
	 * Method to get the getter constants
	 * 
	 * @return
	 */
	private Set<String> getHeaderConstants() {
		Set<String> headerConstanstSet = null;
		try {
			headerConstanstSet = new HashSet<String>(
					Arrays.asList(URLEncoder.encode(CmEmployerRegConstant.TYPE_D_EMPLOYEUR, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.TYPE_D_EST, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.RAISON_SOCIALE, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.NINEA, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.NINET, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.FORME_JURIDIQUE, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.DATE_IDENTIFICATION_FISCALE, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.NUMERO_REGISTER_DE_COMMERCE, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.DATE_IMM_REGISTER_DE_COMMERCE, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.DATE_OUVERTURE_EST, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.DATE_EMBAUCHE_PREMIER_SALARY, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.SECTEUR_ACTIVITIES, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.ACTIVATE_PRINCIPAL, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.TAUX_AT, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.NOMBRE_TRAVAIL_REGIME_GENERAL, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.NOMBRE_TRAVAIL_REGIME_CADRE, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.REGION, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.DÉPARTEMENT, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.ARONDISSEMENT, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.COMMUNE, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.QUARTIER, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.ADDRESS, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.TELEPHONE, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.EMAIL, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.ZONE_GEOGRAPHIQUE_CSS, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.ZONE_GEOGRAPHIQUE_IPRES, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.SECTOR_GEOGRAPHIC_CSS, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.SECTOR_GEOGRAPHIC_IPRES, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.AGENCE_CSS, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.AGENCE_IPRES, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.LAST_NAME, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.FIRST_NAME, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.DATE_DE_NAISSANCE, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.NATIONALITE, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.LEGAL_REP_NIN, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.EMPLOYEE_NIN, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.PAYS_DE_NAISSANCE, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.DATE_DE_DELIVRANCE, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.DATE_DE_EXPIRATION, CmEmployerRegConstant.UTF),
							URLEncoder.encode(CmEmployerRegConstant.MOBILE_NUM, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.TYPE_PIECE_IDENTITE, CmEmployerRegConstant.UTF),URLEncoder.encode(CmEmployerRegConstant.NUMERO_PIECE_IDENTITE, CmEmployerRegConstant.UTF)
				));
		} catch (UnsupportedEncodingException e) {
			log.error("*****Issue in Processing file***** "+e);
		}
		return headerConstanstSet;
		
	}
	
		/**
		 * Method to create To Do
		 * 
		 * @param messageParam
		 * @param nineaNumber
		 * @param messageNumber
		 * @param fileName
		 */
		private void createToDo(String messageParam, String nineaNumber, String messageNumber, String fileName) {
			startChanges();
			// BusinessService_Id businessServiceId=new
			// BusinessService_Id("F1-AddToDoEntry");
			BusinessServiceInstance businessServiceInstance = BusinessServiceInstance.create("F1-AddToDoEntry");
			Role_Id toDoRoleId = new Role_Id("CM-REGTODO");
			Role toDoRole = toDoRoleId.getEntity();
			businessServiceInstance.getFieldAndMDForPath("sendTo").setXMLValue("SNDR");
			businessServiceInstance.getFieldAndMDForPath("subject").setXMLValue("Batch Update from PSRM");
			businessServiceInstance.getFieldAndMDForPath("toDoType").setXMLValue("CM-REGTO");
			businessServiceInstance.getFieldAndMDForPath("toDoRole").setXMLValue(toDoRole.getId().getTrimmedValue());
			businessServiceInstance.getFieldAndMDForPath("drillKey1").setXMLValue("CM-REGFORMSTGULPD");
			businessServiceInstance.getFieldAndMDForPath("messageCategory").setXMLValue("90007");
			businessServiceInstance.getFieldAndMDForPath("messageNumber").setXMLValue(messageNumber);
			businessServiceInstance.getFieldAndMDForPath("messageParm1").setXMLValue(messageParam);
			businessServiceInstance.getFieldAndMDForPath("messageParm2").setXMLValue(nineaNumber);
			businessServiceInstance.getFieldAndMDForPath("messageParm3").setXMLValue(fileName);
			businessServiceInstance.getFieldAndMDForPath("sortKey1").setXMLValue("CM-REGFORMSTGULPD");
		
			BusinessServiceDispatcher.execute(businessServiceInstance);
			saveChanges();
			// getSession().commit();
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
