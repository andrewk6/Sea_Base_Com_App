import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Test{
	public static void main(String[]args) {
		try {
			//OPCPackage pkg = OPCPackage.open(new File("2018 Sailing Staff Schedule.xlsx"));
			FileInputStream in = new FileInputStream(new File("2018 Sailing Staff Schedule.xlsx"));
			XSSFWorkbook wb = new XSSFWorkbook(in);
			XSSFSheet sht = wb.getSheetAt(0);
			System.out.println(sht.getRow(25).getCell(25+19).getStringCellValue());
			in.close();
			wb.close();

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}