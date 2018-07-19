package com.splwg.cm.domain.common.businessComponent;

import com.splwg.base.api.GenericBusinessComponent;
import com.splwg.base.api.businessObject.BusinessObjectDispatcher;
import com.splwg.base.api.businessObject.BusinessObjectInstance;
import com.splwg.base.api.businessObject.COTSInstanceListNode;
import com.splwg.base.api.datatypes.Bool;
import com.splwg.base.api.datatypes.Lookup;
import com.splwg.base.domain.common.businessObject.BusinessObject_Id;
import com.splwg.base.domain.common.installation.InstallationHelper;
import com.splwg.base.domain.security.accessGroup.AccessGroup;
import com.splwg.tax.domain.admin.accountRelationshipType.AccountRelationshipType;
import com.splwg.tax.domain.admin.customerClass.CustomerClass;

/**
 * @author Papa
 *
 * @BusinessComponent (customizationCallable = true)
 */
public class CmAccountRegistrationComponent_Impl extends GenericBusinessComponent
		implements CmAccountRegistrationComponent {

	private CustomerClass accountType;
	private AccessGroup accessGroup;
	private AccountRelationshipType accountRelationshipType;
	private Bool mainCustomerSwitch;
	private Bool financialResponsibleSwitch;
	private Bool canReceiveNotificationSwitch;
	private Bool canReceiveCopyOfBillSwitch;
	private Lookup billAddressSource;
	private String personIdString;

	public CustomerClass getAccountType() {
		return accountType;
	}

	public void setAccountType(CustomerClass accountType) {
		this.accountType = accountType;
	}

	public AccessGroup getAccessGroup() {
		return accessGroup;
	}

	public void setAccessGroup(AccessGroup accessGroup) {
		this.accessGroup = accessGroup;
	}

	public AccountRelationshipType getAccountRelationshipType() {
		return accountRelationshipType;
	}

	public void setAccountRelationshipType(AccountRelationshipType accountRelationshipType) {
		this.accountRelationshipType = accountRelationshipType;
	}

	public Bool getMainCustomerSwitch() {
		return mainCustomerSwitch;
	}

	public void setMainCustomerSwitch(Bool mainCustomerSwitch) {
		this.mainCustomerSwitch = mainCustomerSwitch;
	}

	public Bool getFinancialResponsibleSwitch() {
		return financialResponsibleSwitch;
	}

	public void setFinancialResponsibleSwitch(Bool financialResponsibleSwitch) {
		this.financialResponsibleSwitch = financialResponsibleSwitch;
	}

	public Bool getCanReceiveNotificationSwitch() {
		return canReceiveNotificationSwitch;
	}

	public void setCanReceiveNotificationSwitch(Bool canReceiveNotificationSwitch) {
		this.canReceiveNotificationSwitch = canReceiveNotificationSwitch;
	}

	public Bool getCanReceiveCopyOfBillSwitch() {
		return canReceiveCopyOfBillSwitch;
	}

	public void setCanReceiveCopyOfBillSwitch(Bool canReceiveCopyOfBillSwitch) {
		this.canReceiveCopyOfBillSwitch = canReceiveCopyOfBillSwitch;
	}

	public Lookup getBillAddressSource() {
		return billAddressSource;
	}

	public void setBillAddressSource(Lookup billAddressSource) {
		this.billAddressSource = billAddressSource;
	}

	public String getPersonIdString() {
		return personIdString;
	}

	public void setPersonIdString(String personIdString) {
		this.personIdString = personIdString;
	}

	public BusinessObjectInstance createAccount() {

		BusinessObjectInstance accountBoInstance = null;

		// Retrieve the Related Transaction BO from Account Type
		BusinessObject_Id relatedTransactionBOId = accountType.getRelatedTransactionBOId();

		// Transaction BO must exist in order to create the Person

		if (notNull(relatedTransactionBOId)) {

			// Creating Account BO Instance
			accountBoInstance = BusinessObjectInstance.create(relatedTransactionBOId.getEntity());

			// Set Account Main Information
			accountBoInstance.set("accountType", accountType.getId().getTrimmedValue());
			accountBoInstance.set("currency", InstallationHelper.getCurrencyCode());
			accountBoInstance.set("accessgroup", accessGroup.getId().getTrimmedValue());

			// Linking Person With Account
			COTSInstanceListNode accountPerson = accountBoInstance.getList("accountPersonList").newChild();
			accountPerson.set("person", personIdString);
			accountPerson.set("accountRelationship", accountRelationshipType.getId().getTrimmedValue());
			accountPerson.set("mainCustomer", mainCustomerSwitch);
			accountPerson.set("isFinanciallyResponsible", financialResponsibleSwitch);
			accountPerson.set("canReceiveNotification", canReceiveNotificationSwitch);
			accountPerson.set("canRecieveCopyOfBill", canReceiveCopyOfBillSwitch);
			accountPerson.set("billAddressSource", billAddressSource);
			accountPerson.set("billRouteType", " ");

			// Invoke BO for Account Creation
			accountBoInstance = BusinessObjectDispatcher.add(accountBoInstance);

		} else {
			// TODO Throw Error Due Account Type Not Having a Related
			// Transaction BO
			addError(null);
		}

		return accountBoInstance;

	}

}
