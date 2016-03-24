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

import com.codequicker.quick.templates.exceptions.PreprocessorException;


/*
* @author Rajesh Putta
*/
public class TemplateUtil {
	
	public static boolean isReservedWord(String key)
	{
		return key.equals("payload");
	}
	
	public static boolean isNullOrEmpty(String str)
	{
		return (str==null || str.trim().equals(""));
	}
	
	public static int scanThroughForTargetChar(String expr, int index, char target)
	{
		int length=expr.length();
		while(index<length)
		{
			char ch=expr.charAt(index);
			
			if(ch==target)
			{
				return index;
			}
			
			index++;
		}
		
		throw new PreprocessorException("expected '"+target+"' character...not found...");
	}

	public static int lookAheadNeglectWhitespace(String expr, int index, char target)
	{
		int length=expr.length();
		while(index<length)
		{
			char ch=expr.charAt(index);
			
			if(!Character.isWhitespace(ch))
			{
				if(ch==target)
				{
					return index;
				}
				else
					return -1;
			}
			
			index++;
		}
		
		return -1;
	}
	
	
}
