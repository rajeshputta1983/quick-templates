package com.codequicker.quick.templates.cache.provider;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * @author Rajesh Putta
 */
public class ConcurrentLRUCacheProvider extends LRUCacheProvider {

	private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(
			true);
	private ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
	private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock
			.writeLock();

	public ConcurrentLRUCacheProvider() {
	}

	public ConcurrentLRUCacheProvider(int minCapacity, int maxCapacity) {
		super(minCapacity, maxCapacity);
	}

	@Override
	public Object get(Object key) {
		writeLock.lock();
		try {
			return super.get(key);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public void insert(Object key, Object value) {
		writeLock.lock();
		try {
			super.insert(key, value);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public Object peek(Object key) {
		readLock.lock();
		try {
			return super.peek(key);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public void destroy() {
		writeLock.lock();
		try {
			super.destroy();
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public void remove(Object key) {
		writeLock.lock();
		try {
			super.remove(key);
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public void stop() {
		writeLock.lock();
		try {
			super.stop();
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public int size() {
		readLock.lock();
		try {
			return super.size();
		} finally {
			readLock.unlock();
		}
	}

}
