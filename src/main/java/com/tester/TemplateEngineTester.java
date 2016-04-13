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

package com.tester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codequicker.quick.templates.core.EngineFactory;
import com.codequicker.quick.templates.core.EngineResponse;
import com.codequicker.quick.templates.core.EngineType;
import com.codequicker.quick.templates.core.IEngine;
import com.codequicker.quick.templates.source.adapters.CsvSourceAdapter;
import com.codequicker.quick.templates.source.adapters.ExcelSourceAdapter;
import com.codequicker.quick.templates.source.adapters.ISourceAdapter;
import com.codequicker.quick.templates.state.EngineContext;

/*
* @author Rajesh Putta
*/
public class TemplateEngineTester {
	public static void main(String[] args) throws Exception {
		
		// in-memory objects as context
		
		EngineContext context=new EngineContext();
		
		Map<String, String> companyData=new HashMap<String, String>();
		companyData.put("code", "xyz");
		companyData.put("name", "mycompany");
		
		context.set("company", companyData);
		context.set("data", "123");
		context.set("bindingId", "testPayload");
		context.set("myname", "rajesh");
		context.set("status", "true");
		
		
		Map<String, Object> employeeMap=new HashMap<String, Object>();
		
		List<Map<String, Object>> employeeAddressList=new ArrayList<Map<String, Object>>();
		
		Map<String, Object> employeeAddressMap=new HashMap<String, Object>();
		
		employeeAddressMap.put("type", "myhome");
		employeeAddressMap.put("location", "mykphb");
		
		employeeAddressList.add(employeeAddressMap);
		
		
		employeeAddressMap=new HashMap<String, Object>();
		
		employeeAddressMap.put("type", "myoffice");
		employeeAddressMap.put("location", "mykondapur");
		
		employeeAddressList.add(employeeAddressMap);
		
		employeeMap.put("address", employeeAddressList);
		
		context.set("employee", employeeMap);
		context.set("customObject", new MyCustomObject());
		
		
		
		
		
		// json payload as context
		
		String jsonPayload="{" + 
				"  \"person\": {" + 
				"    \"name\":\"rajesh\"," + 
				"    \"position\":\"engineer\"," + 
				"    \"address\":[" + 
				"        {" + 
				"          \"type\":\"home\"," + 
				"          \"location\":\"kphb\"" + 
				"        }," + 
				"        {" + 
				"          \"type\":\"office\"," + 
				"          \"location\":\"kondapur\"" + 
				"        }      " + 
				"      ]" + 
				"  }" + 
				"  " + 
				"}";
		
		context.setJson("someJson",jsonPayload);
		

		// xml payload as context
		
		String xml="<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + 
				"<inventory>" + 
				"    <book year=\"2000\">" + 
				"        <title>Snow Crash</title>" + 
				"        <author alias=\"NealStep\">Neal Stephenson</author>" + 
				"        <publisher>Spectra</publisher>" + 
				"        <isbn>0553380958</isbn>" + 
				"        <price>14.95</price>" + 
				"    </book>" + 
				"    <book year=\"2005\">" + 
				"        <title>Burning Tower</title>" + 
				"        <author alias=\"LarrNi\">Larry Niven</author>" + 
				"        <publisher>Pocket</publisher>" + 
				"        <isbn>0743416910</isbn>" + 
				"        <price>5.99</price>" + 
				"    </book>" + 
				"    <book year=\"1995\">" + 
				"        <title>Zodiac</title>" + 
				"        <author alias=\"NealStep\">Neal Stephenson</author>" + 
				"        <publisher>Spectra</publisher>" + 
				"        <isbn>0553573862</isbn>" + 
				"        <price>7.50</price>" + 
				"    </book>" + 
				"</inventory>";
		
		context.setXml("someXml",xml);
		
		
		
		// csv file as context
		
		ISourceAdapter sourceAdapter=new CsvSourceAdapter();

		Object csvData=sourceAdapter.transformContent("C:\\payloads\\test\\sample-csv.csv");
		
		context.set("someCsv", csvData);
		
		
		// excel file as context
		
		sourceAdapter=new ExcelSourceAdapter();
		
		Object excelData=sourceAdapter.transformContent("C:\\payloads\\test\\sample.xls");
		
		context.set("someExcel", excelData);
		
		
		// process template
		
		IEngine templateEngine=EngineFactory.getInstance().getEngine(EngineType.TEMPLATES);
		
		templateEngine.initialize("/com/tester/template-rules-config.xml");
		
		long startTime=System.currentTimeMillis();

		EngineResponse response=templateEngine.execute(context);
		
		if(!response.isBinary())
		{
			String content = String.valueOf(response.getContent());
			System.out.println(content);
		}
		
		long delta=System.currentTimeMillis()-startTime;
		
		System.out.println("Time Taken (ms) : "+delta);
		
	}
}
