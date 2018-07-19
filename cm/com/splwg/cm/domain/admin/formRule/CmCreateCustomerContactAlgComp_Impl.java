package com.splwg.cm.domain.admin.formRule;

import com.ibm.icu.math.BigDecimal;
import com.splwg.base.api.businessObject.BusinessObjectDispatcher;
import com.splwg.base.api.businessObject.BusinessObjectInstance;
import com.splwg.base.api.expression.type.ExpressionValue;
import com.splwg.base.api.lookup.LogEntryTypeLookup;
import com.splwg.base.api.maintenanceObject.MaintenanceObjectLogHelper;
import com.splwg.base.domain.common.businessObject.BusinessObject;
import com.splwg.base.domain.common.characteristicType.CharacteristicType;
import com.splwg.base.domain.common.characteristicType.CharacteristicType_Id;
import com.splwg.base.domain.common.message.MessageCategory_Id;
import com.splwg.base.domain.common.message.MessageParameters;
import com.splwg.base.domain.common.message.ServerMessageFactory;
import com.splwg.base.domain.common.message.ServerMessageFactory.Factory;
import com.splwg.base.support.schema.BusinessObjectInfo;
import com.splwg.base.support.schema.BusinessObjectInfoCache;
import com.splwg.shared.common.ServerMessage;
import com.splwg.shared.common.StringUtilities;
import com.splwg.tax.api.lookup.FormSubTypeLookup;
import com.splwg.tax.domain.admin.customerContactClass.CustomerContactClass;
import com.splwg.tax.domain.admin.customerContactType.CustomerContactType;
import com.splwg.tax.domain.admin.customerContactType.CustomerContactType_Id;
import com.splwg.tax.domain.admin.formRule.ApplyFormRuleAlgorithmInputData;
import com.splwg.tax.domain.admin.formRule.ApplyFormRuleAlgorithmInputOutputData;
import com.splwg.tax.domain.admin.formRule.CreateCustomerContactApplyRuleAlgComp_Gen;
import com.splwg.tax.domain.admin.formRule.CreateCustomerContactApplyRuleAlgComp_Impl;
import com.splwg.tax.domain.admin.formRule.FormRule;
import com.splwg.tax.domain.admin.formRule.FormRuleBORuleProcessingAlgorithmSpot;
import com.splwg.tax.domain.admin.formRule.MessageRepository;
import com.splwg.tax.domain.admin.formType.FormType;
import com.splwg.tax.domain.customerinfo.customerContact.CustomerContact;
import com.splwg.tax.domain.customerinfo.customerContact.CustomerContact_Id;
import com.splwg.tax.domain.customerinfo.person.Person;
import com.splwg.tax.domain.customerinfo.person.Person_Id;
import com.splwg.tax.domain.forms.FormsMessages;
import com.splwg.tax.domain.forms.registrationForm.RegistrationForm;
import com.splwg.tax.domain.forms.registrationForm.RegistrationFormLog;
import com.splwg.tax.domain.forms.taxForm.TaxForm;
import com.splwg.tax.domain.forms.taxForm.TaxFormLog;
import com.splwg.tax.forms.FormBOInstance;
import com.splwg.tax.forms.LinesResult;
import java.math.BigInteger;


/**
 * @author Divakar
 *
@AlgorithmComponent (softParameters = { @AlgorithmSoftParameter (name = customerContactClass, type = string)
 *            , @AlgorithmSoftParameter (name = customerContactType, type = string)})
 */
public class CmCreateCustomerContactAlgComp_Impl extends CmCreateCustomerContactAlgComp_Gen
		implements FormRuleBORuleProcessingAlgorithmSpot {

	 
	private static final String FORM_BO_ELEM_TAX_FORM_ID = "taxFormId";
    private static final String FORM_BO_ELEM_REG_FORM_ID = "registrationFormId";

    private static final String TAX_FORM_BO_ELEM_TAXPAYER_PERSON_ID = "taxpayerPersonID";
    private static final String FORM_RULE_SECTION_REFERENCE = "conditionalFormLineRefSecName";
    private static final String FORM_RULE_LINE_REFERENCE = "conditionalFormLineRefLineName";
    private static final String CUSTOMER_CONTACT_CLASS = "customerContactClass";
    private static final String CUSTOMER_CONTACT_TYPE = "customerContactType";
    private static final String CUSTOMER_CONT_LOG_CHAR_TYPE = "C1-CUSCN";

    private static final String BO_ELEM_CUST_CONTACT_TYPE = "customerContactType";
    private static final String BO_ELEM_CUST_CONTACT_CLASS = "customerContactClass";
    private static final String BO_ELEM_PERSON_ID = "personId";
    private static final String BO_ELEM_CUST_CONTACT_ID = "customerContactId";

    private ApplyFormRuleAlgorithmInputData inputData;
    private ApplyFormRuleAlgorithmInputOutputData inputOutputData;
    private BusinessObjectInstance formBoInstance;
    private TaxForm taxForm;
    private RegistrationForm registrationForm;
    private CustomerContactClass customerContactClass;
    private CustomerContactType_Id ccTypeId;
    private FormSubTypeLookup formSubType;
    private String customerContactType;
    private String conditionalFormLineReferenceSection;
    private String conditionalFormLineReferenceLine;
    

    @Override
    public ApplyFormRuleAlgorithmInputOutputData getApplyFormRuleAlgorithmInputOutputData() {
        return inputOutputData;
    }

    @Override
    public void setApplyFormRuleAlgorithmInputData(ApplyFormRuleAlgorithmInputData applyFormRuleAlgorithmInputData) {
        inputData = applyFormRuleAlgorithmInputData;
    }

    @Override
    public void setApplyFormRuleAlgorithmInputOutputData(
            ApplyFormRuleAlgorithmInputOutputData applyFormRuleAlgorithmInputOutputData) {
        inputOutputData = applyFormRuleAlgorithmInputOutputData;
    }

    @Override
    public void invoke() {

        getRuleDetails();
        formBoInstance = (BusinessObjectInstance) inputOutputData.getFormBusinessObject();

        Boolean isValid = shouldCreateCustomerContact();

        if (!isValid)
            return;

        formSubType = inputData.getFormTypeId().getEntity().getFormSubType();

        if (formSubType.isTaxFormType())
            taxForm = formBoInstance.getEntity(FORM_BO_ELEM_TAX_FORM_ID, TaxForm.class);
        else
            registrationForm = formBoInstance.getEntity(FORM_BO_ELEM_REG_FORM_ID, RegistrationForm.class);

        Person person = formBoInstance.getEntity(TAX_FORM_BO_ELEM_TAXPAYER_PERSON_ID, Person.class);

        if (isNull(person)) {
            if (formSubType.isTaxFormType())
                addError(MessageRepository.personNotLinkedToTaxForm(taxForm.getId()));
            else
                addError(MessageRepository.personNotLinkedToRegForm(registrationForm.getId()));
        }

        //Create Customer Contact
        CustomerContact custContact = createCustomerContact(ccTypeId.getEntity(), person.getId());

        //Add form log
        createFormLog(custContact);

    }	

    private boolean shouldCreateCustomerContact() {

        Boolean createCustomerContact = Boolean.FALSE;

        if (StringUtilities.isBlankOrNull(conditionalFormLineReferenceSection)
                && StringUtilities.isBlankOrNull(conditionalFormLineReferenceLine))
            createCustomerContact = Boolean.TRUE;
        else {

            FormBOInstance formInstance = new FormBOInstance(inputData.getFormTypeId(), inputOutputData
                    .getFormBusinessObject().getDocument());

            LinesResult inputLineResult = formInstance.getLineValues(conditionalFormLineReferenceSection,
                    conditionalFormLineReferenceLine, true);
            if (inputLineResult.getLines().isEmpty()) {
                createCustomerContact = Boolean.FALSE;
            } else {

                ExpressionValue value = inputLineResult.getSingleLine().getNullSafeCurrentValue();
                if (value.getType().isBoolean() && value.getValue().equals(Boolean.TRUE))
                    createCustomerContact = Boolean.TRUE;

                if (value.getType().isNumber()) {

                    BigDecimal lineValue = (BigDecimal) value.getValue();
                    if (lineValue.compareTo(BigDecimal.ZERO) > 0)
                        createCustomerContact = Boolean.TRUE;
                }

                if (value.getType().isString() && notBlank(value.getValue().toString()))
                    createCustomerContact = Boolean.TRUE;
            }
        }

        return createCustomerContact;

    }
    
    private void getRuleDetails() {

    	 String getCustClass =  getCustomerContactClass();
         String getCustType = getCustomerContactType();
        BusinessObjectInfo boInfo = BusinessObjectInfoCache.getRequiredBusinessObjectInfo(inputData.getFormRuleId()
                .getEntity().getBusinessObject().getId().getTrimmedValue());
        BusinessObjectInstance boInstance = BusinessObjectDispatcher.readAsBOInstance(boInfo, inputData.getFormRuleId()
                .getEntity());

        customerContactClass = boInstance.getEntity(getCustClass, CustomerContactClass.class);
        customerContactType = boInstance.getString(getCustType);
        conditionalFormLineReferenceSection = boInstance.getString(FORM_RULE_SECTION_REFERENCE);
        conditionalFormLineReferenceLine = boInstance.getString(FORM_RULE_LINE_REFERENCE);

        if (isNull(customerContactClass))
            addError(MessageRepository.missingCustomerContactClass());

        if (isBlankOrNull(customerContactType))
            addError(MessageRepository.missingCustomerContactType());

        ccTypeId = new CustomerContactType_Id(customerContactClass.getId(), customerContactType);
        if (isNull(ccTypeId.getEntity()))
            addError(MessageRepository.invalidCustomerContactTypeForCCClass(customerContactType, customerContactClass
                    .getId().getIdValue()));

    }

    private CustomerContact createCustomerContact(CustomerContactType contactType,
            Person_Id personId) {

        BusinessObject customerContactBo = contactType.fetchRelatedTransactionBO();
        if (isNull(customerContactBo)) {
            addError(MessageRepository.customerContactTypeRelatedTransactionBoMissing(contactType));
        }

        BusinessObjectInstance custContactInstance = BusinessObjectInstance.create(customerContactBo);

        custContactInstance.set(BO_ELEM_CUST_CONTACT_TYPE, contactType.fetchIdContactType());
        custContactInstance.set(BO_ELEM_CUST_CONTACT_CLASS, contactType.fetchIdContactClass().getId().getIdValue());
        custContactInstance.set(BO_ELEM_PERSON_ID, personId.getIdValue());

        custContactInstance = BusinessObjectDispatcher.add(custContactInstance);
        CustomerContact_Id custContactId = new CustomerContact_Id(
                custContactInstance.getString(BO_ELEM_CUST_CONTACT_ID));

        return custContactId.getEntity();

    }

    private void createFormLog(CustomerContact customerContact) {

        ServerMessageFactory smf = ServerMessageFactory.Factory.newInstance();
        MessageParameters parameters = new MessageParameters();
        ServerMessage sm = smf.createMessage(
                new MessageCategory_Id(BigInteger.valueOf(FormsMessages.MESSAGE_CATEGORY)),
                FormsMessages.CUSTOMER_CONTACT_CREATED,
                parameters);
        CharacteristicType charType = new CharacteristicType_Id(CUSTOMER_CONT_LOG_CHAR_TYPE).getEntity();

        if (formSubType.isTaxFormType()) {
            MaintenanceObjectLogHelper<TaxForm, TaxFormLog> logHelperNew = new MaintenanceObjectLogHelper<TaxForm, TaxFormLog>(
                    taxForm.getBusinessObject().getMaintenanceObject(), taxForm);
            logHelperNew.addLogEntry(LogEntryTypeLookup.constants.CREATED, sm, null, charType, customerContact);
        }
        else {
            MaintenanceObjectLogHelper<RegistrationForm, RegistrationFormLog> logHelperNew = new MaintenanceObjectLogHelper<RegistrationForm, RegistrationFormLog>(
                    registrationForm.getBusinessObject().getMaintenanceObject(), registrationForm);
            logHelperNew.addLogEntry(LogEntryTypeLookup.constants.CREATED, sm, null, charType, customerContact);
        }

    }

}