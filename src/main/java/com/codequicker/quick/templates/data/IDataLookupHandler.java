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

package com.codequicker.quick.templates.data;


/*
* @author Rajesh Putta
*/
public interface IDataLookupHandler {
	public Object lookupFromMap(Object context, String key, boolean getMethodCall);

	public Object lookupFromList(Object context, String key, int index);

	public Object lookupAsPrimitive(Object context);

	public Object returnFinalResult(Object context, boolean returnArrayType, String key);
}
