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

package com.codequicker.quick.templates.state;

import java.util.HashMap;
import java.util.Map;

/*
* @author Rajesh Putta
*/
public enum NodeType {
	ROOT(""), TEXT(""), IF("#if"), ELSE_IF("#else if"), ELSE("#else"), SWITCH("#switch"), CASE("#case"), DEFAULT("#default"), FOR("#for"), EXPR("${");

	private static Map<String, NodeType> nodeTypesMap=new HashMap<String, NodeType>();
	
	private String alias;
	
	NodeType(String alias)
	{
		this.alias=alias;
	}
	
	public static NodeType getType(String alias)
	{
		return nodeTypesMap.get(alias);
	}
	
	static{
		NodeType[] nodeTypes=NodeType.values();
		
		for(NodeType type: nodeTypes)
		{
			nodeTypesMap.put(type.alias, type);
		}
	}
	
}
