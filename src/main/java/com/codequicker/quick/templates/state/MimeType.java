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

package com.codequicker.quick.templates.state;

import java.util.HashMap;
import java.util.Map;

/*
 * @author Rajesh Putta
 */
public enum MimeType {
	
	APPLICATION_JSON("application/json","json"), APPLICATION_XML("application/xml","xml"), TEXT_PLAIN("text/plain","text"), TEXT_HTML("text/html", "html"),
	APPLICATION_PDF("application/pdf", "pdf"), APPLICATION_OCTET("application/octet-stream", "binary");
	
	private String typeDesc=null;
	private String fileExtension=null;
	
	MimeType(String typeDesc, String fileExtension){
		this.typeDesc=typeDesc;
		this.fileExtension=fileExtension;
	}
	
	public String getTypeDesc() {
		return typeDesc;
	}
	
	public String getFileExtension() {
		return fileExtension;
	}
	
	public static MimeType typeOf(String extn)
	{
		MimeType result=mapper.get(extn);
		
		if(result==null)
		{
			result=MimeType.APPLICATION_OCTET;
		}
		
		return result;
	}
	
	private static final Map<String, MimeType> mapper=new HashMap<String, MimeType>();
	
	static {
		
		for(MimeType mimeType: MimeType.values())
		{
			mapper.put(mimeType.getFileExtension(), mimeType);
		}
	}
}
