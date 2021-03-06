1.IoC控制反转
将对象之间的依赖交给IoC容器来控制，实现解耦和提高代码可测试性。
解决的问题是如果将对象之间的依赖交给对象管理，会导致代码高度耦合和可测试性降低。
----------------------------------------------------------------------------------------
2.BeanDefinition
Spring通过定义BeanDefinition来管理基于Spring的应用中各种对象以及它们之间的互相依赖的关系，对IoC容器
来说，BeanDefinition就是对依赖反转模式中管理的对象依赖关系的数据抽象。
----------------------------------------------------------------------------------------
3.BeanFactory&FactoryBean
一个是Factory，是IoC容器或者对象工厂，spring中所有Bean都是由BeanFactory来管理的。一个是Bean，不是
一个简单的Bean，是一个能产生或者修饰对象生成的工厂Bean。
public interface BeanFactory{}
public interface FactoryBean<T> {}
----------------------------------------------------------------------------------------
4.XmlBeanFactory（3.1已经废弃）
读取xml配置文件
替代方法：
（1）在不改变处理逻辑的基础上，改为
Resource resource=new ClassPathResource("applicationContext.xml");  
BeanFactory factory=new DefaultListableBeanFactory();  
BeanDefinitionReader bdr=new XmlBeanDefinitionReader((BeanDefinitionRegistry) factory1);  
bdr.loadBeanDefinitions(resource); 
BeanFactory在启动的时候，不会创建bean的实例，而是在getBean()的时候，才会创建Bean的实例
factory.getBean("beanClass");

（2）使用ApplicationContext
ApplicationContext sc = new ClassPathXmlApplicationContext("applicationContext.xml");
ApplicationContext在读取配置文件的时候，配置文件中的bean就会被实例化（不考虑bean的作用域）
----------------------------------------------------------------------------------------
5.ApplicationContext
（1）支持不同的信息源。涉及国际化
（2）访问资源
（3）支持应用事件
（4）附加服务
----------------------------------------------------------------------------------------
6.IoC容器的初始化
IoC容器的初始化包裹BeanDefinition的Resource定位、载入、和注册这三个基本过程。

----------------------------------------------------------------------------------------
7.AOP 面向切面 模块化
----------------------------------------------------------------------------------------
8.Spring + JDBC  OR ORM
----------------------------------------------------------------------------------------
9.Spring 事务及实现原理
事务处理拦截器
ApplicationContext.xml org.springframework.jdbc.datasource.DataSourceTransactionManage

----------------------------------------------------------------------------------------
10.Spring技术内幕贴了大量代码，看的烦躁。