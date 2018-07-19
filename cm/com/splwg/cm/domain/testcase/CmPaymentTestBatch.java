package com.splwg.cm.domain.testcase; 
 
import java.util.Properties;

import com.splwg.base.api.batch.SubmissionParameters;
import com.splwg.base.api.datatypes.Bool;
import com.splwg.base.api.testers.BatchJobTestCase;
import com.splwg.base.domain.batch.batchControl.BatchControl_Id;
import com.splwg.base.domain.batch.batchRun.BatchRun;
public class CmPaymentTestBatch extends BatchJobTestCase{

	@Override
	protected SubmissionParameters setupRun(SubmissionParameters arg0) {
		// TODO Auto-generated method stub 
/*		HashMap<String, HashMap<String,String>> map = new HashMap<String, HashMap<String,String>>();
		map.get(key); 
		map.put("BO", value);*/
		
		
		arg0.setBatchControlId(new BatchControl_Id("CM-IMPRE"));
		//arg0.setB atchControlId(new BatchControl_Id("CM-UPST"));
		Properties  properties = new Properties();
		properties.setProperty("maxErrors", "");
		//properties.setProperty("formType", "IMMAT_EMPL");
		//properties.setProperty("externalSourceId", "EPORTAIL");
		//properties.setProperty("typeOfDistributionRule", "PDNS");
		//properties.setProperty("typeOfReconciliation", "PFORMS");
		//properties.setProperty("typeOfPaymentBatch", "VEPO");
		//properties.setProperty("pathToMove", "D:\\PSRM\\Success\\");
		//properties.setProperty("errorFilePathToMove", "D:\\PSRM\\Failure\\");
		arg0.setExtraParameters(properties);
		arg0.setIsTracingProgramEnd(Bool.TRUE);
		arg0.setIsTracingProgramStart(Bool.TRUE);
		arg0.setIsTracingSQL(Bool.TRUE);
		arg0.setIsTracingStandardOut(Bool.TRUE);
		//private TaxRole_Id taxRoleId;
		
			/*String cancelledSiblings = "";

			StringBuffer queryString = new StringBufferAm ();
			queryString.append("FROM ServiceAgreement sa, FinancialTransaction ft ");
			queryString.append("WHERE sa.taxRoleId = :taxRoleId ");
			queryString.append("AND   sa.id = ft.serviceAgreement.id ");

			if (cancelledSiblings.length() > 0)
				queryString.append("AND   ft.siblingId NOT IN (" + cancelledSiblings + ")");

			Query<QueryResultRow> query = createQuery(queryString.toString());
			
			//query.bindId("taxRoleId", taxRoleId);

			if (query.listSize() > 0) {
				//addError(MessageRepository.assetIdCannotBeModifiedNonCancelledFTsExists(taxRoleId));

		}*/

		/*String tableName = "CMSECTEURACTIVITES_L";
			
			startChanges();			
			PreparedStatement psPreparedStatement = null;
			psPreparedStatement = createPreparedStatement("SELECT SECTEURACTIVITES, DESCR FROM "+tableName);
			psPreparedStatement.setAutoclose(false);
			Map<String,String> lookUpMap = new HashMap<String, String>();
			try {
				QueryIterator<SQLResultRow> result = psPreparedStatement.iterate();
				while(result.hasNext()) {
				System.out.println("I am In");
				SQLResultRow lookUpValue= result.next();
				System.out.println(lookUpValue.getString("REGION"));
				System.out.println(lookUpValue.getString("DESCR"));
				lookUpMap.put(lookUpValue.getString("REGION"), lookUpValue.getString("DESCR"));
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			} finally {
				psPreparedStatement.close();
				psPreparedStatement = null;
			}
			System.out.println("Map Size:: "+ lookUpMap.size());*/
		
		/*String batchCtrlSQL = "UPDATE CI_FORM_BATCH_HDR SET FORM_BATCH_HDR_ID = :FORM_BATCH_HDR_ID WHERE EXT_FORM_BATCH_ID = :EXT_FORM_BATCH_ID ";
		PreparedStatement statement = this.createPreparedStatement(batchCtrlSQL, this.getClass().getName());
		BigInteger big = BigInteger.ZERO;
		BigInteger big1 = BigInteger.ZERO;
		int bb = 20185;
		int bb1 = 20183;
		big = big.add(BigInteger.valueOf(bb));
		big1.add(BigInteger.valueOf(bb1));
		statement.bindBigInteger("FORM_BATCH_HDR_ID",  big);
		statement.bindString("EXT_FORM_BATCH_ID", "999", null);

		try {
			int result = statement.executeUpdate();
			System.out.println(result);
		} finally {
			statement.close();
		}*/
		
		
		/*stringBuilder = new StringBuilder();
		  stringBuilder.append("insert into CI_FORM_BATCH_HDR (FORM_BATCH_HDR_ID,EXT_FORM_BATCH_ID,BUS_OBJ_CD,BO_STATUS_CD,STATUS_UPD_DTTM,CRE_DTTM,TOT_PAY_AMT,TOT_FORMS_CNT,VERSION,C1_FORM_SRCE_CD,TNDR_CTL_ID,BO_DATA_AREA)");
		  stringBuilder.append("values");
		  stringBuilder.append("(:FORM_BATCH_HDR_ID,:EXT_FORM_BATCH_ID,:BUS_OBJ_CD,:BO_STATUS_CD,to_date(:STATUS_UPD_DTTM,'DD/MM/YYYY'),to_date(:CRE_DTTM,'DD/MM/YYYY'),:TOT_PAY_AMT,:TOT_FORMS_CNT,:VERSION,:C1_FORM_SRCE_CD,:TNDR_CTL_ID,:BO_DATA_AREA)");
		  psPreparedStatement = createPreparedStatement(stringBuilder.toString());
		  psPreparedStatement.setAutoclose(false);
		  psPreparedStatement.bindString("FORM_BATCH_HDR_ID","202825",null);
		  psPreparedStatement.bindString("EXT_FORM_BATCH_ID", "1000", null);
		  psPreparedStatement.bindString("BUS_OBJ_CD","C1-StandardFormBatchHeader",null);
		  psPreparedStatement.bindString("BO_STATUS_CD","PENDING",null);
		  psPreparedStatement.bindString("STATUS_UPD_DTTM","25/04/2018",null);
		  psPreparedStatement.bindString("CRE_DTTM","25/04/2018",null);
		  psPreparedStatement.bindString("TOT_PAY_AMT","0",null);
		  psPreparedStatement.bindString("TOT_FORMS_CNT","1",null);
		  psPreparedStatement.bindString("VERSION","5",null);
		  psPreparedStatement.bindString("C1_FORM_SRCE_CD","BANK-A",null);
		  psPreparedStatement.bindString("TNDR_CTL_ID","1835131907",null);
		  psPreparedStatement.bindString("BO_DATA_AREA","suspenseIssueList/><validFormTypes><formType>CM-REGFORM</formType></validFormTypes>",null);
		  
		  try {
			// boolean success =  psPreparedStatement.execute();
		   int result = psPreparedStatement.executeUpdate();
		   System.out.println(result);
		   
		  } catch (Exception exception) {
		   System.out.println("Unable to get Lookup value for the Description:: "+exception.getMessage());
		   exception.printStackTrace();
		  } finally {
		   psPreparedStatement.close();
		   psPreparedStatement = null;
		  }*/
		
		/*PreparedStatement psPreparedStatement = null;
		StringBuilder stringBuilder = null;
		String ninea = "784728347";
		
		stringBuilder = new StringBuilder();
		stringBuilder.append("select * from CI_PROC_FLOW where PROC_FLOW_TYPE_CD = 'CM-DEMANDE-SERVICE-PFLOWTYPE' and  BO_STATUS_CD = 'ACFIE'");
		
		psPreparedStatement = createPreparedStatement(stringBuilder.toString());
		psPreparedStatement.setAutoclose(false);
		psPreparedStatement.bindString("FORM_BATCH_HDR_ID", "240418", null);
		
		
		try {
			QueryIterator<SQLResultRow> result = psPreparedStatement.iterate();
			while(result.hasNext()) {
				System.out.println("I am IN");
				SQLResultRow sqlRoq= result.next();
				System.out.println(sqlRoq.getString("EXT_FORM_BATCH_ID"));
				System.out.println(sqlRoq.getString("BUS_OBJ_CD"));
			}
			//lookUpValue = result.getString("PER_ID1");
			//lookUpValue = result.getString("PER_ID2");
		} catch (Exception exception) {
			System.out.println("Unable to get Lookup value for the Description:: "+exception.getMessage());
			exception.printStackTrace();
		} finally {
			psPreparedStatement.close();
			psPreparedStatement = null;
		}
		*/
		
		//boolean isValidEmail = EmailValidator.getInstance().isValid("pape@catalyst-us.com");
		//System.out.println(isValidEmail);
		/*PreparedStatement psPreparedStatement = null;
		StringBuilder stringBuilder = null;
		
		stringBuilder = new StringBuilder();
		stringBuilder.append("select BUS_OBJ_CD, F1_EXT_LOOKUP_VALUE, DESCR from F1_EXT_LOOKUP_VAL_L where LANGUAGE_CD = 'ENG' and")
		.append(" UPPER(DESCR) = UPPER(:DESCR) AND BUS_OBJ_CD =:BUS_OBJ_CD ");
				
		psPreparedStatement = createPreparedStatement(stringBuilder.toString());
		psPreparedStatement.setAutoclose(false);
		psPreparedStatement.bindString("DESCR", "Projet de d�veloppement", null);
		psPreparedStatement.bindEntity("BUS_OBJ_CD", new BusinessObject_Id("CmLegalStatus").getEntity());
		
		String lookUpValue = "";
		try {
			SQLResultRow result = psPreparedStatement.firstRow();
			lookUpValue = result.getString("F1_EXT_LOOKUP_VALUE");
			System.out.println("lookUpValue:: " + lookUpValue);
		} catch (Exception exception) {
			System.out.println("Unable to get Lookup value for the Description:: "+exception.getMessage());
			exception.printStackTrace();
		} finally {
			psPreparedStatement.close();
			psPreparedStatement = null;
		}*/
		
		return arg0;
	}
	
	
	

	@Override
	protected void validateResults(BatchRun arg0) {
		// TODO Auto-generated method stub
		
	}

}
