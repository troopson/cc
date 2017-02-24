/**
 * 
 */
package org.configcenter.path;

import java.util.logging.Level;

import org.wertx.path.Path;
import org.wertx.path.WebController;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

/**
 * @author 瞿建军       Email: jianjun.qu@istuary.com
 * 2017年2月10日
 */

public class ReadPath extends WebController {
	
	public static final String Data_Dir="data_dir";
	
	private String dataDir=null;

	@Path(path = "/read/:project/:ver/:file", method= HttpMethod.GET, auth=false)
	public void doRead(RoutingContext routingContext) {
		
		HttpServerRequest request = routingContext.request();
		
		String project = request.getParam("project");
		String ver = request.getParam("ver");
		String file = request.getParam("file");
							
		if(this.dataDir==null)
			this.dataDir=config().getString(Data_Dir);
		
		String filePath = this.dataDir+"/"+project+"/"+ver+"/"+file;
		log.info(request.remoteAddress().host()+" read "+filePath);
				
		vertx.fileSystem().readFile(filePath, result->{
			System.out.println("read file here============");
			if(result.succeeded()){
				try{
					HttpServerResponse response = routingContext.response();
					response.putHeader("content-type", "text/html");
					response.setStatusCode(200);
					response.end(result.result());
				}catch(Exception e){
					e.printStackTrace();
				}
//						System.out.println(result.result().toString());
				
			}else{
				HttpServerResponse response = routingContext.response();
				response.setStatusCode(400);
				response.end("ERR:400, "+filePath+" not exists");
				log.error(Level.WARNING, filePath+" not exists", result.cause());

			}
		});
			
		
	}
	
	
	@Path(path = "/watch/:project/:ver/:file", method= HttpMethod.GET, auth=false)
	public void doRegist(RoutingContext routingContext) {
		
		HttpServerRequest request = routingContext.request();
		
		String project = request.getParam("project");
		String ver = request.getParam("ver");
		String file = request.getParam("file");
		String callback = request.getParam("callback");
		
		String filePath =project+"/"+ver+"/"+file;
		
		log.info("regist callback "+callback+" on "+filePath);
//		System.out.println(callback);
		
		request.response().setStatusCode(200).end("ok");
		
	}

}
