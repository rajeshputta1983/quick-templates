package com.codequicker.quick.templates.utils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.codequicker.quick.templates.state.EngineContext;

public class TemplateUtil {
	@SuppressWarnings("rawtypes")
	public static EngineContext createContext(HttpServletRequest request, String bindingId)
	{
		EngineContext context=new EngineContext();
		
		context.set("bindingId", bindingId);
		
		context.set("method", request.getMethod());
		
		// set headers
		
		Enumeration headersEnum=request.getHeaderNames();
		
		while(headersEnum.hasMoreElements())
		{
			String headerName=(String)headersEnum.nextElement();
			
			String headerValue=request.getHeader(headerName);
			
			context.set(headerName, headerValue);
		}
		
		// set parameters
		
		Enumeration paramsEnum=request.getParameterNames();
		
		while(paramsEnum.hasMoreElements())
		{
			String paramName=(String)paramsEnum.nextElement();
			
			String[] paramValues=request.getParameterValues(paramName);
			
			context.set(paramName, paramValues);
		}
		
		return context;
	}
	
	public static boolean isReservedWord(String key)
	{
		return key.equals("payload");
	}
	
	public static boolean isNullOrEmpty(String str)
	{
		return (str==null || str.trim().equals(""));
	}
}
