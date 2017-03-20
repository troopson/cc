# cc
config center, 中央配置服务器，用 vert.x编写。

## 编译安装

+ 1. 在应用目录下，执行 gradle clean jar, 编译出 cc-fat.jar
+ 2. 修改conf目录下的 conf.json , 设置用来存储配置文件的目录
+ 3. 执行 'java -jar cc-fat.jar -conf conf.json' 来启动服务，需要jdk8 
+ 4. 在浏览器中打开链接  'http://host:port/'，可以查看配置库中的配置文件.

## 使用

   存储目录的结构：第一级目录是项目名称，第二级目录是版本名称，在第二级目录中，是的配置文件。在目录中放置好配置文件，就可以通过 'GET: /read/[projectname]/[vername]/[filename]' 方式来获取配置文件内容。
   修改配置文件，可以通过登录后，在页面上进行修改。

springboot中，加载配置文件的示例

```java

 启动类
    public static void main(String[] args) throws Exception {
		SpringApplication a = new SpringApplication(new Object[]{App.class});
		a.addListeners(new CCServerListener());
		a.run(args);
	}
```

```java

@Order(10)
public class CCServerListener implements ApplicationListener<ApplicationEvent> {

	
	public static final String CCServer_url="ccserver.url";
	
	private static final Log logger = LogFactory.getLog(CCServerListener.class);
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		
		if (event instanceof ApplicationPreparedEvent) {
			initialize((ApplicationPreparedEvent) event);
		}
		
	}
	
	
	public void initialize(ApplicationPreparedEvent event) {
		ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
	

		try {
			String ccserverurl = environment.getProperty(CCServer_url);
			if(StringUtils.isEmpty(ccserverurl))
				return;
			
			Resource res = new UrlResource(ccserverurl);

			List<PropertySourceLoader> loaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class,
					getClass().getClassLoader());

			for (PropertySourceLoader loader : loaders) {
				if (canLoadFileExtension(loader, ccserverurl)) {
					PropertySource<?> specific = loader.load("cs_server", res, null);

					environment.getPropertySources().addLast(specific);

					return;
				}
			}

			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private boolean canLoadFileExtension(PropertySourceLoader loader, String url) {
		String filename = url.toLowerCase();
		for (String extension : loader.getFileExtensions()) {
			if (filename.endsWith("." + extension.toLowerCase())) {
				return true;
			}
		}
		return false;
	}	

}

```
## 说明

这个项目是在学习vertx过程中做的，一来学习一下vertx技术，二来做一个看起来有用的东西。
