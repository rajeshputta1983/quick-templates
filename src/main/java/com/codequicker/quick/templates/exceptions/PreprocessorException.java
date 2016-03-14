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

package com.codequicker.quick.templates.exceptions;

/*
* @author Rajesh Putta
*/
public class PreprocessorException extends RuntimeException {

	private static final long serialVersionUID = -8952086989733763984L;

	public PreprocessorException() {
		super();
	}
	
	public PreprocessorException(String msg) {
		super(msg);
	}
	
	public PreprocessorException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public PreprocessorException(Throwable t) {
		super(t);
	}
}
