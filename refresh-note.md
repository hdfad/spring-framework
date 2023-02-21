## bean容器：BeanFactory、ApplicationContext
BeanFactory提供了对外提供bean的能力，ApplicationContext extends beanFactory 提供了对bean的扩展 比如事件发布机制、注解对象注入(@Bean、@PostConstruct、@Resource...)...

## AbstractApplicationContext#refresh() bean工厂、bean生命周期，bean的后置处理器
### 工厂初始化
准备容器→创建bean工厂→准备beanFactory→bean工厂扩展
### 容器初始化
bean处理器注册→初始化bean

## 准备工作

- 准备容器
```java
/**
 * 准备容器初始化系统资源：jvm信息、系统环境，启动时间,applicationListeners监听器容器...
 */
prepareRefresh()
```
- 创建bean工厂
```java
/**
 * annotation：
 * 创建一个新的bean工厂设置id：new
 * xml：
 * beanFactory扩展：是否允许同名bean覆盖、循环依赖
 * 读取解析文件到BeanDefinition(xml<dtd\xsd>，注解...)：BeanDefinitionReader
 */
obtainFreshBeanFactory()

        ↓

        /**
         * 解析xml，读取bean信息到BeanDefinition
         * new XmlBeanDefinitionReader()
         * 配置dtd、xsd解析器
         * 加载解析配置xml
         *  xml文件path->InputSource(InputStream)->Document->doRegisterBeanDefinitions<存储到DefaultListableBeanFactory# beanDefinitionMap beanDefinitionNames>
         */
        loadBeanDefinitions(beanDefinitionReader);

```

- 预初始化BeanFactory
```java
/**
 * 类加载器
 * spel解析器
 * ApplicationContextAwareProcessor
 */
prepareBeanFactory(beanFactory);

```

- 扩展BeanFactory(bean对象注入)
```java
/**
 * BeanFactoryPostProcessor扩展:PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
 * 
 * 1:解析注解配置类注解：ConfigurationClassParser#doProcessConfigurationClass → (beanDefinitionNames、beanDefinitionMap)
 * @Component、@ComponentScan、@Import、@ImportResource、@Configuration、@Bean...→（postProcessor.postProcessBeanDefinitionRegistry）
 *  invokeBeanDefinitionRegistryPostProcessors()
 *      ↓
 *      通过BeanDefinitionReader读取配置信息，同时提供对BeanDefinition的扩展
 *      this.reader.loadBeanDefinitions(configClasses);
 * 2:调用bean工厂后置处理器：
 *  invokeBeanFactoryPostProcessors()
 */
invokeBeanFactoryPostProcessors(beanFactory)
```
### ↑完成bean工厂从容器启动到bean工厂初始化


- 初始化并添加bean后置处理器(BeanPostProcessor)  todo
```java
/**
 * getBeanNamesForType获取BeanPostProcessor实现类beanName<beanDefinitionNames>并初始化
 * 顺序初始化：PriorityOrdered → Ordered → other
 * registerBeanPostProcessors→beanPostProcessors.add(beanPostProcessor)<List<BeanPostProcessor>>
 *      1:internalAutowiredAnnotationProcessor(@Autowired,@Value,@Inject,@Lookup)
 *      2:internalCommonAnnotationProcessor((@PostConstruct、@Resource)→JSR-250)
 *      3:扩展
 */
registerBeanPostProcessors(beanFactory);
```
- 初始化事件多播器
```java
/**
 * 默认事件多播器：SimpleApplicationEventMulticaster <> ApplicationEventMulticaster
 * 添加到 singletonObjects 中
 * 初始化bean完成之后publishEvent执行
 */
initApplicationEventMulticaster();
```

- 往多播器中注册监听器
```java
/**
 * 1:启动容器时加载的监听器applicationListeners
 * 2:ApplicationListener实现类
 */
registerListeners()
```

- refresh完成
```java
/**
 * publishEvent:事件发布
 * 
 * 
 */
finishRefresh()
```

- bean工厂初始化完成操作 todo
```java
/**
 * 
 */
finishBeanFactoryInitialization(beanFactory)
```

## bean初始化

- 初始化懒加载bean
```java

/**
 * beanDefinitionNames
 * FactoryBean:&beanName
 * 普通bean:beanName
 * 
 *  getBean(beanName);
 *  ↓
 *  getSingleton(beanName); → 三级缓存 → 循环依赖 / 存在 → getObjectForBeanInstance
 *  ↓
 *  dependsOn:解析@DependsOn、<depends-on> 依赖 注入 → getBean(beanName);
 *  ↓
 *  创建并实例化对象 createBean
 *      ↓
 *      bean提前初始化 resolveBeforeInstantiation(beanName, mbdToUse); 实例化前创建bean<InstantiationAwareBeanPostProcessor>，如：bean扩展代理
 *  ↓
 *  doCreateBean(beanName, mbdToUse, args)
 */
preInstantiateSingletons()
```

## doCreateBean

- 实例化bean
```java
/**
 * bean创建流程
 * createBeanInstance
 *      ↓
 *      determineConstructorsFromBeanPostProcessors;解析lookUp注解,返回实例化构造函数(=1)
 * ↓
 * instantiateBean:默认根据无参构造方法反射生成实例对象
 * ↓
 * BeanWrapper
 * 
 */
createBeanInstance(beanName, mbdToUse, args)
```

- 实例化后bean扩展
```java
/**
 *  1、获取注解注入属性(InjectionMetadata.InjectedElement)=>@Resource,@Autowired、@Value,@PostConstruct,@PreDestroy......
 *  2、缓存到injectionMetadataCache
 *  MergedBeanDefinitionPostProcessor#postProcessMergedBeanDefinition
 */
applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
```

- singletonFactories中缓存允许循环依赖的bean
```java
/**
 * earlySingletonExposure=>true=>addSingletonFactory
 */
addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
```
- bean属性填充
```java
/**
 * 1、属性填充扩展：是否进行容器属性填充：postProcessAfterInstantiation=>false:不进行属性填充
 * 2、注入模型注入，0, @Bean(autowire=Autowire.BY_TYPE)=>autowireByName、autowireByType
 * 3、bean后置处理器：InstantiationAwareBeanPostProcessor
 *      #1、解析对象依赖属性，实例化注入属性对象：bp.postProcessProperties(pvs, bw.getWrappedInstance(), beanName);
 *          Resource:CommonAnnotationBeanPostProcessor#postProcessProperties
 *          Autowired:AutowiredAnnotationBeanPostProcessor#postProcessProperties
 *          Inject:AutowiredAnnotationBeanPostProcessor#postProcessProperties
 *      #2、pvsToUse = bp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
 * 4、：applyPropertyValues
 * 
 */
populateBean(beanName, mbd, instanceWrapper);
```

- bean初始化
```java

initializeBean(beanName, exposedObject, mbd);
```