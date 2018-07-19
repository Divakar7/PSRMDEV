package com.splwg.cm.domain.common.businessComponent;

import com.ibm.icu.math.BigDecimal;
import com.splwg.base.api.GenericBusinessComponent;
import com.splwg.base.api.businessObject.BusinessObjectDispatcher;
import com.splwg.base.api.businessObject.BusinessObjectInstance;
import com.splwg.base.api.businessObject.COTSInstanceList;
import com.splwg.base.api.businessObject.COTSInstanceListNode;
import com.splwg.base.api.businessObject.COTSInstanceNode;
import com.splwg.base.api.datatypes.Date;
import com.splwg.base.api.datatypes.DateFormat;
import com.splwg.base.api.serviceScript.ServiceScriptDispatcher;
import com.splwg.base.api.serviceScript.ServiceScriptInstance;
import com.splwg.shared.logging.Logger;
import com.splwg.shared.logging.LoggerFactory;
import com.splwg.tax.domain.admin.formType.FormType;
import com.splwg.tax.domain.admin.serviceType.ServiceType;

/**
 * @author Papa
 *
@BusinessComponent (customizationCallable = true)
 */
public class CmTaxRoleRegistrationComponent_Impl extends GenericBusinessComponent
		implements CmTaxRoleRegistrationComponent {

	// Configuration from Form Rule 
    ServiceType serviceType;
    
    // TaxRole Details
    private String accountIdString;
    private Date startDate;
    private Date endDate;
    private String revenueCalendarString;
    private String calculationControlString;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    
    // Form Type
    private FormType formType;
    
    public ServiceType getServiceType() {
        return serviceType;
    }


    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }


    public String getAccountIdString() {
        return accountIdString;
    }


    public void setAccountIdString(String account) {
        this.accountIdString = account;
    }

    public String getRevenueCalendarString() {
        return revenueCalendarString;
    }


    public void setRevenueCalendarString(String revenueCalendar) {
        this.revenueCalendarString = revenueCalendar;
    }

    public String getCalculationControlString() {
        return calculationControlString;
    }


    public void setCalculationControlString(String calculationControl) {
        this.calculationControlString = calculationControl;
    }

    public Date getStartDate() {
        return startDate;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }


    public void setEndtDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public FormType getFormType() {
        return formType;
    }


    public void setFormType(FormType formType) {
        this.formType = formType;
    }


    public ServiceScriptInstance createTaxRoleSS(){
        
        ServiceScriptInstance serviceScriptInstance = ServiceScriptInstance.create("C1-CrteTaxRl");
        
        COTSInstanceNode input = serviceScriptInstance.getGroup("input");
        
        input.set("accountId", accountIdString);
        input.set("taxType", serviceType.getId().getTrimmedValue());
        input.set("startDate", startDate.toString(new DateFormat("yyyy-MM-dd")));
        input.set("formType", formType.getId().getTrimmedValue());
        
        ServiceScriptDispatcher.invoke(serviceScriptInstance);
        
        return serviceScriptInstance;
        
    }

    public BusinessObjectInstance createTaxRoleBO(){
        
        BusinessObjectInstance taxRoleBoInstance = BusinessObjectInstance.create(serviceType.getRelatedTransactionBO());
        
        taxRoleBoInstance.set("taxType", serviceType.getId().getIdValue());
        taxRoleBoInstance.set("accountId", accountIdString);
        taxRoleBoInstance.set("startDate", startDate);
        taxRoleBoInstance.set("bo", serviceType.getRelatedTransactionBO());
        
        taxRoleBoInstance = BusinessObjectDispatcher.add(taxRoleBoInstance);
        
        return taxRoleBoInstance;
        
    }
    
    public BusinessObjectInstance createBillableTaxRoleBO(){
        log.info("STARTING BILLABLE TAX ROLE CREATION");
        BusinessObjectInstance taxRoleBoInstance = BusinessObjectInstance.create(serviceType.getRelatedTransactionBO());
        COTSInstanceList filingCalendarCollectionList = taxRoleBoInstance.getList("filingCalendarCollection");
        COTSInstanceListNode filingCalendarCollectioNode = filingCalendarCollectionList.newChild();
        COTSInstanceList taxRoleCalculationControlList = taxRoleBoInstance.getList("taxRoleCalculationControl");
        COTSInstanceListNode taxRoleCalculationNode = taxRoleCalculationControlList.newChild();
                
        taxRoleBoInstance.set("taxType", serviceType.getId().getIdValue());
        taxRoleBoInstance.set("accountId", accountIdString);
        taxRoleBoInstance.set("startDate", startDate);
        if(!isNull(endDate)){
            taxRoleBoInstance.set("endDate", endDate);
        }
        taxRoleBoInstance.set("bo", serviceType.getRelatedTransactionBO());
        filingCalendarCollectioNode.set("filingCalendar", revenueCalendarString);
        filingCalendarCollectioNode.set("effectiveDate", startDate);
        taxRoleCalculationNode.set("calcControl", calculationControlString);
        taxRoleCalculationNode.set("sequence", new BigDecimal("10"));
        taxRoleCalculationNode.set("effectiveDate", startDate);
        taxRoleBoInstance.set("bo", serviceType.getRelatedTransactionBO());
                
        taxRoleBoInstance = BusinessObjectDispatcher.add(taxRoleBoInstance);
        return taxRoleBoInstance;
        
    }
}
