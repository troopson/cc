/**
 * 
 */
package org.wertx.path;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.vertx.core.http.HttpMethod;

@Retention(RUNTIME)
@Target(ElementType.METHOD)

/**
 * @author 瞿建军       Email: jianjun.qu@istuary.com
 * 2017年2月10日
 */
public @interface Path {
	
	HttpMethod method();
	
	String path();
	
	boolean auth() default true;
	
	String permit() default "";
	

}
