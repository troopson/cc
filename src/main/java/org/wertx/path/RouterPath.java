/**
 * 
 */
package org.wertx.path;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.configcenter.auth.ConfigedAuthProvider;
import org.configcenter.auth.SimpleUser;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.FormLoginHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.templ.TemplateEngine;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

/**
 * @author 瞿建军 Email: jianjun.qu@istuary.com 2017年2月10日
 */
public class RouterPath {

	public static final String Webroot = "static";

	private static TemplateEngine engine = ThymeleafTemplateEngine.create();

	public static Router build(Vertx vertx, String... packages) {

		Router router = Router.router(vertx);

		setCorsSupport(router);

		routeStatic(router);

		AuthProvider ca = routeLogin(vertx, router);

		routeRestApi(vertx, router, packages, ca);

		routeThymeleafHtml(router, ca);

		return router;

	}

	private static ConfigedAuthProvider routeLogin(Vertx vertx, Router router) {
		ConfigedAuthProvider ca = new ConfigedAuthProvider(vertx);
		router.route().handler(CookieHandler.create());
		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
		router.route().handler(UserSessionHandler.create(ca));

		FormLoginHandler flogin = FormLoginHandler.create(ca);
		flogin.setUsernameParam(SimpleUser.Username);
		flogin.setPasswordParam(SimpleUser.Password);
		flogin.setDirectLoggedInOKURL("/main.html");
		router.route("/login").handler(flogin);

		router.route("/logout").handler(routingContext->{
			routingContext.clearUser();
			routingContext.response().putHeader("location", "/index.html").setStatusCode(302).end();
		});
		
		return ca;
	}

	private static void routeStatic(Router router) {
		router.routeWithRegex(".*\\.js").handler(StaticHandler.create(Webroot));
		router.routeWithRegex(".*\\.css").handler(StaticHandler.create(Webroot));
		router.routeWithRegex(".*/img/.*").handler(StaticHandler.create(Webroot));
	}

	private static void routeThymeleafHtml(Router router, AuthProvider authProvider) {

		class IndexHandler implements Handler<RoutingContext> {

			@Override
			public void handle(RoutingContext context) {
				engine.render(context, Webroot + "/index.html", ar -> {
					if (ar.succeeded()) {
						context.response().end(ar.result());
					} else {
						context.fail(ar.cause());
					}
				});
			}

		}

		IndexHandler ih = new IndexHandler();
		router.get("/index.html").handler(ih);
		router.get("/").handler(ih);

		Route r = router.getWithRegex(".*\\.html");

		TemplateHandler templhandler = TemplateHandler.create(engine, Webroot, TemplateHandler.DEFAULT_CONTENT_TYPE);

		r.handler(routingContext -> {

			AuthHandler authHandler = RedirectAuthHandler.create(authProvider, "/index.html");
			/*
			 * System.out.println(routingContext.request().uri());
			 * System.out.println(routingContext.request().absoluteURI());
			 * System.out.println(routingContext.request().path());
			 * 
			 * /main.html?param1=test
			 * http://localhost:7777/main.html?param1=test /main.html
			 * 
			 */
			authHandler.addAuthority(routingContext.request().path());
			authHandler.handle(routingContext);
			
			if(routingContext.response().ended() || routingContext.response().closed())
				return;
			
			templhandler.handle(routingContext);
		});

	}

	private static void setCorsSupport(Router router) {
		Set<String> allowHeaders = new HashSet<>();
		allowHeaders.add("x-requested-with");
		allowHeaders.add("Access-Control-Allow-Origin");
		allowHeaders.add("origin");
		allowHeaders.add("Content-Type");
		allowHeaders.add("accept");
		Set<HttpMethod> allowMethods = new HashSet<>();
		allowMethods.add(HttpMethod.GET);
		allowMethods.add(HttpMethod.POST);
		allowMethods.add(HttpMethod.DELETE);
		allowMethods.add(HttpMethod.PUT);

		router.route().handler(BodyHandler.create());
		router.route().handler(CorsHandler.create("*").allowedHeaders(allowHeaders).allowedMethods(allowMethods));
	}

	private static void routeRestApi(Vertx vertx, Router router, String[] packages, AuthProvider ca) {
		try {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setScanners(new TypeElementsScanner(), new SubTypesScanner());
			cb.setUrls(ClasspathHelper.forPackage("org.cloudconfig", RouterPath.class.getClassLoader()));
			Arrays.stream(packages)
					.forEach(s -> cb.setUrls(ClasspathHelper.forPackage(s, RouterPath.class.getClassLoader())));

			// addMainApplicationClassPackage(cb);

			Reflections reflections = new Reflections(cb);

			Set<Class<? extends WebController>> controllers = reflections.getSubTypesOf(WebController.class);

			for (Class<? extends WebController> w : controllers) {

				WebController inst = w.newInstance();

				inst.setVertx(vertx);
				inst.setAuthProvider(ca);
				inst.setLoginUrl("/index.html");
				inst.routePath(router);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	// @SuppressWarnings("rawtypes")
	// private static String addMainApplicationClassPackage(ConfigurationBuilder
	// cb) {
	// try {
	// StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
	// for (StackTraceElement stackTraceElement : stackTrace) {
	// if ("main".equals(stackTraceElement.getMethodName())) {
	// Class o = Class.forName(stackTraceElement.getClassName());
	// String packages = o.getPackage().getName();
	// cb.setUrls(ClasspathHelper.forPackage(packages, o.getClassLoader()));
	// }
	// }
	// } catch (ClassNotFoundException ex) {
	// // Swallow and continue
	// }
	// return null;
	// }

}
