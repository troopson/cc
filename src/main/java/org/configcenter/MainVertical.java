/**
 * 
 */
package org.configcenter;

import org.configcenter.filesystem.FileChangeHandler;
import org.configcenter.filesystem.WatchHandler;
import org.wertx.path.RouterPath;
import org.wertx.util.StringUtil;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

/**
 * @author 瞿建军       Email: jianjun.qu@istuary.com
 * 2017年2月17日
 */
public class MainVertical extends AbstractVerticle {

	public static Logger log = LoggerFactory.getLogger(MainVertical.class);
	
	
	public void start() throws Exception {
				

		Future<Void> fu = Future.future();
		
		this.loadConfig(fu);
		
		fu.setHandler(ar->{
			startServer();
		});
		
	
		
	}
	
	private void startServer(){
		JsonObject conf = vertx.getOrCreateContext().config();
		String host = conf.getString("host");
		int port = conf.getInteger("port", 7777);
		
				
		HttpServerOptions options = new HttpServerOptions();
		options.setCompressionSupported(true);
		HttpServer server = vertx.createHttpServer(options);		
		
		Router router = RouterPath.build(vertx,"org.configcenter");

		server.requestHandler(router::accept);
		
		startFileSystemMonitor(conf);
		
		
		EventBus bus = vertx.eventBus();		
		FileChangeHandler fc =new FileChangeHandler();
		bus.consumer(WatchHandler.FileEvent,fc::onChange);		
		
		listen(server, host , port);
	}
	
	
	
	private void listen(HttpServer server,String host,int port){	
		
		server.listen(port,host==null?"127.0.0.1":host,ar->{
			
			if (ar.succeeded()) {
				System.out.println("Config Server started, bind at "+host+":"+port);
				System.out.println("Usage:");
				System.out.println("GET: /read/[projectname]/[vername]/[filename]");
				System.out.println("GET: /watch/[projectname]/[vername]/[filename]?callback=[an url]");
			} else {
				ar.cause().printStackTrace();
				System.out.println("Failed to start!");
			}
		});
		
		
		
//		java.util.function.Consumer<T>
//		java.util.function.Function<T, R>
//		java.util.function.Predicate<T>
	}
	
	
	private void loadConfig(Future<Void> fu){
		JsonObject conf= vertx.getOrCreateContext().config();
		String cchost = conf.getString("ccserver.host");
		int ccport = conf.getInteger("ccserver.port",7777);
		String ccpath = conf.getString("ccserver.path");
		
		
		if(StringUtil.isNotBlank(cchost) && StringUtil.isNotBlank(ccpath)){
			
			Future<HttpClientResponse> future = Future.future();

			vertx.createHttpClient().getNow(ccport,cchost,ccpath, resp->{
				future.complete(resp);
			});
			
			future.setHandler(ar ->{
				if(ar.succeeded()){
					HttpClientResponse resp = ar.result();
					resp.bodyHandler(body->{
						if(body.length()>0){
							JsonObject job = body.toJsonObject();
							job.stream().forEach(entry->{
								conf.put(entry.getKey(), entry.getValue());
							});
							log.info("load cc server config:"+body);
					    }
						fu.complete();
					});
				}else{	
					fu.complete();
					log.info("load config fialed!");
				}				
			});
		
		}else{
			fu.complete();
		}
	}

	
	public void startFileSystemMonitor(JsonObject config){
		WorkerExecutor executor = vertx.createSharedWorkerExecutor("backend-jobs");
		
		EventBus bus = vertx.eventBus();
		
		FileChangeHandler fc =new FileChangeHandler();
		
		final WatchHandler w= new WatchHandler(bus,config);
		
		executor.executeBlocking(e-> {w.processEvent();},null);
		
		bus.consumer(WatchHandler.FileEvent,fc::onChange);		
		
	}
	

}
