/**
 * 
 */
package org.wertx.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 瞿建军 Email: jianjun.qu@istuary.com 2017年2月15日
 */
public final class Singleton {

	private static Singleton holder;

	@SuppressWarnings("rawtypes")
	private Map<Class, Object> map;

	private Singleton() {
		map = new ConcurrentHashMap<>();
	}

	private static Singleton getSelf() {
		if (holder == null) {
			synchronized (Singleton.class) {
				if (holder == null)
					holder = new Singleton();
			}
		}
		return holder;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> c) {
		Singleton s = getSelf();
		try {
			T o = (T) s.map.get(c);
			if (o == null) {
				synchronized (c) {
					if (s.map.get(c) == null) {

						o = c.newInstance();
						s.map.put(c, o);

					}
				}
			}
			return o;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
