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

package com.codequicker.quick.templates.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import com.codequicker.quick.templates.exceptions.RuleEngineRuntimeException;
import com.codequicker.quick.templates.exceptions.TemplateRuntimeException;

/*
* @author Rajesh Putta
*/
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
	
	public static Class<?> getPrimitiveType(Class<?> wrapperType)
	{
		Class<?> returnType=wrapperType;
		
		if(wrapperType==Integer.class)
		{
			returnType=Integer.TYPE;
		}
		else if(wrapperType==Double.class)
		{
			returnType=Double.TYPE;
		}
		else if(wrapperType==Float.class)
		{
			returnType=Float.TYPE;
		}
		else if(wrapperType==Long.class)
		{
			returnType=Long.TYPE;
		}
		else if(wrapperType==Boolean.class)
		{
			returnType=Boolean.TYPE;
		}
		
		return returnType;
	}
	
	public static void makeAccessible(Method method) {
		
		if (!Modifier.isPublic(method.getModifiers()) ||
				!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
			method.setAccessible(true);
		}
	}	
	
	@SuppressWarnings("rawtypes")
	public static Method findMethod(Class clazz, String name, Class[] paramTypes) {
		
		Class searchType = clazz;
		
		while ( searchType != null && !Object.class.equals(searchType)) 
		{
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (name.equals(method.getName()) && Arrays.equals(paramTypes, method.getParameterTypes())) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object invokeMethod(Object carriedValue, String methodName, List<Class<?>> classList, List<Object> paramList)
	{
		if(carriedValue==null)
		{
			throw new TemplateRuntimeException("Method '"+methodName+"' cannot be invoked on 'null' object...");
		}
		
		Class<?> clazz=carriedValue.getClass();
		
		Class[] classArray=new Class[classList.size()];
		
		Method method=null;
		
		try {
			method = findMethod(clazz, methodName, classList.toArray(classArray));
			
			if(method==null)
			{
				for(int index=0;index<classArray.length;index++)
				{
					classArray[index]=getPrimitiveType(classArray[index]);
				}
				
				method = findMethod(clazz, methodName, classArray);
				
				if(method==null)
				{
					throw new TemplateRuntimeException("method not found...'"+methodName+"' with parameter types..."+classList);
				}
			}
			
			return method.invoke(carriedValue, paramList.toArray());
			
		} catch (SecurityException e) {
			throw new TemplateRuntimeException(e);
		}catch (IllegalArgumentException e) {
			throw new TemplateRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new TemplateRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new TemplateRuntimeException(e);
		}
	}
	
}
