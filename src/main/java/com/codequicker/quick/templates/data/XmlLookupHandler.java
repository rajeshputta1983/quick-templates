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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;

/*
* @author Rajesh Putta
*/
public class XmlLookupHandler extends BaseDataLookupHandler {
	
	private static final Pattern ARRAY_ATTR_PATTERN=Pattern.compile("\\[\\d+\\]@");
	
	private int position=-1;
	
	public XmlLookupHandler(int position) {
		this.position=position;
	}
	
	public Object lookupFromMap(Object context, String key, boolean getMethodCall)
	{
		Element element=null;
		
		int attrIndex=key.indexOf("@");
		
		String attribute=null;
		
		if(attrIndex>-1)
		{
			attribute=key.substring(attrIndex+1);
			key=key.substring(0, attrIndex);
		}
		
		if(context instanceof Node)
		{
			element=(Element)context;
			
			if(element!=null && element.getNodeName().equals(key))
			{
				return context;
			}
			
			NodeList tmpList=element.getElementsByTagName(key);
			
			if(attribute!=null)
			{
				if(tmpList.getLength()==1)
				{
					context=tmpList.item(0);
					NamedNodeMap namedNodeMap=((Node)context).getAttributes();
					
					if(namedNodeMap==null)
					{
						throw new TemplateRuntimeException("element with no attributes...but tried to access attribute..."+attribute);
					}
					
					context=namedNodeMap.getNamedItem(attribute);
				}
				else if(tmpList.getLength()>1)
				{
					throw new TemplateRuntimeException("node list is not expected here..."+key);
				}
			}
			else
			{
				context=tmpList;
			}

			return context;
		}
		else if(context instanceof NodeList)
		{
			NodeList tmpList=(NodeList)context;
			
			if(tmpList.getLength()==1)
			{
				context=tmpList.item(0);
				
				NamedNodeMap namedNodeMap=((Node)context).getAttributes();
				
				if(namedNodeMap==null)
				{
					throw new TemplateRuntimeException("element with no attributes...but tried to access attribute..."+attribute);
				}
				
				context=namedNodeMap.getNamedItem(attribute);
			}
			else
			{
				throw new TemplateRuntimeException("invalid key...observed list of nodes with parent tag..."+key);
			}
		}
		
		return getNextHandler(context, position).lookupFromMap(context, key, getMethodCall);
	}
	
	public Object lookupFromList(Object context, String key, int index)
	{
		Element element=null;
		NodeList nodeList=null;
		String xmlKey=null;
		String attribute=null;

		Matcher tmpMatcher=ARRAY_ATTR_PATTERN.matcher(key);
		if(tmpMatcher.find())
		{
			xmlKey=key.substring(0, tmpMatcher.start());
			attribute=key.substring(tmpMatcher.end());
		}
		
		if(context instanceof Node)
		{
			element=(Element)context;
			nodeList=element.getElementsByTagName(xmlKey);
		}
		else if(context instanceof NodeList)
		{
			nodeList=(NodeList)context;
		}
		
		if(nodeList!=null)
		{
			if(index>=nodeList.getLength())
			{
				throw new TemplateRuntimeException("Array index out of range...expected..."+index+"\tactual length..."+nodeList.getLength());
			}
				
			context=nodeList.item(index);
			
			if(attribute!=null)
			{
				NamedNodeMap namedNodeMap=((Node)context).getAttributes();
				
				if(namedNodeMap==null)
				{
					throw new TemplateRuntimeException("element with no attributes...but tried to access attribute..."+attribute+"...on element.."+xmlKey);
				}
				
				context=namedNodeMap.getNamedItem(attribute);
			}
			
			return context;
		}
		
		return getNextHandler(context, position).lookupFromList(context, key, index);
	}
	
	public Object lookupAsPrimitive(Object context)
	{
		if(context instanceof Node)
		{
			return ((Node) context).getTextContent();
		}
		
		return getNextHandler(context, position).lookupAsPrimitive(context);
	}
	
	public Object returnFinalResult(Object context, boolean returnArrayType, String key)
	{
		if(returnArrayType)
		{
			if(context instanceof NodeList)
			{
				return new NodeListIterator((NodeList)context);
			}	
		}

		if(context instanceof Node)
		{
			return ((Node) context).getTextContent();
		}
		else if(context instanceof NodeList)
		{
			NodeList tmpList=(NodeList)context;
			
			if(tmpList.getLength()==1)
			{
				context=tmpList.item(0);
				
				return ((Node) context).getTextContent();
			}
			else
			{
				throw new TemplateRuntimeException("expected one node...but observed array of nodes for given key..."+key);
			}
		}

		return getNextHandler(context, position).returnFinalResult(context, returnArrayType, key);
	}
}
