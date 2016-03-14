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

import com.codequicker.quick.templates.rules.core.IRuleProcessor;
import com.codequicker.quick.templates.state.EngineContext;

/*
* @author Rajesh Putta
*/
public class TestRuleProcessor implements IRuleProcessor {
	public void process(EngineContext context) {
		System.out.println("in test rule processor impl...");
	}
}
