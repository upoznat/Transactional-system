package application;


	import java.io.File;
import java.io.FileInputStream;
	import java.io.IOException;
	import java.util.HashMap;
	import java.util.LinkedList;
	import java.util.List;

	import org.apache.poi.ss.usermodel.Sheet;
	import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
	import org.apache.poi.ss.usermodel.Row;
	import org.apache.poi.ss.usermodel.Workbook;
	import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

	public class XLSXLoader {
		
		private static FileInputStream fis;
		private static XSSFWorkbook workbook;
		private static XSSFSheet sheet;
		
		public static List<HashMap<String, String>> XLSXToList(String path) throws EncryptedDocumentException, InvalidFormatException, IOException{
			
			 path = "ExternalInputExample.xlsx";
			fis = new FileInputStream(new File(path));
			workbook = new XSSFWorkbook(new File(path));
			int numberOfAccounts = workbook.getNumberOfSheets();
			List<HashMap<String, String>> lista = new LinkedList<>();
			
			for(int i = 0;  i < numberOfAccounts ; i++){
				sheet = workbook.getSheetAt(i);
				
				int numberOfRows = sheet.getLastRowNum();
				HashMap<String, String> map = new HashMap<>();
				map.put("name", sheet.getSheetName());
				PrvaKlasa.accounts.add(sheet.getSheetName());
				
				for(int j=0; j <= numberOfRows; j++){
					final Row row = sheet.getRow(j);
					//keys - account_names, values - ammounts
					
						map.put(row.getCell(0).getStringCellValue(),
								row.getCell(1).getNumericCellValue() + "");
				}
						//we've read a row, so we put it in a list
				
				lista.add(map);
			}
			
			return lista;
		}
			
	}
		
	




