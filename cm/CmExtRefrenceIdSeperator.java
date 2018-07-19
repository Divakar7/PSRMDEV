import java.util.Date;
import java.util.Scanner;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.splwg.base.api.QueryIterator;
import com.splwg.base.api.QueryResultRow;
import com.splwg.base.api.sql.PreparedStatement;
import com.splwg.base.api.sql.SQLResultRow;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
class CmExtReferenceIdSeperator
{
	public static void main(String[] args)
	{
		
		 System.out.println(csvRowCnt);
	}
	private static final PreparedStatement False = null;
	private static Scanner scanner;

        static int csvRowCnt = csvRowCountFunction();
       
		
		 static int csvRowCountFunction(){   
			BufferedReader bufferedReader;
		
		int csvRowCount = 0;
	     try {
	    	 
	    	 bufferedReader = new BufferedReader(new FileReader("D:\\PSRM\\Denash\\Registration\\488565498EMPLE.csv\\"));
			while((bufferedReader.readLine()) != null)
			 {
				csvRowCount++;
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     System.out.println("Count : "+csvRowCount);
		return csvRowCount;
		}
	}
		

	 /*System.out.println("External Ref ID:");
	 scanner = new Scanner(System.in);
	 String extRefId = scanner.nextLine();
	 //EXT_REFERENCE_ID = 'TD/37945466Denash/23456';
	 String[] extItems = extRefId.split("/");
	   //String[]  CustIdArray=(ExtItems[1].split("[^A-Za-z0-9]+|(?<=[A-Za-z])(?=[0-9])|(?<=[0-9])(?=[A-Za-z])"));
	 String[] custIdArray = (extItems[1].split("(?<=\\d)(?=\\D)"));
	 String custId = custIdArray[0];
	 String name1 = custIdArray[1];
	 System.out.println("Custid="+custId);
	 System.out.println("Name="+name1);*/
	 //List<String> ExtItemList = Arrays.asList(ExtItems);
	 //System.out.println(ExtItemList);
       //  payTender.getTenderStatus()
	 
	 //try{  
		
		   
		 //Connection con=DriverManager.getConnection("jdbc:oracle:thin:@//192.168.125.23:1521/PSRMDCSS" , "CISADM", "CISADM" );
		   
		 //PreparedStatement stmt=con.prepareStatement("insert into CM_EMP values(?,?)");  
		 //stmt.setInt(1,102);//1 specifies the first parameter in the query  
		 //stmt.setString(2,"tock");  
		   
		 //int update=stmt.executeUpdate();  
		 

		 //System.out.println(update+" records inserted");
		 
		 //PreparedStatement stmt1=con.prepareStatement("select * from CM_EMP");
		 //ResultSet rs=stmt1.executeQuery();
		 //while(rs.next()){
		 //System.out.println(rs.getInt(1)+" "+rs.getString(2));
		   
		 
		 //}
	 //}
		 
		// catch(Exception e){ System.out.println(e);}
		