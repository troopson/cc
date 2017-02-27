/**
 * 
 */
package org.configcenter.watch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.wertx.util.StringUtil;

/**
 * @author 瞿建军       Email: jianjun.qu@istuary.com
 * 2017年2月14日
 */
public class WatchHolder {
	
	private static WatchHolder holder;
	
	private static Map<String,Set<String>> watchList;
	
	public static WatchHolder getWatchHolder(){
		if(holder==null){
			synchronized(WatchHolder.class){
				if(holder==null)
					holder= new WatchHolder();
			}
		}
		return holder;
	}
	
	
	private WatchHolder(){
		watchList = new HashMap<>();
	}
	
	public void watch(String file, String callback){
		if(StringUtil.isBlank(file)||StringUtil.isBlank(callback))
			return;
		
		Set<String> ls  = watchList.get(file);
		if(ls==null){
			ls = new HashSet<>();
			watchList.put(file, ls);
		}
		ls.add(callback);		
	}
	
	public void callback(String file){
		if(StringUtil.isBlank(file))
			return;
		
		Set<String> urls = watchList.get(file);
		if(urls==null || urls.isEmpty())
			return;
				
		for(String url : urls){
			
			//TODO
		}
		
		
		
	}
	
	
	
	

}
