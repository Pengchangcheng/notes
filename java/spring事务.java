# spring事务在业务中使用的一些总结

1. 事务使用在希望保证一组业务操作中的数据库操作要么都执行，要么都不执行。
2. 事务使用的是mysql的innodb引擎。myisam不支持事务
3. mysql事务回滚是通过回滚日志实现的。在mysql中所有事务进行的修改都会先记录到这个回滚日志中，然后再执行相关操作。
回滚日志会先于数据持久化到磁盘上，即使数据库宕机也可以通过回滚日志来完成之前未完成的事务
4. 编程式事务管理，通过 TransactionTemplate或者TransactionManager手动管理事务，实际应用中很少使用
5. 声明式事务管理，实际是通过 AOP 实现；@Transactional
6. Spring 并不直接管理事务，而是提供了多种事务管理器 。Spring 事务管理器的接口是： PlatformTransactionManager 
7. spring事务管理接口：事务管理PlatformTransactionManager、事务属性TransactionDefinition、事务状态TransactionStatus
8. 事务传播行为是为了解决业务层方法之间互相调用的事务问题
9. @Transactional注解默认使用就是TransactionDefinition.PROPAGATION_REQUIRED。如果当前存在事务，则加入该事务；
如果当前没有事务，则创建一个新的事务。
10. TransactionDefinition.PROPAGATION_REQUIRES_NEW 创建一个新的事务，如果当前存在事务，则把当前事务挂起。
也就是说不管外部方法是否开启事务，Propagation.REQUIRES_NEW修饰的内部方法会新开启自己的事务，且开启的事务相互独立，互不干扰
11. TransactionDefinition.PROPAGATION_NESTED 如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，
则该取值等价于TransactionDefinition.PROPAGATION_REQUIRED；在外部方法未开启事务的情况下Propagation.NESTED和Propagation.REQUIRED 作用相同，
修饰的内部方法都会新开启自己的事务，且开启的事务相互独立，互不干扰。如果外部方法开启事务的话，Propagation.NESTED 修饰的内部方法属于外部事务的子事务，
***外部主事务回滚的话，子事务也会回滚，而内部子事务可以单独回滚而不影响外部主事务和其他子事务***
12. TransactionDefinition.PROPAGATION_MANDATORY
如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。（mandatory：强制性）
13. 若是错误的配置以下 3 种事务传播行为，事务将不会发生回滚，使用的很少。
	TransactionDefinition.PROPAGATION_SUPPORTS: 如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
	TransactionDefinition.PROPAGATION_NOT_SUPPORTED: 以非事务方式运行，如果当前存在事务，则把当前事务挂起。
    TransactionDefinition.PROPAGATION_NEVER: 以非事务方式运行，如果当前存在事务，则抛出异常
14. MySQL InnoDB 存储引擎的默认支持的隔离级别是 REPEATABLE-READ（可重读）
	对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，可以阻止脏读和不可重复读，但幻读仍有可能发生。
	***
	脏读、不可重复读、幻读 1. 脏读（事务可以读取未提交的数据） 2. 不可重复读（两次执行同样的查询，可能会得到不一样的结果） 
    3. 幻读（也是读取了提交的新事物，指增、删操作） 不可重复读重点在于update，而幻读的重点在于insert和delete。
    ***
    ***
	如果你一次执行单条查询语句，则没有必要启用事务支持，数据库默认支持 SQL 执行期间的读一致性；
	如果你一次执行多条查询语句，例如统计查询，报表查询，在这种场景下，多条查询 SQL 必须保证整体的读一致性，
    否则，在前条 SQL 查询之后，后条 SQL 查询之前，数据被其他用户改变，则该次整体的统计查询将会出现读数据不一致的状态，此时，应该启用事务支持
	***
15. 默认情况下，事务只有遇到运行期异常（RuntimeException 的子类）时才会回滚，Error 也会导致事务回滚，但是，在遇到检查型（Checked）异常时不会回滚。
	***在Java中所有不是RuntimeException派生的Exception都是检查型异常***
16.	@Transactional 的工作机制是基于 AOP 实现的，AOP 又是使用动态代理实现的。如果目标对象实现了接口，默认情况下会采用 JDK 的动态代理，如果目标对象没有实现了接口,会使用 CGLIB 动态代理
	createAopProxy() 方法 决定了是使用 JDK 还是 Cglib 来做动态代理
    DefaultAopProxyFactory 

17. 不支持private，public内部调用也会失效，走spring注解调用