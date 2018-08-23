1.获取类加载器信息
 ClassLoader loader = Thread.currentThread().getContextClassLoader();
 ----------------------------------------------------------------------------
 2.子类的初始化过程和主动引用：子类初始化，先初始化父类
 class Parent {
    static {
        System.out.println("Parent init");
    }
    public static int v = 100;
}
class Child extends Parent {
    static {
        System.out.println("Child  init");
    }
}
public class InitMain {
    public static void main(String[] args) {
//        new Child();// new关键字初始化 注释开启和未开启作比较
        System.out.println("======");
        System.out.println(Child.v); // 此时Child已经被加载，但未被初始化
    }
}
----------------------------------------------------------------------------
3.类加载，返回对象方法数组
Class clzStr = Class.forName("java.lang.String");
// 返回对象方法数组
Method[] methods = clzStr.getDeclaredMethods();
----------------------------------------------------------------------------
4.Final字段不会被引起初始化，参考2的父类常量v=100却会引起初始化。
class FinalFieldClass {
    public static final String CONST_STR = "CONSTSTR";

    static {
        System.out.println("FinalFieldClass init");
    }
}
public class UseFinalField {
    public static void main(String[] args) {
        System.out.println(FinalFieldClass.CONST_STR);
    }
}
//输出：CONSTSTR

