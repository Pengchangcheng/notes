单例模式（Singleton Pattern）是 Java 中最简单的设计模式之一。这种类型的设计模式属于创建型模式，
它提供了一种创建对象的最佳方式。这种模式涉及到一个单一的类，该类负责创建自己的对象，同时确保只有单
个对象被创建。这个类提供了一种访问其唯一的对象的方式，可以直接访问，不需要实例化该类的对象。

注意：
1、单例类只能有一个实例。
2、单例类必须自己创建自己的唯一实例。
3、单例类必须给所有其他对象提供这一实例。

优点： 
1、在内存里只有一个实例，减少了内存的开销，尤其是频繁的创建和销毁实例（比如管理学院首页页面缓存）。 
2、避免对资源的多重占用（比如写文件操作）。

缺点：
没有接口，不能继承，与单一职责原则冲突，一个类应该只关心内部逻辑，而不关心外面怎么样来实例化。

使用场景： 
1、要求生产唯一序列号。 
2、WEB 中的计数器，不用每次刷新都在数据库里加一次，用单例先缓存起来。 
3、创建的一个对象需要消耗的资源过多，比如 I/O 与数据库的连接等。

注意事项：getInstance() 方法中需要使用同步锁 synchronized (Singleton.class) 防止多线程同时进入
造成 instance 被多次实例化，即线程不安全
-----------------------------------------------------------------------------------------------------
1.懒汉式，线程不安全
2.懒汉式，线程安全，方法加锁
3.饿汉式，来一次new一次，执行效率高，线程安全，但是浪费内存
4.双检锁，安全多线程下能保持高性能，有则直接返回，没有则判断锁再实例化
public class Singleton {  
    private volatile static Singleton singleton;  
    private Singleton (){}  
    public static Singleton getSingleton() {  
    if (singleton == null) {  
        synchronized (Singleton.class) {  
        if (singleton == null) {  
            singleton = new Singleton();  
        }  
        }  
    }  
    return singleton;  
    }  
}
5.登记式/静态内部类，类似双检锁，调用静态内部类时再实例化
public class Singleton {  
    private static class SingletonHolder {  
    private static final Singleton INSTANCE = new Singleton();  
    }  
    private Singleton (){}  
    public static final Singleton getInstance() {  
    return SingletonHolder.INSTANCE;  
    }  
}
6.枚举，更简洁，自动支持序列化机制，绝对防止多次实例化。
public enum Singleton {  
    INSTANCE;  
    public void whateverMethod() {  
    }  
}
-----------------------------------------------------------------------------------------------------
*：一般情况下，不建议使用第 1 种和第 2 种懒汉方式，建议使用第 3 种饿汉方式。只有在要明确实现 lazy loading 
效果时，才会使用第 5 种登记方式。如果涉及到反序列化创建对象时，可以尝试使用第 6 种枚举方式。如果有其他特殊的需求，
可以考虑使用第 4 种双检锁方式。

-----------------------------------------------------------------------------------------------------
from 菜鸟教程（http://www.runoob.com/design-pattern/singleton-pattern.html）