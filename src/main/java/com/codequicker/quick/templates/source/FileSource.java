package com.codequicker.quick.templates.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.codequicker.quick.templates.utils.StreamUtils;

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
				content.append(line);
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
