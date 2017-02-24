/**
 * 
 */
package org.wertx.path;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withParameters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import org.wertx.util.StringUtil;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;

/**
 * @author 瞿建军 Email: jianjun.qu@istuary.com 2017年2月15日
 */
public abstract class WebController {

	public static Logger log = LoggerFactory.getLogger(WebController.class);

	protected Vertx vertx;

	protected AuthProvider ca;

	protected String loginUrl;

	public void setVertx(Vertx v) {
		this.vertx = v;
	}

	public void setAuthProvider(AuthProvider ca) {
		this.ca = ca;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public Context Context() {
		return this.vertx.getOrCreateContext();
	}

	public JsonObject config() {
		return Context().config();
	}

	@SuppressWarnings("unchecked")
	void routePath(Router router) {

		Set<Method> pathMethod = getAllMethods(this.getClass(), withParameters(RoutingContext.class),
				withAnnotation(Path.class));

		for (Method m : pathMethod) {

			Path p = m.getAnnotation(Path.class);
			String path = p.path();
			HttpMethod method = p.method();
			boolean auth = p.auth();
			String permit = p.permit();
			m.setAccessible(true);


			router.route(method, path).handler(routingContext -> {

				if (auth) {
//					System.out.println(this.loginUrl+"=====================");
					AuthHandler authHandler = RedirectAuthHandler.create(ca, this.loginUrl);
					if (StringUtil.isBlank(permit))
						authHandler.addAuthority(path);
					else
						authHandler.addAuthority(permit);

					authHandler.handle(routingContext);
					
					if(routingContext.response().ended() || routingContext.response().closed())
						return;
					
				}
				
				try {
					m.invoke(this, routingContext);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}

			});
		}

	}

}
