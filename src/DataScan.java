import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataScan{
	File conf, sched;
	ArrayList<String> docks, snorkel, beach, gear, midweek;
	
	public DataScan() {
		conf = new File("Config.bin");
		if(!conf.exists()) {
			JFileChooser fc = new JFileChooser();
			int fCheck = fc.showOpenDialog(null);
			if(fCheck == JFileChooser.APPROVE_OPTION) {
				sched = fc.getSelectedFile();
				PrintWriter write;
				try {
					write = new PrintWriter(new FileWriter(conf));
					write.print(sched.getPath());
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
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		readExcel();
	}
	
	public void readExcel() {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(sched);
			XSSFSheet sheet = workbook.getSheetAt(0);
			//sheet.
		} catch (InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}