package org.jboss.as.quickstarts.kitchensink.interceptor;

import javax.interceptor.InterceptorBinding;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;
@InterceptorBinding
 

@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD, FIELD})
@Qualifier

public @interface Log { }
