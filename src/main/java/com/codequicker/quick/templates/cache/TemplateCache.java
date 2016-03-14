package com.codequicker.quick.templates.cache;

import java.util.HashMap;
import java.util.Map;

import com.codequicker.quick.templates.state.Node;

public class TemplateCache {

	private Map<String, Node> preprocessedTemplateCache=new HashMap<String, Node>();
	
	public TemplateCache(){
	}
	
	public void set(String templatePath, Node processedNode)
	{
		this.preprocessedTemplateCache.put(templatePath, processedNode);
	}
	
	public Node get(String templatePath)
	{
		return this.preprocessedTemplateCache.get(templatePath);
	}
	
	public boolean contains(String templatePath)
	{
		return this.preprocessedTemplateCache.containsKey(templatePath);
	}
	
}
