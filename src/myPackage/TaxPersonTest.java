package myPackage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell; // There is a way to format cells using NumberCell
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.log4testng.Logger;

import sun.rmi.runtime.Log;

public class TaxPersonTest {

	private static List<String> TaxNames = new ArrayList<String>();
	private static List<Integer> TaxPrices = new ArrayList<Integer>();
	private static List<Double> TaxAmount = new ArrayList<Double>();

	
	public static int Excel_columnNumber;
	public static int Excel_rowNumber;
	private static float necessTax =.01f;
	private static float luxTax = .09f;
	
	private static String filePath = "TaxItemSheet.xls";
	
	@BeforeTest
	public static void ReadExcelFile() throws IOException{
	
		File importWorkbook = new File(filePath);
		// Excel_columnNumber and Excel_rowNumber refer to the size of the sheet
		Excel_columnNumber =1;
		Excel_rowNumber =7;
		Workbook workbook;
		
		// This is used to convert the price numbers from Excel(String) to numbers
		int CelltoInt;
		
		try{
			workbook = Workbook.getWorkbook(importWorkbook);
		
			// this acquires the first sheet
			Sheet worksheet = workbook.getSheet(0);
			Cell TaxInt = worksheet.getCell(1, 1);
			Cell TaxString = worksheet.getCell(0,1); // columns then rows
			String a1 = TaxString.getContents();
			String b1 = TaxInt.getContents();
			
			if(a1.isEmpty() || b1.isEmpty())
			{
				System.out.print("This workbook is empty");
				Assert.assertNull(workbook,"workbook does not exist in project");
				workbook.close();
			}
			
			// This loop is executed only once to read both columns at once
			for(int column=1;column <=Excel_columnNumber;column++)
				{
					for(int row=1;row < Excel_rowNumber; row++)
					{
						// Gathers data from Tax Names in excel sheet
						TaxString = worksheet.getCell((column-1), row);
						a1 = TaxString.getContents();
				
							TaxNames.add(a1.toString());
						
							// Gathers data from Tax prices (in cents) from excel sheet
							TaxInt = worksheet.getCell(column,row);
							b1 = TaxInt.getContents();
							CelltoInt = Integer.parseInt(b1);
							TaxPrices.add(CelltoInt);
						
							
							
					}
					
					
				}
			
			
			
		} catch (BiffException biff){ biff.printStackTrace();}
		Reporter.log("Data acquired for the two Lists");
	}

	@Test
	@Parameters("para_choices")
	public static void CalculateCost()
	{
		float tax=0;
		// this test could have acquired data from parameter annotation
		char[] choices={ 'n','l','n','l','l','l'};
		char validLux = 'l';
		char validNecess = 'n'
		for(int index=0;index<choices.length;index++)
		{
			if(choices[index]==validNecess.toUpper())
			{
				tax = TaxPrices.get(index) * necessTax;
			}
			else if(choices[index]==validLux.toUpper())
			{
				tax = TaxPrices.get(index) + luxTax;
			}
			else
			{
				Reporter.log("Option invalid");
				Assert.assertNotEquals('l', 'n', "Choices 'l' or 'n' not used");
			}
			TaxAmount.add((double) tax);
			System.out.println("Cost of " + TaxNames.get(index) + " is ." + (TaxPrices.get(index) + tax) + "pennies");
		}
		
	}
	@AfterClass
	public static void Finished()
	{
		System.out.println("This is where we can do any clean up and end our test run");
		
	}
}
