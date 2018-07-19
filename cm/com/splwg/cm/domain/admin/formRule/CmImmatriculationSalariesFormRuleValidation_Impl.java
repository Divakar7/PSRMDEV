package com.splwg.cm.domain.admin.formRule;

import java.util.Iterator;

import com.ibm.icu.math.BigDecimal;
import com.splwg.base.api.businessObject.BusinessObjectDispatcher;
import com.splwg.base.api.businessObject.BusinessObjectInstance;
import com.splwg.base.api.businessObject.BusinessObjectInstanceKey;
import com.splwg.base.api.businessObject.BusinessObjectStatusCode;
import com.splwg.base.api.businessObject.COTSInstanceListNode;
import com.splwg.base.api.businessObject.COTSInstanceNode;
import com.splwg.base.api.datatypes.Date;
import com.splwg.base.api.datatypes.EntityId;
import com.splwg.base.api.datatypes.Lookup;
import com.splwg.base.api.lookup.BusinessObjectActionLookup;
import com.splwg.base.api.lookup.BusinessObjectStatusTransitionConditionLookup;
import com.splwg.base.domain.StandardMessages;
import com.splwg.base.domain.common.businessObject.BusinessObject;
import com.splwg.base.domain.common.businessObject.BusinessObjectEnterStatusAlgorithmSpot;
import com.splwg.base.domain.common.businessObjectStatusReason.BusinessObjectStatusReason_Id;
import com.splwg.base.domain.common.maintenanceObject.MaintenanceObject;
import com.splwg.cm.api.lookup.CmAgeLookup;
//import com.splwg.cm.domain.batch.CmHelper;
//import com.splwg.cm.domain.customMessages.CmMessageRepository90003;
import com.splwg.shared.logging.Logger;
import com.splwg.shared.logging.LoggerFactory;


/**
 * @author Papa
 *
 * @AlgorithmComponent ()
 */
public class CmImmatriculationSalariesFormRuleValidation_Impl extends CmImmatriculationSalariesFormRuleValidation_Gen
		implements BusinessObjectEnterStatusAlgorithmSpot {

	Logger logger = LoggerFactory.getLogger(CmImmatriculationSalariesFormRuleValidation_Impl.class);

	private BusinessObject bo;
	private BusinessObjectInstanceKey boKey;
	private BusinessObjectInstance newBOInstance;
	private BusinessObjectInstance boInstance;
	private BusinessObjectActionLookup boAction;
	private static final String BUSINESS_OBJECT = "businessObject";
	private static final String BO_INSTANCE = "BoInstance";
	//CmHelper customHelper = new CmHelper();

	@Override
	public void setEntityId(EntityId id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaintenanceObject(MaintenanceObject mo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNewBusinessObject(BusinessObjectInstance newBOInstance) {
		this.newBOInstance = newBOInstance;

	}

	@Override
	public void setOriginalBusinessObject(BusinessObjectInstance boRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBusinessObject(BusinessObject bo) {

		this.bo = bo;
	}

	@Override
	public void setBusinessObjectKey(BusinessObjectInstanceKey boKey) {
		this.boKey = boKey;

	}

	@Override
	public void setAction(BusinessObjectActionLookup boAction) {
		this.boAction = boAction;

	}

	@Override
	public BusinessObjectStatusCode getNextStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BusinessObjectStatusTransitionConditionLookup getNextStatusCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getUseDefaultNextStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BusinessObjectStatusReason_Id getStatusChangeReasonId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getForcePostProcessing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void invoke() {
		// TODO Auto-generated method stub
		if (boAction.isDelete()) {
			return;
		}
		if (isNull(boKey)) {
			return;
		}
		// Read Usage BO
		boInstance = readUsageBO();

		/* validate the inputs */
		validateInputs();

		/* validate the BO Instance details */
		  validateBOInstanceValues();

	}

	private BusinessObjectInstance readUsageBO() {

		if (isNull(boKey))
			return null;
		BusinessObjectInstance boInstance = BusinessObjectInstance.create(bo);
		boInstance = BusinessObjectDispatcher.read(boKey, true);
		return boInstance;

	}

	/*
	 * Validates the inputs to ensure that the required parameters have been set
	 * accordingly
	 * 
	 * @return void
	 */
	private void validateInputs() {
		if (isNull(bo)) {
			addError(StandardMessages.fieldMissing(BUSINESS_OBJECT));
		}
		if (isNull(boKey)) {
			addError(StandardMessages.fieldMissing("BUS_OBJ_CD"));
		}
		if (isNull(boInstance)) {
			addError(StandardMessages.fieldMissing(BO_INSTANCE));
		}
	}

	private void validateBOInstanceValues() {

		logger.info("validateBOInstanceValues :" + boInstance);
		COTSInstanceNode group = boInstance.getGroupFromPath("employe");
		Iterator<COTSInstanceListNode> iterator = group.getList("employeList").iterator();
		int count=0;
		BigDecimal nmbreEmpRegGen =(BigDecimal) boInstance.getFieldAndMDForPath("employeur/nmbreEmpRegGen/asCurrent").getValue();
		String idTravailleur=null;
		while (iterator.hasNext()) {
			COTSInstanceListNode nextElt = iterator.next();
			if (nextElt != null) {
				BigDecimal ninEmployee = (BigDecimal) nextElt.getFieldAndMDForPath("nin/asCurrent")
						.getValue();
				Date dateFinContrat = (Date) nextElt.getFieldAndMDForPath("dateFinContrat/asCurrent")
						.getValue();
				Date dateDebutContrat = (Date) nextElt.getFieldAndMDForPath("dateDebut/asCurrent")
						.getValue();
				Date dateNaissance = (Date) nextElt.getFieldAndMDForPath("dateDeNaissance/asCurrent")
						.getValue();
				Date dateLivraison = (Date) nextElt.getFieldAndMDForPath("delivreLe/asCurrent")
						.getValue();
				Date dateExpiration = (Date) nextElt.getFieldAndMDForPath("expireLe/asCurrent")
						.getValue();
				
				String nomEmployeeDmt = (String) nextElt.getFieldAndMDForPath("nomEmploye/asCurrent")
						.getValue();
				String prenomEmployeeDmt = (String) nextElt.getFieldAndMDForPath("prenomEmploye/asCurrent")
						.getValue();
				idTravailleur = (String) nextElt.getFieldAndMDForPath("rechercheEmploye/asCurrent")
						.getValue();
				
				
				
				boolean checkValidationFlag = false ;
			}
		}
	}
}
				/* if(ninEmployee!=null){
			    	   String nin = String.valueOf(ninEmployee);
			    	   checkValidationFlag = customHelper.validateNinNumber(nin);
			    	   if(!checkValidationFlag){
			    			logger.info("Given Nin having invalid format");
			          		addError(CmMessageRepository90003.MSG_2(nin)); 
			    	   }
			       }
				
				if(dateDebutContrat!=null && dateFinContrat!=null){
					if(dateDebutContrat.compareTo(dateFinContrat)>0){
						addError(CmMessageRepository90003.MSG_3(dateDebutContrat.toString(),dateFinContrat.toString())); 
						
					}
				}
				if(dateLivraison!=null && dateExpiration!=null){
					if(dateLivraison.compareTo(dateExpiration)>0){
						addError(CmMessageRepository90003.MSG_4(dateLivraison.toString(),dateExpiration.toString())); 
						
					}
				}
				if(dateNaissance!=null){
//					Lookup lookupAge=CmAgeLookup.constants.CM_AGE;
//					int age=Integer.parseInt(lookupAge.toString());
					if((getSystemDateTime().getDate().getYear()-dateNaissance.getYear())<18){
						addError(CmMessageRepository90003.MSG_5()); 
						
					}
				}
				
				if(nomEmployeeDmt!=null){
			    	   checkValidationFlag = customHelper.validateAlphabetsOnly(prenomEmployeeDmt);
			    	   if(!checkValidationFlag){
			    			logger.info("Given Prenom having invalid format");
			          		addError(CmMessageRepository90003.MSG_6(prenomEmployeeDmt)); 
			    	   }
			       }
				if(prenomEmployeeDmt!=null){
			    	   checkValidationFlag = customHelper.validateAlphabetsOnly(nomEmployeeDmt);
			    	   if(!checkValidationFlag){
			    			logger.info("Given Nom having invalid format");
			          		addError(CmMessageRepository90003.MSG_7(nomEmployeeDmt)); 
			    	   }
			       }
				if(ninEmployee!=null && idTravailleur==null){
					String nin = String.valueOf(ninEmployee);
			    	   Boolean ok = customHelper.validateNinExist(nin);
			    	   if(ok!=null){
			    			logger.info("Nin exists");
			          		addError(CmMessageRepository90003.MSG_8(nin)); 
			    	   }
			       }
			}
			count++;
		}
		if((nmbreEmpRegGen!=null) && (count!=nmbreEmpRegGen.intValue())){  
			addError(CmMessageRepository90003.MSG_9(String.valueOf(nmbreEmpRegGen)));
		}
	}
} */
