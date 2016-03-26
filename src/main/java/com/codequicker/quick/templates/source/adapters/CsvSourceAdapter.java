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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.utils.TemplateUtil;

/*
* @author Rajesh Putta
*/
public class CsvSourceAdapter implements ISourceAdapter {
	
	public Object transformContent(String filePath)
	{
		if(TemplateUtil.isNullOrEmpty(filePath))
		{
			throw new IllegalArgumentException("csv file path cannot be null or empty...");
		}
		
		List<Map<String, String>> data=new ArrayList<Map<String, String>>();
		
		CSVReader reader=null;
		
		try {
			reader=new CSVReader(new BufferedReader(new FileReader(filePath)));
			
			String[] row=null;
			
			while((row=reader.readNext())!=null)
			{
				Map<String, String> columnData=new HashMap<String, String>();
				
				int count=1;
				
				for(String column:row)
				{
					columnData.put("column"+count, column);
					count++;
				}
				
				data.add(columnData);
			}
			
		} catch (Exception e) {
			throw new TemplateRuntimeException(e);
		}
		finally{
			if(reader!=null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return data;
	}
}
