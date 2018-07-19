package com.splwg.cm.domain.common.businessComponent;

import com.splwg.base.api.GenericBusinessComponent;
import com.splwg.base.api.businessObject.COTSInstanceList;
import com.splwg.base.api.businessObject.COTSInstanceListNode;
import com.splwg.base.api.businessService.BusinessServiceDispatcher;
import com.splwg.base.api.businessService.BusinessServiceInstance;
import com.splwg.shared.logging.Logger;
import com.splwg.shared.logging.LoggerFactory;
import com.splwg.tax.domain.admin.idType.IdType;
import com.splwg.tax.domain.admin.personType.PersonType;
import com.splwg.tax.domain.customerinfo.person.Person;
import com.splwg.tax.domain.customerinfo.person.Person_Id;

/**
 * @author Papa
 *
 * @BusinessComponent (customizationCallable = true)
 */
public class CmPersonSearchComponent_Impl extends GenericBusinessComponent implements CmPersonSearchComponent {
	Logger logger = LoggerFactory.getLogger(CmPersonSearchComponent_Impl.class);

	public Person searchPerson(String personId) {

		// Business Service Instance
		BusinessServiceInstance bsInstance = BusinessServiceInstance.create("C1-PersonSearchById");

		// Populate BS parameters if available
		if (!isNull(personId))
			bsInstance.set("personId", personId);
		// Execute BS and return the person Id if exists
		return executeBSAndRetrievePersonById(bsInstance);
	}

	public Person searchPerson(IdType idType, String idNumber) {

		// Business Service Instance
		BusinessServiceInstance bsInstance = BusinessServiceInstance.create("C1-PersonSearchByIdTypeNumber");

		// Populate BS parameters if available
		if (!isNull(idType))
			bsInstance.set("idType", idType.getId().getTrimmedValue());
		if (!isBlankOrNull(idNumber))
			bsInstance.set("idNumber", idNumber);

		// Execute BS and return the person Id if exists
		return executeBSAndRetrievePerson(bsInstance);

	}

	public Person searchPerson(PersonType personType, String entityName, String firstName, String lastName) {

		// TODO This person Search method might have to be updated to include
		// additional search details that help identify a unique person

		// Business Service Instance
		BusinessServiceInstance bsInstance = BusinessServiceInstance.create("CM-PERSRCHQ1");

		// Populate BS parameters if available
		if (!isNull(personType))
			bsInstance.set("perTypeCd", personType.getId().getTrimmedValue());
		if (!isBlankOrNull(entityName))
			bsInstance.set("entityName", entityName);
		if (!isBlankOrNull(firstName))
			bsInstance.set("firstName", firstName);
		if (!isBlankOrNull(lastName))
			bsInstance.set("lastName", lastName);

		// Execute BS and return the person Id if exists
		return executeBSAndRetrievePerson(bsInstance);

	}

	private Person executeBSAndRetrievePerson(BusinessServiceInstance bsInstance) {

		// Executing BS
		bsInstance = BusinessServiceDispatcher.execute(bsInstance);

		logger.info(bsInstance.getDocument().asXML());

		// Getting the list of results
		COTSInstanceList list = bsInstance.getList("results");

		// If list IS NOT empty
		if (!list.isEmpty()) {

			// Get the first result
			COTSInstanceListNode firstRow = list.iterator().next();

			// Return the person entity
			return new Person_Id(firstRow.getString("personId")).getEntity();

		}

		return null;
	} 

	private Person executeBSAndRetrievePersonById(BusinessServiceInstance bsInstance) {

		// Executing BS
		bsInstance = BusinessServiceDispatcher.execute(bsInstance);

		logger.info(bsInstance.getDocument().asXML());

		// Getting the list of results
		COTSInstanceList list = bsInstance.getList("results");

		// If list IS NOT empty
		if (!list.isEmpty()) {

			// Get the first result
			COTSInstanceListNode firstRow = list.iterator().next();

			// Return the person entity
			return new Person_Id(firstRow.getString("personId")).getEntity(); 

		}

		return null;
	}
}
