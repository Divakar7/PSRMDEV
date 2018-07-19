package com.splwg.cm.domain.common.businessComponent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.splwg.base.api.GenericBusinessComponent;
//import com.splwg.cm.domain.customMessages.CmMessageRepository90002;
import com.splwg.shared.logging.Logger;
import com.splwg.shared.logging.LoggerFactory;

/**
 * @author Papa
 *
@BusinessComponent (customizationCallable = true)
 */
public class CmXLSXReaderComponent_Impl extends GenericBusinessComponent implements CmXLSXReaderComponent {

 	Logger logger = LoggerFactory.getLogger(this.getClass());
    File file;
    FileInputStream fileInputStream;
   //XSSFWorkbook workbook;

    public void openXLSXFile(String filePath){
        file = new File(filePath);      
        if(file.isFile() && file.exists()){
            try{
                fileInputStream = new FileInputStream(file);
               // workbook = new XSSFWorkbook(fileInputStream);   
            }
            catch(FileNotFoundException fileNotFoundException){
                fileNotFoundException.printStackTrace();
                //addError(CmMessageRepository90002.MSG_202(file.getAbsolutePath()));
            }
            catch(IOException ioException){
                ioException.printStackTrace();
              //  addError(CmMessageRepository90002.MSG_203(file.getAbsolutePath()));
            }                   
        }
        else{
            logger.info("Error opening file");
            //addError(CmMessageRepository90002.MSG_201(file.getAbsolutePath()));
        }               
    }
}
    /* public XSSFSheet openSpreadsheet(int index, String name){
        XSSFSheet spreadsheet = null;
        if(isNull(index)){
            spreadsheet = workbook.getSheet(name);
        }
        if(isNull(name))
        {
            spreadsheet = workbook.getSheetAt(index);
        }
        if(isNull(index)&&isNull(name)){
            //TODO AGREGAR PSRM ERROR MESSAGE
        }
        return spreadsheet;
    }
} */
