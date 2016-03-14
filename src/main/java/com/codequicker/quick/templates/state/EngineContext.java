package com.codequicker.quick.templates.state;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;
import com.codequicker.quick.templates.utils.TemplateUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class EngineContext {
	
	private Map<String, Object> context=new HashMap<String, Object>();
	
	private Map<String, Object> variableContext=new HashMap<String, Object>();
	
	private JsonElement jsonObject=null;
	
	private Object xmlRoot=null;
	
	public void set(String key, Object value)
	{
		if(key==null || key.trim().equals(""))
			throw new TemplateRuntimeException("key cannot be null or empty...");
		
		this.context.put(key, value);
	}
	
	public void setXml(String payload)
	{
		if(TemplateUtil.isNullOrEmpty(payload))
			return;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setNamespaceAware(true);
        DocumentBuilder builder;

        try {
			builder = factory.newDocumentBuilder();
			this.xmlRoot = builder.parse(new ByteArrayInputStream(payload.getBytes("UTF8"))).getDocumentElement();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object getXmlRoot() {
		return xmlRoot;
	}
	
	public void setJson(String payload)
	{
		if(TemplateUtil.isNullOrEmpty(payload))
			return;
		
		JsonParser jsonParser = new JsonParser();
		jsonObject = jsonParser.parse(payload).getAsJsonObject();		
	}
	
	public JsonElement getJsonObject() {
		return jsonObject;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setAll(Map other)
	{
		this.context.putAll(other);
	}

	public Object get(String key)
	{
		return this.context.get(key);
	}
	
	public void setVariable(String variable, Object reference)
	{
		this.variableContext.put(variable, reference);
	}
	
	public Object getVariable(String variable) {
		return this.variableContext.get(variable);
	}
	
	public void removeVariable(String variable)
	{
		this.variableContext.remove(variable);
	}
	
	public Map<String, Object> getVariableContext() {
		return variableContext;
	}
	
	public Map<String, Object> getContext() {
		return context;
	}
	
	@Override
	public int hashCode() {
		return this.context.hashCode();
	}
	
	@Override
	public String toString() {
		return this.context.toString();
	}
	
}
