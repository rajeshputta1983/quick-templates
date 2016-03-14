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
import com.codequicker.quick.templates.state.EngineContext;

/*
* @author Rajesh Putta
*/
public class XmlLookupHandler implements ILookupHandler {
	
	private Pattern arrayNodePattern=Pattern.compile(".+\\[(\\d+)\\](@.+)?");
	
	public Class<?> getType() {
		return null;
	}
	
	public Object lookup(String key, EngineContext context, Object reference,
			boolean returnArrayType) {
		
		Object node=(reference!=null)?(Node)reference:context.getXmlRoot();
		
		if(node==null)
			throw new TemplateRuntimeException("No Xml Payload is set in the EngineContext instance...");
		
		String[] keys=key.split("\\.");
		
		boolean flag=(reference==null);
		
		boolean isArrayType=false;
		
		for(String xmlKey: keys)
		{
			Matcher matcher=arrayNodePattern.matcher(xmlKey);
			
			if(matcher.matches())
				isArrayType=true;
			else
				isArrayType=false;

			if(isArrayType)
			{
				String tmpKey=xmlKey;
				
				int startIndex=xmlKey.indexOf("[");
				
				int endIndex=xmlKey.indexOf("]", startIndex+1);
				
				String indexStr=xmlKey.substring(startIndex+1, endIndex);

				xmlKey=xmlKey.substring(0, startIndex);

				Element element=null;
				
				NodeList nodeList=null;
				
				if(node instanceof Node)
				{
					element=(Element)node;
					nodeList=element.getElementsByTagName(xmlKey);
				}
				else if(node instanceof NodeList)
				{
					nodeList=(NodeList)node;
				}
				
				int index=Integer.parseInt(indexStr);
				
				if(index>=nodeList.getLength())
				{
					throw new TemplateRuntimeException("Array index out of range...expected..."+index+"\tactual length..."+nodeList.getLength());
				}
					
				node=nodeList.item(index);
				
				if(endIndex+2<tmpKey.length())
				{
					tmpKey=tmpKey.substring(endIndex+2);
					NamedNodeMap namedNodeMap=((Node)node).getAttributes();
					
					if(namedNodeMap==null)
					{
						throw new TemplateRuntimeException("element with no attributes...but tried to access attribute..."+tmpKey);
					}
					
					node=namedNodeMap.getNamedItem(tmpKey);
				}
			}
			else
			{
				int attrIndex=xmlKey.indexOf("@");
				
				String attribute=null;
				
				if(attrIndex>-1)
				{
					attribute=xmlKey.substring(attrIndex+1);
					xmlKey=xmlKey.substring(0, attrIndex);
				}
				
				Element element=null;
				
				if(node instanceof Node)
				{
					element=(Element)node;

//					if(element.isSameNode((Node)context.getXmlRoot()))
					if(flag)
					{
						flag=false;
						continue;
					}
					
					NodeList tmpList=element.getElementsByTagName(xmlKey);

					if(attribute!=null)
					{
						if(tmpList.getLength()==1)
						{
							node=tmpList.item(0);
							NamedNodeMap namedNodeMap=((Node)node).getAttributes();
							
							if(namedNodeMap==null)
							{
								throw new TemplateRuntimeException("element with no attributes...but tried to access attribute..."+attribute);
							}
							
							node=namedNodeMap.getNamedItem(attribute);
						}
						else if(tmpList.getLength()>1)
						{
							throw new TemplateRuntimeException("node list is not expected here..."+xmlKey);
						}
					}
					else
					{
						node=tmpList;
					}
				}
				else if(node instanceof NodeList)
				{
					NodeList tmpList=(NodeList)node;
					
					if(tmpList.getLength()==1)
					{
						node=tmpList.item(0);
						
						NamedNodeMap namedNodeMap=((Node)node).getAttributes();
						
						if(namedNodeMap==null)
						{
							throw new TemplateRuntimeException("element with no attributes...but tried to access attribute..."+attribute);
						}
						
						node=namedNodeMap.getNamedItem(attribute);
					}
					else
					{
						throw new TemplateRuntimeException("invalid key...observed list of nodes with parent tag..."+key);
					}
				}
			}
		}
		
		if(returnArrayType)
		{
			if(node instanceof NodeList)
			{
				return new NodeListIterator((NodeList)node);
			}	
			else
			{
				throw new TemplateRuntimeException("Xml node object is not of array type..."+key);
			}
		}

		if(node instanceof Node)
		{
			return ((Node) node).getTextContent();
		}
		else if(node instanceof NodeList)
		{
			NodeList tmpList=(NodeList)node;
			
			if(tmpList.getLength()==1)
			{
				node=tmpList.item(0);
				
				return ((Node) node).getTextContent();
			}
			else
			{
				throw new TemplateRuntimeException("expected one node...but observed array of nodes for given key..."+key);
			}
		}
		
		return null;
	}
}
