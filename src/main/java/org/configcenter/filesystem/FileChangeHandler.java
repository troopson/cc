/**
 * 
 */
package org.configcenter.filesystem;

import io.vertx.core.eventbus.Message;

/**
 * @author 瞿建军       Email: jianjun.qu@istuary.com
 * 2017年2月13日
 */
public class FileChangeHandler {

	
	
	public void onChange(Message<String> message){
		
		String m =  message.body();
				
		String[] body = m.split("@");
		
		String kind = body[0];
		String way = body[1];		
				
		System.out.println(" call "+way.toString()+ " is "+kind+".");
//		message.reply("file change "+count);
		//TODO  callback of the client servers
		
		
	}
	
}
