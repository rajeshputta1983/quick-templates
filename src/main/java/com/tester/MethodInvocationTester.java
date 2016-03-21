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

import java.util.HashMap;
import java.util.Map;

import com.codequicker.quick.templates.core.EngineFactory;
import com.codequicker.quick.templates.core.EngineType;
import com.codequicker.quick.templates.core.IEngine;
import com.codequicker.quick.templates.state.EngineContext;

/*
* @author Rajesh Putta
*/
public class MethodInvocationTester {
	public static void main(String[] args) throws Exception {
		EngineContext context=new EngineContext();
		
		Map<String, String> companyData=new HashMap<String, String>();
		companyData.put("code", "xyz");
		companyData.put("name", "mycompany");
		
		context.set("company", companyData);
		context.set("bindingId", "testPayload");
		
		IEngine templateEngine=EngineFactory.getInstance().getEngine(EngineType.TEMPLATES);
		
		templateEngine.initialize("/com/tester/template-rules-config.xml");
		
		String content=templateEngine.execute(context);
		
		System.out.println(content);
		
	}
}
