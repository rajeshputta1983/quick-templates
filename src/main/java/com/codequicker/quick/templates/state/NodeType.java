package com.codequicker.quick.templates.state;

import java.util.HashMap;
import java.util.Map;

public enum NodeType {
	ROOT(""), TEXT(""), IF("#if"), FOR("#for"), EXPR("${");

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
