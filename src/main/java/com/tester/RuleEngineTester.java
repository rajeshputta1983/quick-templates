package com.tester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codequicker.quick.templates.core.EngineType;
import com.codequicker.quick.templates.core.IEngine;
import com.codequicker.quick.templates.core.EngineFactory;
import com.codequicker.quick.templates.state.EngineContext;

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
		
		context.setJson(jsonPayload);
		
		IEngine ruleEngine=EngineFactory.getInstance().getEngine(EngineType.RULES);
		
		ruleEngine.initialize("/com/tester/rules-config.xml");
		
		ruleEngine.execute(context);
		
	}
}
