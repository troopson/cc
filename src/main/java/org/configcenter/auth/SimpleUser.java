/**
 * 
 */
package org.configcenter.auth;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;

/**
 * @author 瞿建军 Email: jianjun.qu@istuary.com 2017年2月23日
 */
public class SimpleUser extends AbstractUser {

	public static final String Username = "username";
	public static final String Password = "password";

	private JsonObject principal = null;
	private ConfigedAuthProvider authProvider = null;

	public SimpleUser(String username) {
		principal = new JsonObject().put("username", username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.vertx.ext.auth.User#principal()
	 */
	@Override
	public JsonObject principal() {
		return principal;
	}

	@Override
	public void setAuthProvider(AuthProvider authProvider) {
		if (authProvider instanceof ConfigedAuthProvider) {
			this.authProvider = (ConfigedAuthProvider) authProvider;
		} else {
			throw new IllegalArgumentException("Not a ConfigedAuthProvider");
		}
	}

	@Override
	protected void doIsPermitted(String permission, Handler<AsyncResult<Boolean>> resultHandler) {
		if(this.principal==null)
			Future.failedFuture("no permisstion!");
		else
			Future.succeededFuture(true);
	}

}
