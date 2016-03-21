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

import com.codequicker.quick.templates.core.EngineType;
import com.codequicker.quick.templates.core.IEngine;
import com.codequicker.quick.templates.core.EngineFactory;
import com.codequicker.quick.templates.state.EngineContext;

/*
* @author Rajesh Putta
*/
public class RuleEngineTester {
	public static void main(String[] args) throws Exception {
		EngineContext context=new EngineContext();
		
		Map<String, String> companyData=new HashMap<String, String>();
		companyData.put("code", "abc");
		
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
		
		context.setJson("someJson", jsonPayload);
		
		IEngine ruleEngine=EngineFactory.getInstance().getEngine(EngineType.RULES);
		
		ruleEngine.initialize("/com/tester/rules-config.xml");
		
		ruleEngine.execute(context);
		
	}
}
