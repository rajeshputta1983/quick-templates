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


package com.codequicker.quick.templates.cache.provider;

import java.util.HashMap;
import java.util.Map;

/*
 * @author Rajesh Putta
 */
public class LRUCacheProvider implements CacheProvider {
	public int maxCapacity;
	public int minCapacity;

	protected Map<Object, LinkedNode> cacheMap;

	protected LRULinkedList lruList;

	public LRUCacheProvider() {
		this.maxCapacity = 1 << 7;
	}

	public LRUCacheProvider(int minCapacity, int maxCapacity) {
		this.minCapacity = minCapacity;
		this.maxCapacity = maxCapacity;
	}

	public void start() throws Exception {
	}

	public void stop() {
		if (lruList != null)
			lruList.reset();
	}

	public void create() throws Exception {
		cacheMap = new HashMap<Object, LinkedNode>();
		lruList = new LRULinkedList();
		lruList.maxCapacity = this.maxCapacity;
	}

	public void destroy() {
		cacheMap = null;
		lruList = null;
	}

	public void flush() {
		LinkedNode entry = null;
		while ((entry = lruList.tail) != null) {
			remove(entry);
		}
	}

	public Object get(Object key) {
		if (key == null)
			throw new IllegalArgumentException("Key cannot be null...");

		LinkedNode node = cacheMap.get(key);
		if (node != null) {
			lruList.moveToHead(node);
			return node.value;
		}

		return null;
	}

	public Object peek(Object key) {
		if (key == null)
			throw new IllegalArgumentException("Key cannot be null...");

		LinkedNode node = cacheMap.get(key);
		if (node != null)
			return node.value;

		return null;
	}

	public void insert(Object key, Object value) {
		if (key == null || value == null)
			throw new IllegalArgumentException(
					"Null Key or Null Value is not allowed to be inserted into cache...");

		if (cacheMap.containsKey(key))
			throw new IllegalStateException(
					"Duplicate key is not allowed to be inserted....");

		if (lruList.count == lruList.maxCapacity) {
			remove(lruList.tail.key);
		}

		LinkedNode entry = new LinkedNode(key, value);
		cacheMap.put(key, entry);
		lruList.moveToHead(entry);
	}

	public void remove(Object key) {
		LinkedNode temp = cacheMap.remove(key);
		if (temp != null) {
			lruList.remove(temp);
		}
	}

	public int size() {
		return cacheMap.size();
	}

}
