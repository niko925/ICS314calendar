package ics314.dezesseis.calendar.interfacecomponents;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Scanner;

import ics314.dezesseis.calendar.GEO;
import ics314.dezesseis.calendar.InputCheck;
import ics314.dezesseis.calendar.VObject;
import ics314.dezesseis.calendar.constants.Component;

public class CreateICSFile {

	public CreateICSFile(){	
	}
	
	public VObject start(Scanner cliInput){
        VObject event = new VObject(Component.EVENT);
		InputCheck inputCheck = new InputCheck();
		GEO GEOposition;
		String title = "", begin="",end="", address="",position;
		
		//ask the user: do he/she want to create a ics file	
	     //if input no then exit program
	     //input title of event
	     System.out.print("Please, Enter a title of the evet: ");
	     title = cliInput.nextLine();
	     
	     //input the begin and end date and make sure that end date is later than begin time
	     do{
	    	   begin = inputCheck.CheckDate("beginning")+"T";
	       	   begin += inputCheck.Checktime("beginning")+"00";
	       	   end = inputCheck.CheckDate("ending")+"T";
	       	   end += inputCheck.Checktime("ending")+"00";
	     }while(!inputCheck.CheckBeginTimeAndEndTime(begin, end));
	     //input the location 
	 	System.out.print("Please, enter the location of the event:");
	    address = cliInput.nextLine().trim();

	    //input geo position if use input yes;
	    if(YesOrNo("Do you want to locate the position?: ", cliInput)){
	    	GEOposition = new GEO(address);
	    	position = GEOposition.getPosition();
	    	if(position.equalsIgnoreCase("ERROR")){
	    		System.out.println("ERROR: Cannot find the position.");
	    		if (YesOrNo("Do you want to enter the position?", cliInput))
	    			position = inputCheck.CheckPosition();
	    	}
	    	else{
	    		System.out.println("the position of the location has been found");
	    		event.addContentLine("GEO", position);
	    	}
	    }
	    System.out.println("Please, enter a description of the event:");
	    System.out.print("->");
       	String description = cliInput.nextLine();
       	description = description.trim();
       	//input all data to event
    	event.addSummary(title);
       	event.addDescription(description);
       	event.addDtStart(begin);
       	event.addDtEnd(end);
       	event.addDtStamp(CurrentTimeStamp());
       	event.addLocation(address);
       	return event;
       	//calendar.addChild(event);
       	//WriteToICSFile(event, System.getProperty("user.home")+"/Desktop/test1.ics");
	}
	
	/****************************
	 * ask user yes or no question
	 * @param yes or no question that you want to ask 
	 * @return true or false
	 */
	public static boolean YesOrNo(String question, Scanner cliInput){
		String input;
		do{
			System.out.print(question+"(y/n): ");
	        input = cliInput.nextLine();
	        if(input.equalsIgnoreCase("y"))
	        	return true;
	     }while(!input.equalsIgnoreCase("n"));
		return false;
	}
	
	/***********************
	 * current time
	 * @return String, ICS file format current time stamp 
	 */
	public static String CurrentTimeStamp(){	
       	Date myDate = new Date();
       	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
       	return sdf.format(myDate);        	
	}
	
	
	/*****************************
	 * @param event (VObject)
	 * @param path (String) .ics file archive path 
	 */
	public static void WriteToICSFile(VObject event,String path){
		VObject calendar = new VObject(Component.CALENDAR);
		calendar.addChild(event);
		File out = new File(path);
       	try(FileOutputStream outStream = new FileOutputStream(out)) {
       		outStream.write(calendar.getTextRepresentation().getBytes());
       		outStream.close();
       	} catch (IOException e) {
       		e.printStackTrace();
       	}
		
	}
}