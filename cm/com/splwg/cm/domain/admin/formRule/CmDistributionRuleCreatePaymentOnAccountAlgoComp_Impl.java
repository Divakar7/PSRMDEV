package com.splwg.cm.domain.admin.formRule;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ibm.icu.math.BigDecimal;
import com.splwg.base.api.QueryIterator;
import com.splwg.base.api.businessObject.COTSInstanceNode;
import com.splwg.base.api.businessService.BusinessServiceDispatcher;
import com.splwg.base.api.businessService.BusinessServiceInstance;
import com.splwg.base.api.datatypes.Bool;
import com.splwg.base.api.datatypes.Date;
import com.splwg.base.api.datatypes.Money;
import com.splwg.base.api.sql.PreparedStatement;
import com.splwg.base.api.sql.SQLResultRow;
import com.splwg.base.domain.common.characteristicType.CharacteristicType;
import com.splwg.shared.logging.Logger;
import com.splwg.shared.logging.LoggerFactory;
import com.splwg.tax.api.lookup.MatchEventStatusLookup;
import com.splwg.tax.api.lookup.PaymentStatusLookup;
import com.splwg.tax.domain.adjustment.adjustment.Adjustment_Id;
import com.splwg.tax.domain.admin.distributionRule.DistributionRule;
import com.splwg.tax.domain.admin.distributionRule.DistributionRuleCreatePaymentAlgorithmSpot;
import com.splwg.tax.domain.admin.generalLedgerDistributionCode.GeneralLedgerDistributionCode;
import com.splwg.tax.domain.admin.matchType.MatchType;
import com.splwg.tax.domain.customerinfo.account.Account;
import com.splwg.tax.domain.customerinfo.account.Account_Id;
import com.splwg.tax.domain.customerinfo.serviceAgreement.ServiceAgreement;
import com.splwg.tax.domain.customerinfo.serviceAgreement.ServiceAgreement_Id;
import com.splwg.tax.domain.financial.financialTransaction.FinancialTransaction_DTO;
import com.splwg.tax.domain.financial.matchEvent.MatchEvent;
import com.splwg.tax.domain.financial.matchEvent.MatchEvent_DTO;
import com.splwg.tax.domain.financial.matchEvent.MatchEvent_Id;
import com.splwg.tax.domain.financial.matchEvent.MatchEvent_Per;
import com.splwg.tax.domain.payment.payment.CreateDistributeFreezePayment;
import com.splwg.tax.domain.payment.payment.CreateDistributeFreezePayment.Factory;
import com.splwg.tax.domain.payment.payment.Payment;
import com.splwg.tax.domain.payment.payment.PaymentSegment;
import com.splwg.tax.domain.payment.payment.PaymentSegment_DTO;
import com.splwg.tax.domain.payment.payment.Payment_DTO;
import com.splwg.tax.domain.payment.payment.Payment_Id;
import com.splwg.tax.domain.payment.paymentEvent.PaymentEvent;
import com.splwg.tax.domain.payment.paymentEvent.PaymentEvent_Id;


/**
 * @author Divakar
 *
@AlgorithmComponent (softParameters = { @AlgorithmSoftParameter (name = accountType1, type = string)
 *            , @AlgorithmSoftParameter (name = accountType2, type = string)
 *            , @AlgorithmSoftParameter (name = accountType3, type = string)
 *            , @AlgorithmSoftParameter (name = obligationType1, type = string)
 *            , @AlgorithmSoftParameter (name = obligationType2, type = string)
 *            , @AlgorithmSoftParameter (name = obligationType3, type = string)
 *            , @AlgorithmSoftParameter (name = obligationType4, type = string)
 *            , @AlgorithmSoftParameter (name = adjustmentType1, type = string)
 *            , @AlgorithmSoftParameter (name = adjustmentType2, type = string)
 *            , @AlgorithmSoftParameter (name = adjustmentType3, type = string)
 *            , @AlgorithmSoftParameter (name = adjustmentType4, type = string)
 *            , @AlgorithmSoftParameter (name = adjustmentType5, type = string)
 *            , @AlgorithmSoftParameter (name = adjustmentType6, type = string)
 *            , @AlgorithmSoftParameter (name = adjustmentType7, type = string)})
 */
public class CmDistributionRuleCreatePaymentOnAccountAlgoComp_Impl extends
		CmDistributionRuleCreatePaymentOnAccountAlgoComp_Gen implements DistributionRuleCreatePaymentAlgorithmSpot {

	private static final Logger logger = LoggerFactory.getLogger(CmDistributionRuleCreatePaymentOnAccountAlgoComp_Impl.class);

	private PaymentEvent paymentEvent;
	private DistributionRule distributionRule;
	private Money amount;
	private String characteristicValueFk1;
	private BigInteger sequence;
	private Payment_Id paymentId;
	PreparedStatement psPreparedStatement = null;
	public String accId = null;
	
	@Override
	public void invoke() {
		
		logger.info("characteristicFK: " + this.characteristicValueFk1);
		System.out.println("characteristicFK: " + this.characteristicValueFk1);

		logger.info("Amount: " + this.amount);
		System.out.println("Amount: " + this.amount);		

		logger.info("Sequence: " + this.sequence);
		System.out.println("Sequence: " + this.sequence);		

		logger.info("paymentEvent: " + this.paymentEvent);
		System.out.println("paymentEvent: " + this.paymentEvent);//12321324
		
		MatchEvent_Per match = new MatchEvent_Per();
		MatchEvent_Id matchEvent_Id = new MatchEvent_Id("245660701256");
		match.setId(matchEvent_Id);
		match.setAccount(new Account_Id("4062146587").getEntity());
		match.setIsDisputed(Bool.FALSE);
		match.setMatchEventStatus(MatchEventStatusLookup.constants.OPEN);
		match.setCreatedDate(getSystemDateTime().getDate());
		
		LinkedHashMap<HashMap<String, Money>, HashMap<String, HashMap<List<String>, List<Money>>>> debtOblMap = getDebtObligation(
				this.characteristicValueFk1);// 1000
		
		logger.info("debtOblMap: " + debtOblMap.size());
		System.out.println("debtOblMap: " + debtOblMap.size());
		ServiceAgreement debtObligation = null;
		Money debtMoney = Money.ZERO;
		Money moneyValue = Money.ZERO;
		String periodValue = null;
		
		if(!debtOblMap.isEmpty()) {
			for(Map.Entry<HashMap<String, Money>, HashMap<String, HashMap<List<String>,List<Money>>>> debtMapObj : debtOblMap.entrySet()){
				HashMap<String,Money> moneyMapkey = debtMapObj.getKey();
				HashMap<String, HashMap<List<String>,List<Money>>> moneyMap = debtMapObj.getValue();
				for(Map.Entry<String, Money> moneyMapObj : moneyMapkey.entrySet() ){
					Money moneyMapList = moneyMapObj.getValue();
					moneyValue = moneyMapList.add(moneyValue);
				}
				
				logger.info("Sum of Obligation Amount:: " + moneyValue);
				System.out.println("Sum of Obligation Amount:: " + moneyValue);
				
				if(!moneyValue.isZero() && this.amount.isGreaterThan(moneyValue)){ 
					logger.info("###Input Amount is greater than sum of obligation amount.Creating payment for equal distribution##" );
					System.out.println("###Input Amount is greater than sum of obligation amount.Creating payment for equal distribution##" );
					HashMap<String,Money> moneyMapValue = debtMapObj.getKey();
					for (Map.Entry<String, Money> moneyEntry : moneyMapValue.entrySet()) {
						ServiceAgreement_Id sa_id = new ServiceAgreement_Id(moneyEntry.getKey());
						logger.info("ServiceAgreement_Id: " + sa_id);
						System.out.println("ServiceAgreement_Id, : " + sa_id);
						debtObligation = (ServiceAgreement) sa_id.getEntity();
						logger.info("ServiceAgreement: " + debtObligation);
						System.out.println("ServiceAgreement: " + debtObligation);
						debtMoney = moneyEntry.getValue();
						System.out.println("DebtMoney: " + debtMoney);
						logger.info("Amount before the payment creation:: " + this.amount);
						System.out.println("Amount before the payment creation:: " + this.amount);
						if (!this.amount.isZero() && this.amount.isPositive()) {
							this.createFrozenPayment(debtObligation, debtMoney);
						}
			        }
					
					if(!this.amount.isZero() && this.amount.isPositive()){
						logger.info("********Creating OverPayment Adjustment*******");
						String obligationId = createObligation(this.characteristicValueFk1,"DOR", getObligationType4());
						String adjustmentId = createAdjustment(obligationId,  getAdjustmentType7(), this.amount.toString(), "OVERPAY", getSystemDateTime().getDate());
						logger.info("OverPayment Created against the Adjustment ID: " + adjustmentId);
					}
						
				} else {
					for(Entry<String, HashMap<List<String>, List<Money>>> moneyMapObj : moneyMap.entrySet()){
						  periodValue = moneyMapObj.getKey(); 
						  HashMap<List<String>,List<Money>>  finalMoneyMap = moneyMapObj.getValue();
						  Money monthObligationMoney = Money.ZERO;
						  for(Map.Entry<List<String>, List<Money>> moneyEntry : finalMoneyMap.entrySet()){
							  List<String> obligIdList = moneyEntry.getKey();
							  if(!isNull(moneyEntry) && moneyEntry.getValue().size()>=1){ 
								  List<Money> moneyList = moneyEntry.getValue();
								 	for(int i=0;i<moneyList.size();i++){
								 		monthObligationMoney = moneyList.get(i).add(monthObligationMoney);
								 	}
								 	if(!monthObligationMoney.isZero() && this.amount.isLessThanOrEqual(monthObligationMoney)){
								 		logger.info("###Creating payment for same month obligations####" );
										System.out.println("###Creating payment for same month obligations####" );
								 		for(int i=0;i<moneyList.size();i++){
									 		Money obligationMoney = moneyList.get(i);
									 		String oblStr = obligIdList.get(i);
									 		int prorateMoney = Math.round(this.amount.getAmount().floatValue()/monthObligationMoney.getAmount().floatValue()*obligationMoney.getAmount().floatValue());
									 		debtMoney = new Money(String.valueOf(prorateMoney));
									 		ServiceAgreement_Id sa_id = new ServiceAgreement_Id(oblStr);
											logger.info("ServiceAgreement_Id: " + sa_id);
											System.out.println("ServiceAgreement_Id: " + sa_id);
											debtObligation = (ServiceAgreement) sa_id.getEntity();
											logger.info("ServiceAgreement: " + debtObligation);
											System.out.println("ServiceAgreement: " + debtObligation);
											System.out.println("DebtMoney: " + debtMoney);
											logger.info("Amount before the payment creation:: " + this.amount);
											System.out.println("Amount before the payment creation:: " + this.amount);
											if (!this.amount.isZero() && this.amount.isPositive()) {
												this.createFrozenPayment(debtObligation, debtMoney);
											}
									 		
									 	}
								 		
								 	} else { 
								 		logger.info("###Creating payment for sequence month obligations####" );
										System.out.println("###Creating payment for sequence month obligations####" );
								 		for (Map.Entry<List<String>,List<Money>> moneyEntryy : finalMoneyMap.entrySet()) {
								 			List<String> obligIdListt = moneyEntryy.getKey();
								 			List<Money> moneyListt = moneyEntry.getValue();
								 			for(int i=0;i<obligIdListt.size();i++){
								 				ServiceAgreement_Id sa_id = new ServiceAgreement_Id(obligIdListt.get(i));
												logger.info("ServiceAgreement_Id: " + sa_id);
												System.out.println("ServiceAgreement_Id: " + sa_id);
												debtObligation = (ServiceAgreement) sa_id.getEntity();
												logger.info("ServiceAgreement: " + debtObligation);
												System.out.println("ServiceAgreement: " + debtObligation);
												debtMoney = moneyListt.get(i);
												System.out.println("DebtMoney: " + debtMoney);
												logger.info("Amount before the payment creation :: " + this.amount);
												System.out.println("Amount before the payment creation:: " + this.amount);
												if (!this.amount.isZero() && this.amount.isPositive()) {
													this.createFrozenPayment(debtObligation, debtMoney);
												}

										}
									}
								}

							}

						}
					}
				}

			}
			
		} else {
			logger.info("There is oblogation to pay");
		}
		
	}

	/**
	 * @param obligation
	 * @param money
	 */
	private void createFrozenPayment(ServiceAgreement obligation, Money money) {

		logger.info("Money: " + money);
		System.out.println("Money: " + money);
		
		logger.info("Money String: " + String.valueOf(money));
		System.out.println("Money String: " + String.valueOf(money));
	
		Payment_DTO paymentDTO = (Payment_DTO) this.createDTO(Payment.class);
		paymentDTO.setAccountId(obligation.getAccount().getId());
		PaymentSegment_DTO paymentSegmentDTO = (PaymentSegment_DTO) this.createDTO(PaymentSegment.class);
		if(this.amount.isLessThanOrEqual(money)) { //money = 4000, amount-screen = 5000
			paymentDTO.setPaymentAmount(this.amount);
			paymentSegmentDTO.setPaySegmentAmount(this.amount);
			this.amount = this.amount.subtract(money);
		} else if(this.amount.isGreaterThan(money)){
			this.amount = this.amount.subtract(money);
			paymentDTO.setPaymentAmount(money);
			paymentSegmentDTO.setPaySegmentAmount(money);
		}
		paymentDTO.setCurrencyId(obligation.getAccount().getCurrency().getId());
		paymentDTO.setSequence(this.sequence);
		paymentDTO.setPaymentEventId(new PaymentEvent_Id("406214677912"));//this.paymentEvent.getId());
		paymentDTO.setPaymentStatus(PaymentStatusLookup.constants.FREEZABLE);
		paymentSegmentDTO.setServiceAgreementId(obligation.getId());
		paymentSegmentDTO.setCurrencyId(obligation.getAccount().getCurrency().getId());	
		FinancialTransaction_DTO ftDTO = null;
		MatchEvent_Id match = paymentSegmentDTO.getAdjustmentId().getEntity().getRelatedFinancialTransaction().getMatchEventId();
		paymentSegmentDTO.setMatchEventId(match);
		Adjustment_Id linkedAdjustmentId = paymentSegmentDTO.getAdjustmentId();
		CreateDistributeFreezePayment createDistributeFreezePayment = Factory.newInstance();
		Payment payment = createDistributeFreezePayment.process(paymentDTO, paymentSegmentDTO, (Date) null, (Date) null,
				(GeneralLedgerDistributionCode) null, linkedAdjustmentId);
		if (this.notNull(payment)) {
			this.paymentId = payment.getId();
		}

		
		MatchEvent mtchEntity =	new MatchEvent_Id("").getEntity();
		MatchEvent_DTO mtchDTO = mtchEntity.getDTO();
		mtchDTO.setMatchEventStatus(MatchEventStatusLookup.constants.BALANCED);
		mtchEntity.setDTO(mtchDTO);
	}
	
	public String createObligation(String accountId, String division, String obligationType) {

		  // Business Service Instance
		  BusinessServiceInstance bsInstance = BusinessServiceInstance.create("C1-FindCreateObligation");

		  // Populate BS parameters if available
		  if (null != accountId && null != division && null != obligationType) {
		   COTSInstanceNode group = bsInstance.getGroupFromPath("input");
		   group.set("accountId", accountId);
		   group.set("division", division);
		   group.set("obligationType", obligationType);
		  }

		  return executeBSAndCreateObligation(bsInstance);

		 }
	
	/**
	 * @param bsInstance
	 * @return
	 */
	private String executeBSAndCreateObligation(BusinessServiceInstance bsInstance) {
		  // TODO Auto-generated method stub
		  bsInstance = BusinessServiceDispatcher.execute(bsInstance);
		  String obligationId = null;
		  System.out.println(getSystemDateTime().getDate());
		  // Getting the list of results
		  COTSInstanceNode group = bsInstance.getGroupFromPath("output");

		  // If list IS NOT empty
		  if (group != null) {
		   obligationId = group.getString("obligationId");
		  }
		  logger.info("obligationId " +obligationId); 
		  System.out.println("obligationId " +obligationId); 
		  return obligationId;

		 }
	
	/**
	 * @param obligationId
	 * @param adjustmentType
	 * @param adjustmentAmount
	 * @param debtCat
	 * @param date
	 * @return
	 */
	private String createAdjustment(String obligationId, String adjustmentType, String adjustmentAmount,String debtCat,
			com.splwg.base.api.datatypes.Date date) { 
		
		    BusinessServiceInstance businessServiceInstanc = BusinessServiceInstance.create("C1-AdjustmentAddFreeze");
			COTSInstanceNode cotsGroup = businessServiceInstanc.getGroupFromPath("input");
			cotsGroup.set("serviceAgreement", obligationId);
			cotsGroup.set("adjustmentType", adjustmentType);
			cotsGroup.set("adjustmentAmount", new BigDecimal(adjustmentAmount));
			cotsGroup.set("debtCategory", debtCat); //	
			cotsGroup.set("adjustmentDate", date);

		  return executeBSAndCreateAdjustment(businessServiceInstanc);
		  
	}
	
	/**
	 * @param bsInstance
	 * @return
	 */
	private String executeBSAndCreateAdjustment(BusinessServiceInstance bsInstance) {
		  // TODO Auto-generated method stub
		  bsInstance = BusinessServiceDispatcher.execute(bsInstance);
		  String adjustmentId = null;
		  // Getting the list of results
		  COTSInstanceNode group = bsInstance.getGroupFromPath("output");

		  if (group != null) {
			  adjustmentId = group.getString("adjustment");
		  }
		  logger.info("adjustmentId " +adjustmentId); 
		  System.out.println("adjustmentId " +adjustmentId); 
		  return adjustmentId;

		 }

	@SuppressWarnings("deprecation")
	private LinkedHashMap<HashMap<String, Money>, HashMap<String, HashMap<List<String>, List<Money>>>> getDebtObligation(String accId) {
		
		PreparedStatement psPreparedStatement = null;

		String acc1 = getAccountType1();
		String acc2 = getAccountType2();
		String acc3 = getAccountType3();
		String oblType1 = getObligationType1();
		String oblType2 = getObligationType2();
		String oblType3 = getObligationType3();
		String oblType4 = getObligationType4();
		String adjType1 = getAdjustmentType1();
		String adjType2 = getAdjustmentType2();
		String adjType3 = getAdjustmentType3();
		String adjType4 = getAdjustmentType4();
		String adjType5 = getAdjustmentType5();
		String adjType6 = getAdjustmentType6();
		String adjType7 = getAdjustmentType7();

		String period = null;
		HashMap<String, Money> debtOblMap = new HashMap<String, Money>();
		HashMap<String, HashMap<List<String>,List<Money>>> periodMap = new HashMap<String, HashMap<List<String>,List<Money>>>();
	    LinkedHashMap<HashMap<String, Money>, HashMap<String, HashMap<List<String>,List<Money>>>> debtPriorityMap = new LinkedHashMap<HashMap<String, Money>, HashMap<String, HashMap<List<String>,List<Money>>>>();
		
	    psPreparedStatement = createPreparedStatement("SELECT CAP.ACCT_ID, CS.SA_ID, CS.SA_TYPE_CD, CS.SA_STATUS_FLG ,CADJ.ADJ_TYPE_CD, CADJ.ADJ_ID, CADJ.ADJ_AMT, CS.START_DT , CADJ.CRE_DT FROM CI_ACCT_PER CAP, "
	    		+ "CI_ACCT CA ,CI_SA CS,CI_ADJ CADJ WHERE CAP.PER_ID IN (SELECT PER_ID from CI_ACCT_PER where ACCT_ID = '4062146587') "
				+ " AND CAP.ACCT_ID=CA.ACCT_ID AND CA.CUST_CL_CD IN('ATMP') AND CAP.ACCT_ID = CS.ACCT_ID "
				+ " AND CS.SA_TYPE_CD in('CM_DNS') AND CS.SA_ID = CADJ.SA_ID AND CADJ.ADJ_TYPE_CD IN "
				+ "('CPF') AND CS.SA_STATUS_FLG=40 ORDER BY CS.START_DT");
	    /*	psPreparedStatement = createPreparedStatement("SELECT CAP.ACCT_ID, CS.SA_ID, CS.SA_TYPE_CD, CS.SA_STATUS_FLG ,"
					+ "CADJ.ADJ_TYPE_CD, CADJ.ADJ_ID, CADJ.ADJ_AMT, CS.START_DT , CADJ.CRE_DT FROM CI_ACCT_PER CAP,"
					+ " CI_ACCT CA ,CI_SA CS,CI_ADJ CADJ WHERE CAP.PER_ID IN (SELECT PER_ID from CI_ACCT_PER where ACCT_ID = :accId)"
							+ " AND CAP.ACCT_ID=CA.ACCT_ID AND CA.CUST_CL_CD IN(:acc1,:acc2,:acc3) AND CAP.ACCT_ID = CS.ACCT_ID "
							+ " AND CS.SA_TYPE_CD in(:oblType1,:oblType2,:oblType3) AND CS.SA_ID = CADJ.SA_ID AND CADJ.ADJ_TYPE_CD IN "
							+ " (:adjType1,:adjType2,:adjType3,:adjType4,:adjType5,:adjType6,:adjType7) AND CS.SA_STATUS_FLG=40 ORDER BY CS.START_DT");
			psPreparedStatement.setAutoclose(false); */
			try {
/*				psPreparedStatement.bindString("accId", accId, null);
				psPreparedStatement.bindString("acc1", acc1, null);
				psPreparedStatement.bindString("acc2", acc2, null);
				psPreparedStatement.bindString("acc3", acc3, null);
				psPreparedStatement.bindString("oblType1", oblType1, null);
				psPreparedStatement.bindString("oblType2", oblType2, null);
				psPreparedStatement.bindString("oblType3", oblType3, null);
				psPreparedStatement.bindString("adjType1", adjType1, null);
				psPreparedStatement.bindString("adjType2", adjType2, null);
				psPreparedStatement.bindString("adjType3", adjType3, null);
				psPreparedStatement.bindString("adjType4", adjType4, null);
				psPreparedStatement.bindString("adjType5", adjType5, null);
				psPreparedStatement.bindString("adjType6", adjType6, null);
				psPreparedStatement.bindString("adjType7", adjType7, null);*/
				
				QueryIterator<SQLResultRow> result = psPreparedStatement.iterate();
				List<Money> moneyList = new ArrayList<Money>();
				List<String> oblgList = new ArrayList<String>();
				List<String> saIdList = new  ArrayList<String>();
				HashMap<List<String>, List<Money>> oblMoneyMap = new HashMap<List<String>, List<Money>>();
				while (result.hasNext()) {
					System.out.println("I am In");
					SQLResultRow lookUpValue = result.next();
					System.out.println(lookUpValue.getString("SA_ID"));
					if(!saIdList.contains(lookUpValue.getString("SA_ID"))){
						saIdList.add(lookUpValue.getString("SA_ID"));
					try {
						psPreparedStatement = createPreparedStatement("SELECT SUM(CUR_AMT) AS \"Total\" from CI_FT where SA_ID = "+ lookUpValue.getString("SA_ID"), "select");
						psPreparedStatement.setAutoclose(false);
						QueryIterator<SQLResultRow> oblResultIterator = psPreparedStatement.iterate();
						while (oblResultIterator.hasNext()) {
							System.out.println("I am In");
							SQLResultRow oblResult = oblResultIterator.next();
							System.out.println(lookUpValue.getString("SA_ID"));
							if (oblResult.getString("Total") != null && Integer.parseInt(oblResult.getString("Total")) > 0) {
								debtOblMap.put(lookUpValue.getString("SA_ID"), new Money(oblResult.getString("Total")));
								
								if(null == period || lookUpValue.getString("START_DT").equalsIgnoreCase(period)){
									period = lookUpValue.getString("START_DT");
									moneyList.add(new Money(oblResult.getString("Total")));	
									oblgList.add(lookUpValue.getString("SA_ID"));
									oblMoneyMap = new HashMap<List<String>,List<Money>>();
									oblMoneyMap.put(oblgList, moneyList);
									periodMap.put(period, oblMoneyMap);
								} else if(!lookUpValue.getString("START_DT").equalsIgnoreCase(period)) {
									moneyList = new ArrayList<Money>();
									oblgList = new ArrayList<String>();
									oblMoneyMap = new HashMap<List<String>,List<Money>>();
									moneyList.add(new Money(oblResult.getString("Total")));
									oblgList.add(lookUpValue.getString("SA_ID"));
									oblMoneyMap.put(oblgList, moneyList);
									periodMap.put(lookUpValue.getString("START_DT"), oblMoneyMap);
									period = lookUpValue.getString("START_DT");
								}
							}
						}	
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
					
			}
				debtPriorityMap.put(debtOblMap, periodMap);
		} catch (Exception exception) {
				exception.printStackTrace();
			} finally {
				psPreparedStatement.close();
				psPreparedStatement = null;
			}
		//}
		return debtPriorityMap;
	}
	
	@Override
	public void setPaymentEvent(PaymentEvent arg0) {
		// TODO Auto-generated method stub
		paymentEvent = arg0;
	}

	@Override
	public void setDistributionRule(DistributionRule arg0) {
		// TODO Auto-generated method stub
		distributionRule = arg0;

	}

	@Override
	public void setAmount(Money arg0) {
		// TODO Auto-generated method stub
		amount = arg0;

	}

	@Override
	public void setCharacteristicType(CharacteristicType arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacteristicValue(String arg0) {
		// TODO Auto-generated method stub


	}

	@Override
	public void setAdhocCharacteristicValue(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacteristicValueFk1(String arg0) {
		// TODO Auto-generated method stub
		accId = arg0;
		characteristicValueFk1 = arg0;

	}

	@Override
	public void setCharacteristicValueFk2(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacteristicValueFk3(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacteristicValueFk4(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacteristicValueFk5(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTenderAccount(Account arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMatchType(MatchType arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMatchValue(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSequence(BigInteger arg0) {
		// TODO Auto-generated method stub
		sequence = arg0;
		
	}

	@Override
	public Payment_Id getPaymentId() {
		// TODO Auto-generated method stub
		return null;
	}

}

