## bean容器：BeanFactory、ApplicationContext
BeanFactory提供了对外提供bean的能力，ApplicationContext extends beanFactory 提供了对bean的扩展 比如事件发布机制、注解对象注入(@Bean、@PostConstruct、@Resource...)...

## AbstractApplicationContext#refresh() bean工厂、bean生命周期，bean的后置处理器
### 工厂初始化
准备容器→创建bean工厂→准备beanFactory→bean工厂扩展
### 容器初始化
bean处理器注册→初始化bean

- 准备容器
```java
/**
 * 准备容器初始化系统资源：jvm信息、系统环境，启动时间...
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
 * 读取解析文件(xml<dtd\xsd>，注解...)：BeanDefinitionReader
 */
obtainFreshBeanFactory()
        
↓
        
/**
 * 解析xml
 * new XmlBeanDefinitionReader()
 * 配置dtd、xsd解析器
 * 加载解析配置xml
 *  xml文件path->InputSource(InputStream)->Document->doRegisterBeanDefinitions<存储到DefaultListableBeanFactory#beanDefinitionMap>
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
 * BeanFactoryPostProcessor扩展
 * 1:解析注解配置类注解：ConfigurationClassParser#doProcessConfigurationClass
 * @Component、@ComponentScan、@Import、@ImportResource、@Configuration、@Bean...→（postProcessor.postProcessBeanDefinitionRegistry）
 *  invokeBeanDefinitionRegistryPostProcessors()
 * 2:调用bean工厂后置处理器：
 *  invokeBeanFactoryPostProcessors()
 */
invokeBeanFactoryPostProcessors(beanFactory)
```
### ↑上述完成了bean工厂从容器启动到bean工厂初始化，对象扫描注入的一个过程


- 注册bean后置处理器
- 初始化事件多播器
- 注册监听器
- 加载bean(non-lazy-init)


