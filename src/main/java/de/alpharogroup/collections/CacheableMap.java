package de.alpharogroup.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * The class {@link CacheableMap} is a simple cache map the factory method have
 * to be overwritten to provide new values.
 *
 * @param <K>
 *            the key type
 * @param <VK>
 *            the generic type
 * @param <VV>
 *            the generic type
 */
public abstract class CacheableMap<K, VK, VV> {

	/** The cache. */
	private final Map<K, Map<VK, VV>> cache = new HashMap<>();

	/**
	 * Gets the value from the given arguments. If it does not exist it will be created with the factory method.
	 *
	 * @param key
	 *            the key
	 * @param valueKey
	 *            the value key
	 * @return the value in the cache.
	 */
	public VV getValue(K key, VK valueKey) {
		VV value;
		Map<VK, VV> valueKeyMap = cache.get(key);
		if (valueKeyMap != null) {
			value = valueKeyMap.get(valueKey);
			if (value == null) {
				value = newValue(key, valueKey);
				valueKeyMap.put(valueKey, value);
			}
			return value;
		} else {
			valueKeyMap = new HashMap<>();
			cache.put(key, valueKeyMap);
			value = newValue(key, valueKey);
			valueKeyMap.put(valueKey, value);
		}
		return value;
	}

	/**
	 * Abstract factory method to create a new value for the cache.
	 *
	 * @param baseName
	 *            the base name
	 * @param locale
	 *            the locale
	 * @return the new value for the cache.
	 */
	public abstract VV newValue(K baseName, VK locale);
}