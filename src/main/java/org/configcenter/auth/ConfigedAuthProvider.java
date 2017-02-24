/**
 * 
 */
package org.configcenter.auth;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

/**
 * @author 瞿建军       Email: jianjun.qu@istuary.com
 * 2017年2月23日
 */
public class ConfigedAuthProvider implements AuthProvider {

	private Vertx vertx;
	
	public ConfigedAuthProvider(Vertx vertx){
		this.vertx=vertx;
	}
	
	@Override
	public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
		
		System.out.println(authInfo);
		
		String username = authInfo.getString(SimpleUser.Username);
		String password = authInfo.getString(SimpleUser.Password);
		
		JsonObject config= vertx.getOrCreateContext().config();
		
		String configUser = config.getString(SimpleUser.Username,"admin");
		String configPass = config.getString(SimpleUser.Password,"admin");		
		
		if(configUser.equals(username) && configPass.equals(password)){
			User u=new  SimpleUser(username);
			resultHandler.handle(Future.succeededFuture(u));
		}else{
			resultHandler.handle(Future.failedFuture("Login failed!"));
		}
		
	}

}
