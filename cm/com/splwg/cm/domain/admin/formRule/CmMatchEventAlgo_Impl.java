package com.splwg.cm.domain.admin.formRule;

import com.splwg.base.api.datatypes.Bool;
import com.splwg.shared.logging.Logger;
import com.splwg.shared.logging.LoggerFactory;
import com.splwg.tax.api.lookup.MatchEventStatusLookup;
import com.splwg.tax.domain.adjustment.adjustment.Adjustment;
import com.splwg.tax.domain.admin.adjustmentType.AdjustmentTypeAdjustmentFreezeAlgorithmSpot;
import com.splwg.tax.domain.customerinfo.account.Account_Id;
import com.splwg.tax.domain.financial.financialTransaction.FinancialTransaction_DTO;
import com.splwg.tax.domain.financial.matchEvent.MatchEvent;
import com.splwg.tax.domain.financial.matchEvent.MatchEvent_DTO;
/**
 * @author Divakar
 *
@AlgorithmComponent (softParameters = { @AlgorithmSoftParameter (name = typeOfAdjustment, type = string)})
 */
public class CmMatchEventAlgo_Impl extends CmMatchEventAlgo_Gen implements AdjustmentTypeAdjustmentFreezeAlgorithmSpot {

	private static final Logger logger = LoggerFactory.getLogger(CmMatchEventAlgo_Impl.class);
	Account_Id acc = null;
	FinancialTransaction_DTO ftDTO = null;
	Adjustment adjustment;
	@Override
	public void invoke() {
		// TODO Auto-generated method stub
		logger.info("Invoking Match Event algorithm:: " + acc.getEntity().getId().getIdValue());
		System.out.println("Invoking Match Event algorithm:: " + acc.getEntity().getId().getIdValue());
		
		logger.info("Invoking Match Event algorithm::Current Amount: " + ftDTO.getCurrentAmount());
		System.out.println("Invoking Match Event algorithm::Current Amount: " + acc.getEntity().getId().getIdValue());
		
		
		MatchEvent_DTO matchEvent = createDTO(MatchEvent.class);
		matchEvent.setAccountId(acc);
		matchEvent.setIsDisputed(Bool.FALSE);
		matchEvent.setMatchEventStatus(MatchEventStatusLookup.constants.OPEN);
		matchEvent.setCreatedDate(getSystemDateTime().getDate());
		MatchEvent finalMatchEvnt = matchEvent.newEntity();
		System.out.println(finalMatchEvnt.getId());
		logger.info("Match Event Id : "+finalMatchEvnt.getId());
		
		//Account_Id acntId = acc.getEntity().getId();
		//String accntID = acntId.getIdValue();
		
	//select * from Ci_Match_evt where ACCT_ID = :accntID and MEVT_STATUS_FLAG = 'O';
			
		
		ftDTO.setMatchEventId(finalMatchEvnt.getId());
		ftDTO.getEntity().setDTO(ftDTO);
		
	}

	@Override
	public void setAdjustment(Adjustment arg0) {
		// TODO Auto-generated method stub
		 adjustment = arg0;
		 acc = adjustment.getServiceAgreement().getAccount().getId();
		 ftDTO = adjustment.getRelatedFinancialTransaction().getDTO();
		

	}

}
