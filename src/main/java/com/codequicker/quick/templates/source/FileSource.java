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

package com.codequicker.quick.templates.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.codequicker.quick.templates.utils.StreamUtils;

/*
* @author Rajesh Putta
*/
public class FileSource implements ISource {
	
	public String readContent(String filePath)
	{
		BufferedReader reader=null;
		
		try {
			reader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));
			
			String line=null;
			
			StringBuilder content=new StringBuilder();
			
			while((line=reader.readLine())!=null)
			{
				content.append(line).append("\r\n");
			}
			
			return content.toString();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			StreamUtils.closeReaderWriter(reader, null);
		}
		
		return "";
	}
}
