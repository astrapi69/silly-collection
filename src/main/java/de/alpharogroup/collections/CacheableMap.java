/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.alpharogroup.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * The class {@link CacheableMap} is a simple cache map the factory method have to be overwritten to
 * provide new values.
 *
 * @param <K>
 *            the key type
 * @param <VK>
 *            the generic type
 * @param <VV>
 *            the generic type
 */
public abstract class CacheableMap<K, VK, VV>
{

	/** The cache. */
	private final Map<K, Map<VK, VV>> cache = new HashMap<>();

	/**
	 * Gets the value from the given arguments. If it does not exist it will be created with the
	 * factory method.
	 *
	 * @param key
	 *            the key
	 * @param valueKey
	 *            the value key
	 * @return the value in the cache.
	 */
	public VV getValue(K key, VK valueKey)
	{
		VV value;
		Map<VK, VV> valueKeyMap = cache.get(key);
		if (valueKeyMap != null)
		{
			value = valueKeyMap.get(valueKey);
			if (value == null)
			{
				value = newValue(key, valueKey);
				valueKeyMap.put(valueKey, value);
			}
			return value;
		}
		else
		{
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