package org.jboss.as.quickstarts.kitchensink.interceptor;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.logging.Logger;


@Interceptor
@Log  //binding the interceptor here. now any method annotated with @Log would be intercepted by logMethodEntry
public class LoggingInterceptor implements Serializable {
 
	private static final Logger logger =
			Logger.getLogger(LoggingInterceptor.class);    
	@AroundInvoke
        public Object logMethodEntry(InvocationContext ctx) throws Exception {
           logger.info("Before entering method:" + ctx.getMethod().getName());
           return ctx.proceed();
    }
}