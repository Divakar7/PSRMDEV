package com.splwg.cm.domain.testcase;

import java.math.BigInteger;
import java.util.Properties;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.math.MathContext;
import com.splwg.base.api.businessObject.BusinessObjectDispatcher;
import com.splwg.base.api.businessObject.BusinessObjectInstance;
import com.splwg.base.api.businessObject.COTSFieldDataAndMD;
import com.splwg.base.api.datatypes.Money;
import com.splwg.base.api.testers.AlgorithmImplementationTestCase;
import com.splwg.base.domain.common.algorithm.Algorithm;
import com.splwg.base.domain.common.algorithm.Algorithm_Id;
import com.splwg.cm.domain.admin.formRule.CmCreateCustomerContactAlgComp;
import com.splwg.cm.domain.admin.formRule.CmDistributionRuleCreatePaymentOnAccountAlgoComp;
import com.splwg.cm.domain.admin.formRule.CmEmployeeRegAlgo;
import com.splwg.tax.domain.admin.distributionRule.DistributionRule;
import com.splwg.tax.domain.customerinfo.account.Account_Id;
import com.splwg.tax.domain.customerinfo.person.Person;
import com.splwg.tax.domain.customerinfo.person.Person_DTO;
import com.splwg.tax.domain.customerinfo.person.Person_Id;
import com.splwg.tax.domain.payment.payment.Payment_Id;
import com.splwg.tax.domain.admin.formRule.ApplyFormRuleAlgorithmInputData;
import com.splwg.tax.domain.admin.formRule.ApplyFormRuleAlgorithmInputOutputData;
import com.splwg.tax.domain.admin.formType.FormType;
import com.splwg.tax.domain.admin.formType.FormType_Id;
import com.splwg.tax.domain.payment.paymentEvent.PaymentEvent;
import com.splwg.tax.domain.payment.paymentEvent.PaymentEvent_Id;
import com.sun.corba.se.impl.orbutil.GetPropertyAction;

import org.junit.Test;

public class AlgoTest extends AlgorithmImplementationTestCase{

	@SuppressWarnings("deprecation")
	public void testInvoke() {
			
		 Algorithm alg = new Algorithm_Id("CM-EMPLEREG").getEntity();
		
		 //ApplyFormRuleAlgorithmInputData alogInput = new .getFormRuleId().getEntity().getBusinessObject().getId().getTrimmedValue());
		 //alg.getAlgorithmComponent(arg0, arg1)
		 CmEmployeeRegAlgo cmEmployerRegAlgo = alg.getAlgorithmComponent(CmEmployeeRegAlgo.class);
		 BusinessObjectInstance busInst = BusinessObjectInstance.create("CM-DemandeServicePflwTransBO");
		 busInst.set("processFlowId", "59273964034676");
		 busInst = BusinessObjectDispatcher.read(busInst);
		 COTSFieldDataAndMD cots = busInst.getFieldAndMDForPath("employerDetails/ninea");
		 cmEmployerRegAlgo.invoke();
			/*System.out.println("***test start***");
			//Account_Id id = new Account_Id("2456607326");
			//Account tenderObligation =  id.getEntity();
		Properties  properties = new Properties();
			properties.setProperty("customerContactClass", "HDSP");
			properties.setProperty("customerContactType", "APPL");
			
		
	/*		Person_Id perId = new Person_Id("8626400536");
			String addrs = perId.getEntity().getAddress1();
			System.out.println(addrs);
			Person_DTO partyDTO = perId.getEntity().getDTO();
			partyDTO.setAddress1("32 Cook Drive");
			perId.getEntity().setDTO(partyDTO);
			getSession().commit();*/
			
			//partyDTO.newEntity();
			//getSession().commit();
		//	CmEmployeeRegAlgo.invoke();
		//startChanges();			
	}
		@Override
		protected Class getAlgorithmImplementationClass() {
			// TODO Auto-generated method stub
			return CmEmployeeRegAlgo.class;
		}

	}
