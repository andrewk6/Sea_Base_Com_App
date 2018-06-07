import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableInstant;

public class Test{
	private ArrayList<String> snorkel, dock, beach, gear, midweek;
	//Cell = 44 + Date Difference * 4
	public static void main(String[]args) {
		Test test = new Test();
	}
	
	public Test() {
		snorkel = new ArrayList<String>();
		dock = new ArrayList<String>();
		beach = new ArrayList<String>();
		gear = new ArrayList<String>();
		midweek = new ArrayList<String>();
		try {
			//OPCPackage pkg = OPCPackage.open(new File("2018 Sailing Staff Schedule.xlsx"));
			FileInputStream in = new FileInputStream(new File("2018 Sailing Staff Schedule.xlsx"));
			XSSFWorkbook wb = new XSSFWorkbook(in);
			XSSFSheet sht = wb.getSheetAt(0);
			
			MutableDateTime date1 = new MutableDateTime(new Date());
			date1.setMonthOfYear(5);
			date1.setDayOfMonth(27);
			MutableDateTime date2 = new MutableDateTime(new Date());
			int days = Days.daysBetween(date1 , date2).getDays() * 4;
			System.out.println(days);
			for(int c1 = days; c1 < days + 4; c1 ++) {
				for(int c2 = 25; c2 < 30; c2 ++) {
					System.out.println("Row: " + c2 + " Cell: " + c1 + " Val: " +
						sht.getRow(c2).getCell(c1).getStringCellValue());
					if(c1 == days) {
						String s = sht.getRow(c2).getCell(c1).getStringCellValue();
						if(s.length() > 0) snorkel.add(s);
					}else if(c1 == days + 1) {
						String s = sht.getRow(c2).getCell(c1).getStringCellValue();
						if(s.length() > 0) dock.add(s);
					}else if(c1 == days + 2) {
						String s = sht.getRow(c2).getCell(c1).getStringCellValue();
						if(s.length() > 0) beach.add(s);
					}else if(c1 == days + 3) {
						String s = sht.getRow(c2).getCell(c1).getStringCellValue();
						if(s.length() > 0) gear.add(s);
					}
				}
			}
			gear.add(sht.getRow(19).getCell(days+2).getStringCellValue().concat("(Quartermaster)"));
			for(int c1 = days; c1 < days + 4; c1 ++) {
				for(int c2 = 21; c2 < 23; c2 ++) {
					String s = sht.getRow(c2).getCell(c1).getStringCellValue();
					if(s.length() > 0) midweek.add(s);
				}
			}
			System.out.println("Snorkel");
			for(String s : snorkel)
				System.out.println(s);
			System.out.println("\nDock");
			for(String s : dock)
				System.out.println(s);
			System.out.println("\nBeach");
			for(String s : beach)
				System.out.println(s);
			System.out.println("\nGear");
			for(String s : gear)
				System.out.println(s);
			System.out.println("\nMidweek");
			for(String s : midweek)
				System.out.println(s);
		
			//System.out.println(sht.getRow(27).getCell(days * 4).getStringCellValue() + ", " + days);
			in.close();
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*PRINTING TEST
	 * System.out.println("Snorkel");
			for(String s : snorkel)
				System.out.println(s);
			System.out.println("\nDock");
			for(String s : dock)
				System.out.println(s);
			System.out.println("\nBeach");
			for(String s : beach)
				System.out.println(s);
			System.out.println("\nGear");
			for(String s : gear)
				System.out.println(s);
			System.out.println("\nMidweek");
			for(String s : midweek)
				System.out.println(s);
		
			//System.out.println(sht.getRow(27).getCell(days * 4).getStringCellValue() + ", " + days);
	 */
}