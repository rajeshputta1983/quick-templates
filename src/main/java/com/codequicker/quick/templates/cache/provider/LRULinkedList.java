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

import java.util.ArrayList;
import java.util.List;

/*
 * @author Rajesh Putta
 */
public class LRULinkedList {
	private List<CacheEventListener> listeners = new ArrayList<CacheEventListener>();

	public LinkedNode head;
	public LinkedNode tail;

	public int count;

	public int maxCapacity;

	public void reset() {
		head = tail = null;
		count = 0;
		maxCapacity = 1 << 7;
	}

	public void moveToHead(LinkedNode element) {
		if (count > maxCapacity)
			throw new IllegalStateException(
					"Trying to add element on full cache...");

		if (element == null)
			throw new IllegalArgumentException(
					"Null element cannot be added...");

		if (element.key == null || element.value == null)
			throw new IllegalArgumentException(
					"Null Key or Null Value is not allowed to store in the cache...");

		if (head == null && tail == null) {
			element.prev = null;
			element.next = null;
			head = tail = element;
			count = 1;
			return;
		}

		if (element.prev == null && element.next == null) {
			if (count == 1) {
				if (head != element) {
					element.next = head;
					head.prev = element;
					head = element;
					count++;
				}
			} else {
				element.next = head;
				head.prev = element;
				head = element;
				count++;
			}
		} else if (element.prev != null && element.next == null) {
			element.prev.next = null;
			tail = element.prev;

			element.prev = null;
			element.next = head;
			head.prev = element;
			head = element;
		} else if (element.prev != null && element.next != null) {
			element.prev.next = element.next;
			element.next.prev = element.prev;

			element.prev = null;
			element.next = head;
			head.prev = element;
			head = element;
		}
	}

	public void remove(LinkedNode element) {
		if (element == null) {
			throw new IllegalArgumentException(
					"Cannot remove a null entry from the cache");
		}
		if (count == 0) {
			throw new IllegalStateException(
					"Trying to remove an entry from an empty cache");
		}

		element.key = element.value = null;

		if (count == 1) {
			head = tail = null;
		} else {
			if (element.prev == null) {
				head = element.next;
				head.prev = null;
				element.next = null;
			} else if (element.next == null) {
				tail = element.prev;
				tail.next = null;
				element.prev = null;
			} else {
				element.next.prev = element.prev;
				element.prev.next = element.next;
				element.prev = null;
				element.next = null;
			}
		}
		count--;
	}

	public synchronized void registerEventListener(CacheEventListener listener) {
		listeners.add(listener);
	}

	public synchronized void unRegisterEventListener(CacheEventListener listener) {
		listeners.remove(listener);
	}

}
