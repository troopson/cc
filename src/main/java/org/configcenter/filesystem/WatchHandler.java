/**
 * 
 */
package org.configcenter.filesystem;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

/**
 * @author 瞿建军 Email: jianjun.qu@istuary.com 2017年2月13日
 */
public class WatchHandler {

	public static final String DataDir="data_dir";
	public static final String FileEvent = "config.file.changed";

	private WatchService watcher;

	private Map<WatchKey, Path> keys;

	private EventBus eb;
	
	private JsonObject config;

	public WatchHandler(EventBus eb,JsonObject config) {
		try {
			this.watcher = FileSystems.getDefault().newWatchService();
			this.keys = new HashMap<>();
			this.eb = eb;
			this.config=config;
			this.registerAll(Paths.get(config.getString(DataDir)));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void handle(Future<String> event) {

	}

	private void registerAll(Path start) throws IOException {


		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

				WatchHandler.this.keys.put(key, dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processEvent() {
		for (;;) {

			WatchKey wk = null;

			try {
				wk = this.watcher.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Path p = keys.get(wk);
			Path root = Paths.get(config.getString(DataDir));
//			System.out.println("watch: " + p.toString());

			Set<String> mods=new HashSet<>();
			
			for (WatchEvent<?> event : wk.pollEvents()) {

				WatchEvent.Kind kind = event.kind();

//				System.out.println("event count:" + event.count());
				// TBD - provide example of how OVERFLOW event is handled
				if (kind == OVERFLOW)
					continue;

				// Context for directory entry event is the file name of entry
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path name = ev.context();
				Path child = p.resolve(name);

				// System.out.println("WatchEvent event on:"+child.toString());

				if (Files.isDirectory(child, NOFOLLOW_LINKS))
					continue;

				Path way = root.relativize(child);
				// print out event
				System.out.format("%s: %s\n", event.kind().name(), way.toString());

				 // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
				
                boolean valid = wk.reset();
//				System.out.println(valid);
    			if (!valid) {
    				keys.remove(wk);

    				// all directories are inaccessible
    				if (keys.isEmpty()) {
    					break;
    				}
    			}    			
    			
                mods.add(event.kind().name()+"@"+way.toString());    	
               
				
			}

			if(!mods.isEmpty()){
				for(String bf : mods){
					this.eb.send(FileEvent,bf);
//					this.eb.send(FileEvent,bf,ar->{
//						if(ar.succeeded())
//							System.out.println("received reply:"+ar.result().body());
//						
//					});
				}
			
			}
			// reset key and remove from set if directory no longer accessible
			

		}
	}

}
