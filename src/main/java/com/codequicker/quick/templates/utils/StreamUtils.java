package com.codequicker.quick.templates.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class StreamUtils {
	
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
