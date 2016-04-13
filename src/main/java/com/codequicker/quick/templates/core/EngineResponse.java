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

package com.codequicker.quick.templates.core;

import com.codequicker.quick.templates.state.MimeType;

/*
* @author Rajesh Putta
*/
public class EngineResponse {
	
	private Object content;
	private MimeType mimeType;
	private boolean isBinary=false;
	
	public void setContent(Object content) {
		this.content = content;
	}
	
	public Object getContent() {
		return content;
	}
	
	public void setMimeType(MimeType mimeType) {
		this.mimeType = mimeType;
	}
	
	public MimeType getMimeType() {
		return mimeType;
	}
	
	public void setBinary(boolean isBinary) {
		this.isBinary = isBinary;
	}
	
	public boolean isBinary() {
		return isBinary;
	}
	
	public String getContentAsText()
	{
		if(!this.isBinary)
		{
			return String.valueOf(this.content);
		}
		
		return null;
	}
	
	public byte[] getContentAsBinary()
	{
		if(this.isBinary)
		{
			return (byte[])this.content;
		}
		
		return null;
	}
	
}
