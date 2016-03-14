package com.codequicker.quick.templates.source;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import com.codequicker.quick.templates.utils.StreamUtils;

public class UrlSource implements ISource {
	
	public String readContent(String url)
	{
		InputStream stream=null;
		BufferedReader reader=null;
		
		try {

			URL urlObj=new URL(url);
			
			stream =urlObj.openStream();
			
			reader = new BufferedReader(new InputStreamReader(stream, "UTF8"));
				
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
			StreamUtils.closeStreams(stream, null);			
		}
		
		return "";
	}
}
