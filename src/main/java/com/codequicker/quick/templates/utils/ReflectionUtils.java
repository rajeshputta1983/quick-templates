package com.codequicker.quick.templates.utils;

import com.codequicker.quick.templates.exceptions.RuleEngineRuntimeException;

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
