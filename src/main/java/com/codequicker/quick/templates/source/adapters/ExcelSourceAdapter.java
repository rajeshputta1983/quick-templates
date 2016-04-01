/*
 
* Copyright 2016 Rajesh Putta
	
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.codequicker.quick.templates.source.adapters;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.codequicker.quick.templates.source.adapters.ISourceAdapter;
import com.codequicker.quick.templates.utils.TemplateUtil;

/*
* @author Rajesh Putta
*/
public class ExcelSourceAdapter implements ISourceAdapter {

	public Object transformContent(String filePath) {

		if(TemplateUtil.isNullOrEmpty(filePath))
		{
			throw new IllegalArgumentException("excel file path cannot be null or empty...");
		}
		
		Map<String, List<Map<String, String>>> data=new HashMap<String, List<Map<String, String>>>();
		
		BufferedInputStream bufferedStream = null;

		try {
			bufferedStream = new BufferedInputStream(new FileInputStream(
					filePath));
			
			if (filePath.endsWith(".xls")) {
				readOleBasedData(bufferedStream, data);
			} else if(filePath.endsWith(".xlsx")){
				readXmlBasedExcel(bufferedStream, data);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (bufferedStream != null) {
				try {
					bufferedStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return data;
	}

	private void readOleBasedData(BufferedInputStream bufferedStream, Map<String, List<Map<String, String>>> data) {

		try {
			HSSFWorkbook workbook = new HSSFWorkbook(bufferedStream);

			int sheetCount = workbook.getNumberOfSheets();

			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
				Cell cell=null;
				
				List<Map<String, String>> sheetData=new ArrayList<Map<String, String>>();

				int lastRowNumber = sheet.getLastRowNum();

				for (int rowIndex = 0; rowIndex <= lastRowNumber; rowIndex++) {
					HSSFRow row = sheet.getRow(rowIndex);
					if(row==null)
					{
						continue;
					}

					Map<String, String> columnData=new HashMap<String, String>();
					
					for(int cellIndex=0; cellIndex<row.getLastCellNum(); cellIndex++) {
						cell = row.getCell(cellIndex, Row.CREATE_NULL_AS_BLANK);
						columnData.put("column"+(cellIndex+1), cell.toString());
					}
					
					sheetData.add(columnData);
				}
				
				data.put("sheet"+(sheetIndex+1), sheetData);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void readXmlBasedExcel(BufferedInputStream bufferedStream, Map<String, List<Map<String, String>>> data)
			throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(bufferedStream);

		int sheetCount = workbook.getNumberOfSheets();

		for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
			XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
			Cell cell=null;
			
			List<Map<String, String>> sheetData=new ArrayList<Map<String, String>>();

			int lastRowNumber = sheet.getLastRowNum();

			for (int rowIndex = 0; rowIndex <= lastRowNumber; rowIndex++) {
				XSSFRow row = sheet.getRow(rowIndex);
				if(row==null)
				{
					continue;
				}				
				
				Map<String, String> columnData=new HashMap<String, String>();

				for(int cellIndex=0; cellIndex<row.getLastCellNum(); cellIndex++) {
					cell = row.getCell(cellIndex, Row.CREATE_NULL_AS_BLANK);
					
					columnData.put("column"+(cellIndex+1), cell.toString());
				}
				
				sheetData.add(columnData);
			}
			
			data.put("sheet"+(sheetIndex+1), sheetData);
		}

	}
}
