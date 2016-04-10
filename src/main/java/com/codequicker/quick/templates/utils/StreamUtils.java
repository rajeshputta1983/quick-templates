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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

import com.codequicker.quick.templates.exceptions.PreprocessorException;

/*
* @author Rajesh Putta
*/
public class StreamUtils {
	
	public static InputStream loadStream(String path)
	{
		if(TemplateUtil.isNullOrEmpty(path))
		{
			throw new IllegalArgumentException("config file path cannot be null or empty...");
		}
		
		InputStream stream=null;
		
		try
		{
			if(path.startsWith("file://"))
			{
				stream=new BufferedInputStream(new FileInputStream(new File(path.substring(7).trim())));
			}
			else if(path.startsWith("http://") || path.startsWith("https://"))
			{
				stream=new URL(path).openStream();
			}
			else
			{
				// try to load from classpath
				stream=StreamUtils.class.getResourceAsStream(path);
				
				if(stream==null)
				{
					stream=Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
				}
			}
		}catch (Exception e) {
			throw new PreprocessorException(e);
		}
		
		return stream;
	}
	
	public static void closeStreams(InputStream iStream, OutputStream oStream)
	{
		if(iStream!=null)
		{
			try {
				iStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(oStream!=null)
		{
			try {
				oStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void closeReaderWriter(Reader reader, Writer writer)
	{
		if(reader!=null)
		{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(writer!=null)
		{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
