package com.github.typesafe_query.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 簡易的なLeast Recently Usedを実現するMap実装クラスです
 * @author Takahiko Sato(MOSA architect Inc.)
 *
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = -1882071901467368406L;
	static final int DEFAULT_INITIAL_CAPACITY = 16;
	static final float DEFAULT_LOAD_FACTOR = 0.75f;
	static final int DEFAULT_CACHE_SIZE = 1024;

	private final int cacheSize;

	/**
	 * デフォルト値を使用してインスタンスを生成します
	 */
	public LRUCache() {
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_CACHE_SIZE);
	}

	/**
	 * 引数の設定値を使用してインスタンスを生成します
	 * @param capacity 初期容量
	 * @param loadFactor 係数
	 * @param cacheSize キャッシュ数
	 */
	public LRUCache(int capacity, float loadFactor, int cacheSize) {
		super(capacity, loadFactor, true);
		this.cacheSize = cacheSize;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
	 */
	@Override
	protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
		return size() > cacheSize;
	}
}
