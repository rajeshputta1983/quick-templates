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

package com.codequicker.quick.templates.utils;

import com.codequicker.quick.templates.exceptions.RuleEngineRuntimeException;

/*
* @author Rajesh Putta
*/
public class ReflectionUtils {
	
	@SuppressWarnings("unchecked")
	public static <T> T loadClass(String clazz, Class<T> clazzObj)
	{
		T classObj=null;
		
		try {
			classObj=(T)ReflectionUtils.class.getClassLoader().loadClass(clazz).newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuleEngineRuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuleEngineRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuleEngineRuntimeException(e);
		}
		
		return classObj;
	}
}
