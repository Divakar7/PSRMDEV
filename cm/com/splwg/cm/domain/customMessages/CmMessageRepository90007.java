package com.splwg.cm.domain.customMessages;

import com.splwg.base.domain.common.message.AbstractMessageRepository;
import com.splwg.base.domain.common.message.MessageParameters;
import com.splwg.shared.common.ServerMessage;

public class CmMessageRepository90007  extends AbstractMessageRepository{

	
	/**
    * Message Category Number 90002
    */
    public static final int MESSAGE_CATEGORY = 90007;
    
    private static CmMessageRepository90007 instance;

    public CmMessageRepository90007() {
        super(MESSAGE_CATEGORY);
    }

    private static CmMessageRepository90007 getCommonInstance() {
        if (instance == null) {
          instance = new CmMessageRepository90007();
        }
        return instance;
    }

    /**
     * Method for Email id exist
     * @param param1
     * @param param2
     * @param param3
     * @return
     */
    public static ServerMessage MSG_302(String param1,String param2,String param3) {
        MessageParameters params = new MessageParameters();
        params.addRawString(param1);
        params.addRawString(param2);
        params.addRawString(param3);
        return getCommonInstance().getMessage(Messages.MSG_302, params);
    }
    
    /**
     * Method for Trade register number exist
     * @param param1
     * @param param2
     * @param param3
     * @return
     */
    public static ServerMessage MSG_303(String param1,String param2,String param3) {
        MessageParameters params = new MessageParameters();
        params.addRawString(param1);
        params.addRawString(param2);
        params.addRawString(param3);
        return getCommonInstance().getMessage(Messages.MSG_303, params);
    }
    
    /**
     * Method for Ninea id exist
     * @param param1
     * @param param2
     * @param param3
     * @return
     */
    public static ServerMessage MSG_313(String param1,String param2,String param3) {
        MessageParameters params = new MessageParameters();
        params.addRawString(param1);
        params.addRawString(param2);
        params.addRawString(param3);
        return getCommonInstance().getMessage(Messages.MSG_313, params);
    }
    
    

    
    public static class Messages {
        /**
        * Message Text: "Variable '%1' must be initialized in '%2' '%3'"
        */
        public static final int MSG_301 = 301;

        /**
        * Message Text: "Invalid XPath '%1' in Form Rule '%2' from Form Type '%3'"
        */
        public static final int MSG_302 = 302;

        /**
        * Message Text: "'%1' parameter value is required in Form Rule '%2' from Form Type '%3'"
        */
        public static final int MSG_303 = 303;

        /**
        * Message Text: "'%1' parameter value is not allowed in Form Rule '%2' from Form Type '%3'"
        */
        public static final int MSG_304 = 304;

        /**
        * Message Text: "%1 is an invalid filter name. Filter names must be in the form of filter1, filter2.. to filter25"
        */
        public static final int MSG_305 = 305;

        /**
        * Message Text: "%1 is an invalid column name. Column names must be in the form of column1, column2... to column20"
        */
        public static final int MSG_306 = 306;

        /**
        * Message Text: "Person Type %1 does not have a Related Transaction BO"
        */
        public static final int MSG_307 = 307;

        /**
        * Message Text: "A name must be provided for the Person. First Name and Last Name must be provided for Individual persons or Business Name for Legal persons"
        */
        public static final int MSG_308 = 308;

        /**
        * Message Text: "Person: %1 does not have an account of type: %2."
        */
        public static final int MSG_309 = 309;

        /**
        * Message Text: "File: %1 is not a file or does not exist."
        */
        public static final int MSG_310 = 310;

        /**
        * Message Text: "Unable to open file: %1"
        */
        public static final int MSG_311 = 311;

        /**
        * Message Text: "Unable to to open workbook for file %1"
        */
        public static final int MSG_312 = 312;

        /**
        * Message Text: "There is no Identification Type associated with column %1."
        */
        public static final int MSG_313 = 313;

        /**
        * Message Text: "The specified object type %1 is not valid."
        */
        public static final int MSG_314 = 314;

        /**
        * Message Text: "The specified %1 %2 does not exist."
        */
        public static final int MSG_315 = 315;

        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_316 = 316;

        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_317 = 317;
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_318 = 318;
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_319 = 319;
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_320 = 320;
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_321 = 321;
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_322 = 322;
        
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_323 = 323;
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_324 = 324;
        
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_325 = 325;
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_326 = 326;
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_327 = 327;
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_328 = 328;
        
        /**
        * Message Text: "The selected value %1 (%2) from Extendable Lookup %5, does not have the proper parent; it should be %3 instead of %4."
        */
        public static final int MSG_329 = 329;
        

    }
    
}
