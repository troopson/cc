/**
 * 
 */
package org.configcenter.path;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wertx.path.Path;
import org.wertx.path.WebController;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

/**
 * @author 瞿建军 Email: jianjun.qu@istuary.com 2017年2月10日
 */

public class ConfigMainPath extends WebController {


	@Path(path = "/storeList", method = HttpMethod.GET)
	public void loadFileTree(RoutingContext routingContext) {

		// HttpServerRequest request = routingContext.request();
		HttpServerResponse response = routingContext.response();

		Future<List<Map<String, String>>> fu = Future.future();
		this.readDir(fu);
		fu.setHandler(ar -> {
			if (ar.failed()) {
				response.setStatusCode(500).end();
				log.error("Read center base dir failed.", ar.cause());
			} else {
				List<Map<String, String>> nodes = ar.result();
				response.putHeader("content-type", "application/json");
				response.setStatusCode(200);
				response.end(Json.encode(nodes));
			}
		});

	}

	private void readDir(Future<List<Map<String, String>>> fu) {

		List<Map<String, String>> nodes = new ArrayList<>();

		String dataDir = config().getString(ReadPath.Data_Dir);
		
		List<String> projects = vertx.fileSystem().readDirBlocking(dataDir);
		for (int i = 0; i < projects.size(); i++) {
			String project = projects.get(i);
			File projectDir = new File(project);

			String pid = projectDir.getName();//(i + 1) + "";
			Map<String, String> m = createNode("0", pid, projectDir.getName());
			nodes.add(m);

			if (!projectDir.isDirectory())
				continue;

			File[] verdir = projectDir.listFiles();
			for (int v = 0; v < verdir.length; v++) {
				File vdir = verdir[v];
				String vid = pid+"/"+vdir.getName();
				Map<String, String> m2 = createNode(pid, vid, vdir.getName());
				nodes.add(m2);

				if (!vdir.isDirectory())
					continue;

				File[] configs = vdir.listFiles();
				for (int f = 0; f < configs.length; f++) {
					File file = configs[f];
					String fid = vid +"/"+ file.getName();
					Map<String, String> m3 = createNode(vid, fid, file.getName());
					nodes.add(m3);
				}
			}
		}

	     System.out.println(nodes.toString());
	     fu.complete(nodes);

	}

	private Map<String, String> createNode(String parentId, String myid, String name) {
		Map<String, String> m2 = new HashMap<>();
		m2.put("id", myid);
		m2.put("pid", parentId);
		m2.put("name", name);
		m2.put("title", name);
		return m2;
	}

}
