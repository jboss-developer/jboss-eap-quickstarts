package org.jboss.weld.bean;

import static org.jboss.weld.logging.Category.BEAN;
import static org.jboss.weld.logging.LoggerFactory.loggerFactory;
import static org.jboss.weld.logging.messages.BeanMessage.NON_SERIALIZABLE_CONSTRUCTOR_PARAM_INJECTION_ERROR;
import static org.jboss.weld.logging.messages.BeanMessage.NON_SERIALIZABLE_FIELD_INJECTION_ERROR;
import static org.jboss.weld.logging.messages.BeanMessage.NON_SERIALIZABLE_INITIALIZER_PARAM_INJECTION_ERROR;
import static org.jboss.weld.logging.messages.BeanMessage.NON_SERIALIZABLE_PRODUCER_PARAM_INJECTION_ERROR;
import static org.jboss.weld.logging.messages.BeanMessage.NON_SERIALIZABLE_PRODUCT_ERROR;
import static org.jboss.weld.logging.messages.BeanMessage.NULL_NOT_ALLOWED_FROM_PRODUCER;
import static org.jboss.weld.logging.messages.BeanMessage.ONLY_ONE_SCOPE_ALLOWED;
import static org.jboss.weld.logging.messages.BeanMessage.PRODUCER_CAST_ERROR;
import static org.jboss.weld.logging.messages.BeanMessage.PRODUCER_METHOD_WITH_TYPE_VARIABLE_RETURN_TYPE_MUST_BE_DEPENDENT;
import static org.jboss.weld.logging.messages.BeanMessage.PRODUCER_METHOD_WITH_WILDCARD_RETURN_TYPE_MUST_BE_DEPENDENT;
import static org.jboss.weld.logging.messages.BeanMessage.RETURN_TYPE_MUST_BE_CONCRETE;
import static org.jboss.weld.logging.messages.BeanMessage.USING_DEFAULT_SCOPE;
import static org.jboss.weld.logging.messages.BeanMessage.USING_SCOPE;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.NormalScope;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.Producer;
import javax.inject.Inject;
import javax.inject.Scope;

import org.jboss.weld.Container;
import org.jboss.weld.bootstrap.BeanDeployerEnvironment;
import org.jboss.weld.exceptions.DefinitionException;
import org.jboss.weld.exceptions.IllegalProductException;
import org.jboss.weld.exceptions.WeldException;
import org.jboss.weld.injection.CurrentInjectionPoint;
import org.jboss.weld.introspector.WeldMember;
import org.jboss.weld.manager.BeanManagerImpl;
import org.jboss.weld.metadata.cache.MetaAnnotationStore;
import org.jboss.weld.util.Beans;
import org.jboss.weld.util.reflection.Reflections;
import org.slf4j.cal10n.LocLogger;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;

/**
 * The implicit producer bean
 * 
 * @author Gavin King
 * @author David Allen
 * 
 * @param <X>
 * @param <T>
 * @param <S>
 */
public abstract class AbstractProducerBean<X, T, S extends Member> extends AbstractReceiverBean<X, T, S>
{

   private static final Function<Class<?>, Boolean> SERIALIZABLE_CHECK = new Function<Class<?>, Boolean>()
   {

      @Override
      public Boolean apply(Class<?> from)
      {
         return Reflections.isSerializable(from);
      }

   };

   // Logger for messages
   private static final LocLogger log = loggerFactory().getLogger(BEAN);

   // Underlying Producer represented by this bean
   private Producer<T> producer;

   // Passivation flags
   private boolean passivationCapableBean;
   private boolean passivationCapableDependency;

   // Serialization cache for produced types at runtime
   private final ConcurrentMap<Class<?>, Boolean> serializationCheckCache;

   /**
    * Constructor
    * 
    * @param declaringBean The declaring bean
    * @param beanManager The Bean manager
    */
   public AbstractProducerBean(String idSuffix, AbstractClassBean<X> declaringBean, BeanManagerImpl beanManager)
   {
      super(idSuffix, declaringBean, beanManager);
      serializationCheckCache = new MapMaker().makeComputingMap(SERIALIZABLE_CHECK);
   }

   @Override
   public abstract WeldMember<T, ? super X, S> getWeldAnnotated();

   @Override
   // Overriden to provide the class of the bean that declares the producer
   // method/field
   public Class<?> getBeanClass()
   {
      return getDeclaringBean().getBeanClass();
   }

   /**
    * Initializes the API types
    */
   @Override
   protected void initTypes()
   {
      if (getType().isArray() || getType().isPrimitive())
      {
         Set<Type> types = new HashSet<Type>();
         types.add(getType());
         types.add(Object.class);
         super.types = types;
      }
      else
      {
         super.initTypes();
      }
   }

   /**
    * Initializes the type
    */
   protected void initType()
   {
      try
      {
         this.type = getWeldAnnotated().getJavaClass();
      }
      catch (ClassCastException e)
      {
         Type type = Beans.getDeclaredBeanType(getClass());
         throw new WeldException(PRODUCER_CAST_ERROR, e, getWeldAnnotated().getJavaClass(), (type == null ? " unknown " : type));
      }
   }

   /**
    * Validates the producer method
    */
   protected void checkProducerReturnType()
   {
      if ((getWeldAnnotated().getBaseType() instanceof TypeVariable<?>) || (getWeldAnnotated().getBaseType() instanceof WildcardType))
      {
         throw new DefinitionException(RETURN_TYPE_MUST_BE_CONCRETE, getWeldAnnotated().getBaseType());
      }
      else if (getWeldAnnotated().isParameterizedType())
      {
         for (Type type : getWeldAnnotated().getActualTypeArguments())
         {
            if (!Dependent.class.equals(getScope()) && (type instanceof TypeVariable<?>))
            {
               throw new DefinitionException(PRODUCER_METHOD_WITH_TYPE_VARIABLE_RETURN_TYPE_MUST_BE_DEPENDENT, getWeldAnnotated());
            }
            else if (!Dependent.class.equals(getScope()) && (type instanceof WildcardType))
            {
               throw new DefinitionException(PRODUCER_METHOD_WITH_WILDCARD_RETURN_TYPE_MUST_BE_DEPENDENT, getWeldAnnotated());
            }
         }
      }
   }

   /**
    * Initializes the bean and its metadata
    */
   @Override
   public void initialize(BeanDeployerEnvironment environment)
   {
      getDeclaringBean().initialize(environment);
      super.initialize(environment);
      checkProducerReturnType();
      initPassivationCapable();
   }

   private void initPassivationCapable()
   {
      if (getWeldAnnotated().isFinal() && !Serializable.class.isAssignableFrom(getWeldAnnotated().getJavaClass()))
      {
         this.passivationCapableBean = false;
      }
      else
      {
         this.passivationCapableBean = true;
      }
      if (Container.instance().services().get(MetaAnnotationStore.class).getScopeModel(getScope()).isNormal())
      {
         this.passivationCapableDependency = true;
      }
      else if (getScope().equals(Dependent.class) && passivationCapableBean)
      {
         this.passivationCapableDependency = true;
      }
      else
      {
         this.passivationCapableDependency = false;
      }
   }

   @Override
   public boolean isPassivationCapableBean()
   {
      return passivationCapableBean;
   }

   @Override
   public boolean isPassivationCapableDependency()
   {
      return passivationCapableDependency;
   }

   @Override
   public Set<InjectionPoint> getInjectionPoints()
   {
      return getProducer().getInjectionPoints();
   }

   /**
    * Validates the return value
    * 
    * @param instance The instance to validate
    */
   protected void checkReturnValue(T instance)
   {
      if ((instance == null) && !isDependent())
      {
         throw new IllegalProductException(NULL_NOT_ALLOWED_FROM_PRODUCER, getProducer());
      }
      else if (instance != null)
      {
         boolean passivating = beanManager.getServices().get(MetaAnnotationStore.class).getScopeModel(getScope()).isPassivating();
         boolean instanceSerializable = isTypeSerializable(instance.getClass());
         if (passivating && !instanceSerializable)
         {
            throw new IllegalProductException(NON_SERIALIZABLE_PRODUCT_ERROR, getProducer());
         }
         InjectionPoint injectionPoint = Container.instance().services().get(CurrentInjectionPoint.class).peek();
         if ((injectionPoint != null) && (injectionPoint.getBean() != null))
         {
            if (!instanceSerializable && Beans.isPassivatingScope(injectionPoint.getBean(), beanManager))
            {
               if (injectionPoint.getMember() instanceof Field)
               {
                  if (!injectionPoint.isTransient() && (instance != null) && !instanceSerializable)
                  {
                     throw new IllegalProductException(NON_SERIALIZABLE_FIELD_INJECTION_ERROR, this, injectionPoint);
                  }
               }
               else if (injectionPoint.getMember() instanceof Method)
               {
                  Method method = (Method) injectionPoint.getMember();
                  if (method.isAnnotationPresent(Inject.class))
                  {
                     throw new IllegalProductException(NON_SERIALIZABLE_INITIALIZER_PARAM_INJECTION_ERROR, this, injectionPoint);
                  }
                  if (method.isAnnotationPresent(Produces.class))
                  {
                     throw new IllegalProductException(NON_SERIALIZABLE_PRODUCER_PARAM_INJECTION_ERROR, this, injectionPoint);
                  }
               }
               else if (injectionPoint.getMember() instanceof Constructor<?>)
               {
                  throw new IllegalProductException(NON_SERIALIZABLE_CONSTRUCTOR_PARAM_INJECTION_ERROR, this, injectionPoint);
               }
            }
         }
      }
   }

   @Override
   protected void checkType()
   {

   }

   protected boolean isTypeSerializable(final Class<?> clazz)
   {
      return serializationCheckCache.get(clazz);
   }

   @Override
   protected void initScope()
   {
      Set<Annotation> scopes = new HashSet<Annotation>();
      scopes.addAll(getWeldAnnotated().getMetaAnnotations(Scope.class));
      scopes.addAll(getWeldAnnotated().getMetaAnnotations(NormalScope.class));
      if (scopes.size() > 1)
      {
         throw new DefinitionException(ONLY_ONE_SCOPE_ALLOWED, getProducer());
      }
      if (scopes.size() == 1)
      {
         this.scope = scopes.iterator().next().annotationType();
         log.trace(USING_SCOPE, scope, this);
         return;
      }

      initScopeFromStereotype();

      if (this.scope == null)
      {
         this.scope = Dependent.class;
         log.trace(USING_DEFAULT_SCOPE, this);
      }
   }

   /**
    * This operation is *not* threadsafe, and should not be called outside
    * bootstrap
    * 
    * @param producer
    */
   public void setProducer(Producer<T> producer)
   {
      this.producer = producer;
   }

   public Producer<T> getProducer()
   {
      return producer;
   }

   /**
    * Creates an instance of the bean
    * 
    * @returns The instance
    */
   @Override
   public T create(final CreationalContext<T> creationalContext)
   {
      try
      {
         T instance = getProducer().produce(creationalContext);
         checkReturnValue(instance);
         return instance;
      }
      finally
      {
         if (getDeclaringBean().isDependent())
         {
            creationalContext.release();
         }
      }
   }

}