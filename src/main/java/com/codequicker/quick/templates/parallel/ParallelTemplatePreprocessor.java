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

package com.codequicker.quick.templates.parallel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.codequicker.quick.templates.cache.TemplateCache;
import com.codequicker.quick.templates.exceptions.PreprocessorException;
import com.codequicker.quick.templates.processors.TemplatePreprocessor;
import com.codequicker.quick.templates.source.FileSource;
import com.codequicker.quick.templates.source.ISource;
import com.codequicker.quick.templates.state.Node;

/*
* @author Rajesh Putta
*/
public class ParallelTemplatePreprocessor {
	
	private final int THREAD_COUNT = 10; // make it configurable
	
	private static final ParallelTemplatePreprocessor preProcessor=new ParallelTemplatePreprocessor();
	
	private ExecutorService executorService=null;
	
	private TemplateCache cache=null;
	
	private Map<String, Future<Node>> futureMap=new HashMap<String, Future<Node>>();
	
	private static final Logger LOGGER=Logger.getLogger(ParallelTemplatePreprocessor.class.getName());
	
	private ParallelTemplatePreprocessor() {
		 this.executorService = Executors.newFixedThreadPool(THREAD_COUNT);
	}
	
	public void setCache(TemplateCache cache) {
		this.cache = cache;
	}
	
	public static ParallelTemplatePreprocessor getInstance(){
		return preProcessor;
	}
	
	public void submitTemplate(final String rootDirPath, final String templatePath) {
		
		if(this.futureMap.containsKey(templatePath))
		{
			return;
		}
		
		Future<Node> future = executorService
				.submit(new Callable<Node>() {
					public Node call() throws Exception {

						try {
							ISource source=new FileSource();
							String content=source.readContentAsText(rootDirPath+File.separator+templatePath);
							
							TemplatePreprocessor preprocessor=new TemplatePreprocessor();
							Node rootNode=preprocessor.preprocess(content);
							
							return rootNode;

						} catch (Exception e) {
							throw new PreprocessorException(e);
						}
					}
			});
		
	    this.futureMap.put(templatePath, future);
	    
	    LOGGER.log(Level.SEVERE, "Preprocessing Task submitted to thread pool for template..."+templatePath);
	}
	
	public void waitForCompletion()
	{
		while(true)
		{
			boolean isDone=true;
			
			Set<Entry<String, Future<Node>>> futureSet=this.futureMap.entrySet();
			
			for(Entry<String, Future<Node>> entry: futureSet)
			{
				String templatePath=entry.getKey();
				
				Future<Node> future=entry.getValue();
				
				if(future==null)
				{
					continue;
				}
				
				isDone=false;
				
				if(future.isDone())
				{
					LOGGER.log(Level.SEVERE, "Preprocessing completed for template..."+templatePath);
					
					Node rootNode=null;
					try {
						rootNode = future.get();
					} catch (InterruptedException e) {
						throw new PreprocessorException(e);
					} catch (ExecutionException e) {
						throw new PreprocessorException(e);
					}
					
					this.cache.set(templatePath, rootNode);
					
					this.futureMap.put(templatePath, null);
				}
				else
				{
					LOGGER.log(Level.SEVERE, "Preprocessing NOT YET completed for template..."+templatePath);
				}
			}
			
			if(isDone)
			{
				break;
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				throw new PreprocessorException(e);
			}
		}
		
		this.futureMap.clear();
		this.executorService.shutdownNow();
	}
	
	public void shutdown()
	{
		this.futureMap.clear();
		this.executorService.shutdownNow();
	}
}