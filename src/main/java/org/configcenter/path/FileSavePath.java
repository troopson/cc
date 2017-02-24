/**
 * 
 */
package org.configcenter.path;

import java.util.logging.Level;

import org.wertx.path.Path;
import org.wertx.path.WebController;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

/**
 * @author 瞿建军       Email: jianjun.qu@istuary.com
 * 2017年2月10日
 */

public class FileSavePath extends WebController {
	
	private String dataDir=null;

	@Path(path = "/saveContent/:project/:ver/:file", method= HttpMethod.POST)
	public void doRead(RoutingContext routingContext) {
		
		HttpServerRequest request = routingContext.request();
		
		String project = request.getParam("project");
		String ver = request.getParam("ver");
		String file = request.getParam("file");
							
		String content = request.getParam("content");
		
		if(this.dataDir==null)
			this.dataDir=config().getString("data_dir");
		
		String filePath = this.dataDir+"/"+project+"/"+ver+"/"+file;
		log.info(request.remoteAddress().host()+" read "+filePath);
				
//		System.out.println(content);
		Buffer data= Buffer.buffer(content);
		HttpServerResponse response = routingContext.response();
		
		vertx.fileSystem().writeFile(filePath, data, ar->{
			if(ar.succeeded()){
				response.putHeader("content-type", "text/html");
				response.setStatusCode(200);
				response.end();
			}else{
				response.setStatusCode(400);
				response.end();
				log.error(Level.WARNING, filePath+" not exists", ar.cause());
			}
			
		});	
		
	}
	

}
