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

import java.util.ArrayList;
import java.util.List;


/*
* @author Rajesh Putta
*/
public class DataLookupHandlerChain {
	
	private static final DataLookupHandlerChain chain=new DataLookupHandlerChain();
	
	private IDataLookupHandler defaultHandler=new DefaultDataLookupHandler();
	
	private final List<IDataLookupHandler> handlerChain=new ArrayList<IDataLookupHandler>();
	
	private DataLookupHandlerChain()
	{
		this.handlerChain.add(new MapLookupHandler(this.handlerChain.size()));
		this.handlerChain.add(new JsonLookupHandler(this.handlerChain.size()));
		this.handlerChain.add(new XmlLookupHandler(this.handlerChain.size()));
	}
	
	public static DataLookupHandlerChain getInstance()
	{
		return chain;
	}
	
	public void addDataLookupHandler(IDataLookupHandler lookupHandler)
	{
		this.handlerChain.add(lookupHandler);
	}
	
	public List<IDataLookupHandler> getHandlerChain() {
		return handlerChain;
	}
	
	public IDataLookupHandler getNextInChain(int position)
	{
		if((position+1)>=this.handlerChain.size())
		{
			return this.defaultHandler;
		}
		
		return this.handlerChain.get(position+1);
	}
	
	public Object lookupFromMap(Object context, String key, boolean getMethodCall)
	{
		if(context==null) return null;
		
		return this.handlerChain.get(0).lookupFromMap(context, key, getMethodCall);
	}
	
	public Object lookupFromList(Object context, String key, int index)
	{
		if(context==null) return null;
		
		return this.handlerChain.get(0).lookupFromList(context, key, index);
	}
	
	public Object lookupAsPrimitive(Object context)
	{
		if(context==null) return null;
		
		return this.handlerChain.get(0).lookupAsPrimitive(context);
	}
	
	public Object returnFinalResult(Object context, boolean returnArrayType, String key)
	{
		if(context==null) return null;
		
		return this.handlerChain.get(0).returnFinalResult(context, returnArrayType, key);
	}
}
