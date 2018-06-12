import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.Days;
import org.joda.time.MutableDateTime;

public class DataScan{
	File conf, sched;
	Groupme_Com gCom;
	
	public DataScan(Groupme_Com gCom) {
		this.gCom = gCom;
		readExcel();
	}
	
	public void start() {
		conf = new File("Config.bin");
		if(!conf.exists()) {
			JFileChooser fc = new JFileChooser();
			int fCheck = fc.showOpenDialog(null);
			if(fCheck == JFileChooser.APPROVE_OPTION) {
				sched = fc.getSelectedFile();
				PrintWriter write;
				try {
					write = new PrintWriter(new FileWriter(conf));
					write.println(sched.getPath());
					write.println(7);
					write.println(0);
					write.println(8);
					write.println(0);
					write.flush();
					write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			try {
				Scanner in = new Scanner(conf);
				sched = new File(in.nextLine());
				in.close();
				if(!sched.exists()) {
					conf.delete();
					start();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		//fillStalker();
	}
	
	public void readExcel() {
		if(sched == null)
			start();
		try {
			gCom.clearArrays();
			//OPCPackage pkg = OPCPackage.open(new File("2018 Sailing Staff Schedule.xlsx"));
			FileInputStream in = new FileInputStream(sched);
			XSSFWorkbook wb = new XSSFWorkbook(in);
			XSSFSheet sht = wb.getSheetAt(0);
			
			MutableDateTime date1 = new MutableDateTime(new Date());
			date1.setMonthOfYear(5);
			date1.setDayOfMonth(27);
			MutableDateTime date2 = new MutableDateTime(new Date());
			//date2.setMonthOfYear(6);
			//date2.setDayOfMonth(9);
			int days = Days.daysBetween(date1 , date2).getDays() * 4;
			System.out.println(days);
			for(int c1 = days; c1 < days + 4; c1 ++) {
				for(int c2 = 25; c2 < 31; c2 ++) {
					System.out.println("Row: " + c2 + " Cell: " + c1 + " Val: " +
						sht.getRow(c2).getCell(c1).getStringCellValue());
					if(c1 == days) {
						String s = sht.getRow(c2).getCell(c1).getStringCellValue();
						if(s.length() > 0) gCom.getSnorkel().add(s);
					}else if(c1 == days + 1) {
						String s = sht.getRow(c2).getCell(c1).getStringCellValue();
						if(s.length() > 0) gCom.getDock().add(s);
					}else if(c1 == days + 2) {
						String s = sht.getRow(c2).getCell(c1).getStringCellValue();
						if(s.length() > 0) gCom.getBeach().add(s);
					}else if(c1 == days + 3) {
						String s = sht.getRow(c2).getCell(c1).getStringCellValue();
						if(s.length() > 0) gCom.getGear().add(s);
					}
				}
			}
			gCom.getGear().add(sht.getRow(19).getCell(days+2).getStringCellValue().concat("(Quartermaster)"));
			for(int c1 = days; c1 < days + 4; c1 ++) {
				for(int c2 = 21; c2 < 23; c2 ++) {
					String s = sht.getRow(c2).getCell(c1).getStringCellValue();
					if(s.length() > 0) gCom.getMidweek().add(s);
				}
			}
			in.close();
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fillStalker() {
		FileInputStream in;
		try {
			in = new FileInputStream(new File("2018 Sailing Staff Schedule.xlsx"));
			XSSFWorkbook wb = new XSSFWorkbook(in);
			XSSFSheet sht = wb.getSheetAt(0);
			for(int c1 = 1; c1 < 103; c1 ++) {
				if(sht.getRow(c1).getCell(3).getStringCellValue() == "Sailing") {
					
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}