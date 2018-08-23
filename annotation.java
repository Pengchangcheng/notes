1.注解定义：
同 classs 和 interface 一样，注解也属于一种类型，java 5.0引入。
通过@interface关键字定义
public @interface TestAnnotation {
}
------------------------------------------------------------------------------------------
2.注解的使用：贴标签
@TestAnnotation
public class Test {
}
------------------------------------------------------------------------------------------
3.元注解：
@Retention 
说明注解的的存活时间，
- RetentionPolicy.SOURCE 注解只在源码阶段保留，在编译器进行编译时它将被丢弃忽视。 
- RetentionPolicy.CLASS 注解只被保留到编译进行的时候，它并不会被加载到 JVM 中。 
- RetentionPolicy.RUNTIME 注解可以保留到程序运行的时候，它会被加载进入到 JVM 中，所以在程序运行时可以获取到它们
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
}
******************************************************************************
@Documented
将注解中的元素包含到 Javadoc 中去
******************************************************************************
@Target
指定了注解运用的地方，被限定了运用的场景
ElementType.ANNOTATION_TYPE 可以给一个注解进行注解
ElementType.CONSTRUCTOR 可以给构造方法进行注解
ElementType.FIELD 可以给属性进行注解
ElementType.LOCAL_VARIABLE 可以给局部变量进行注解
ElementType.METHOD 可以给方法进行注解
ElementType.PACKAGE 可以给一个包进行注解
ElementType.PARAMETER 可以给一个方法内的参数进行注解
ElementType.TYPE 可以给一个类型进行注解，比如类、接口、枚举
******************************************************************************
@Inherited
子类没有被任何注解应用的话，那么这个子类就继承了超类的注解
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@interface Test {}

@Test
public class A {}


public class B extends A {}
注解 Test 被 @Inherited 修饰，之后类 A 被 Test 注解，类 B 继承 A,类 B 也拥有 Test 这个注解。
******************************************************************************
@Repeatable
可重复。java 1.8 引入
@interface Persons {
    Person[]  value();
}


@Repeatable(Persons.class)
@interface Person{
    String role default "";
}


@Person(role="artist")
@Person(role="coder")
@Person(role="PM")
public class SuperMan{

}
------------------------------------------------------------------------------------------
4.注解属性
注解的属性也叫做成员变量。注解只有成员变量，没有方法。注解的成员变量在注解的定义中以“无形参的方法”形式来声明，
其方法名定义了该成员变量的名字，其返回值定义了该成员变量的类型。
在注解中定义属性时它的类型必须是 8 种基本数据类型外加 类、接口、注解及它们的数组。
注解中属性可以有默认值，默认值需要用 default 关键值指定。比如：
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {

    public int id() default -1;

    public String msg() default "Hi";

}
只有一个属性
public @interface Check {
    String value();
}
@Check("hi") 等效 @Check(value="hi")

没有任何属性，括号省略
public @interface Perform {}
@Perform
public void testMethod(){}
------------------------------------------------------------------------------------------
5.java预置注解
@Deprecated 标记过时元素
@Override 复写父类
@SuppressWarnings 阻止警告
@SafeVarargs 参数安全类型注解 java 1.7加入
@FunctionalInterface 函数式接口注解 java1.8引入
函数式接口 (Functional Interface) 就是一个具有一个方法的普通接口，函数式接口可以很容易转换为 Lambda 表达式
------------------------------------------------------------------------------------------
6.注解的提取
注解与反射，通过反射来获取注解的所有信息
注解通过反射获取。首先可以通过 Class 对象的 isAnnotationPresent() 方法判断它是否应用了某个注解
public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {}

然后通过 getAnnotation() 方法来获取 Annotation 对象。
public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {}

或者是 getAnnotations() 方法。
public Annotation[] getAnnotations() {}
前一种方法返回指定类型的注解，后一种方法返回注解到这个元素上的所有注解

如果获取到的 Annotation 如果不为 null，则就可以调用它们的属性方法了。比如
@TestAnnotation()
public class Test {

    public static void main(String[] args) {

        boolean hasAnnotation = Test.class.isAnnotationPresent(TestAnnotation.class);

        if ( hasAnnotation ) {
            TestAnnotation testAnnotation = Test.class.getAnnotation(TestAnnotation.class);

            System.out.println("id:"+testAnnotation.id());
            System.out.println("msg:"+testAnnotation.msg());
        }

    }

}
如果一个注解要在运行时被成功提取，那么 @Retention(RetentionPolicy.RUNTIME) 是必须的
------------------------------------------------------------------------------------------
7.使用场景
 APT（Annotation Processing Tool)
通过反射获取使用注解的场景，处理注解了的方法，进行自己需要的操作
************************************NoBug.java 使用了@Jiecha这个注解*********************************************
package ceshi;
import ceshi.Jiecha;


public class NoBug {

    @Jiecha
    public void suanShu(){
        System.out.println("1234567890");
    }
    @Jiecha
    public void jiafa(){
        System.out.println("1+1="+1+1);
    }
    @Jiecha
    public void jiefa(){
        System.out.println("1-1="+(1-1));
    }
    @Jiecha
    public void chengfa(){
        System.out.println("3 x 5="+ 3*5);
    }
    @Jiecha
    public void chufa(){
        System.out.println("6 / 0="+ 6 / 0);
    }

    public void ziwojieshao(){
        System.out.println("我写的程序没有 bug!");
    }

}
************************************@Jiecha注解本身*********************************************
package ceshi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Jiecha {

}
************************************@Jiecha注解本身*********************************************
package ceshi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Jiecha {
}
************************************处理注解 APT*********************************************
package ceshi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public class TestTool {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        NoBug testobj = new NoBug();

        Class clazz = testobj.getClass();

        Method[] method = clazz.getDeclaredMethods();
        //用来记录测试产生的 log 信息
        StringBuilder log = new StringBuilder();
        // 记录异常的次数
        int errornum = 0;

        for ( Method m: method ) {
            // 只有被 @Jiecha 标注过的方法才进行测试
            if ( m.isAnnotationPresent( Jiecha.class )) {
                try {
                    m.setAccessible(true);
                    m.invoke(testobj, null);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    errornum++;
                    log.append(m.getName());
                    log.append(" ");
                    log.append("has error:");
                    log.append("\n\r  caused by ");
                    //记录测试过程中，发生的异常的名称
                    log.append(e.getCause().getClass().getSimpleName());
                    log.append("\n\r");
                    //记录测试过程中，发生的异常的具体信息
                    log.append(e.getCause().getMessage());
                    log.append("\n\r");
                } 
            }
        }


        log.append(clazz.getSimpleName());
        log.append(" has  ");
        log.append(errornum);
        log.append(" error.");

        // 生成测试报告
        System.out.println(log.toString());

    }

}

------------------------------------------------------------------------------------------
8.注解面向AOP编程，在处理重复逻辑和日志方面运用广泛
注解 + 被注解的方法等 + APT处理注解的逻辑
用于对被注解的方法进行某些切面操作

