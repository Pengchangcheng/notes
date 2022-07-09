1.定义 JDK1.5引入
把类型明确的工作推迟到创建对象或调用方法的时候才去明确的特殊的类型
参数化类型:
把类型当作是参数一样传递
<数据类型> 只能是引用类型
---------------------------------------------------------------------------
2.泛型的好处
代码更加简洁【不用强制转换】
程序更加健壮【只要编译时期没有警告，那么运行时期就不会出现ClassCastException异常】
可读性和稳定性【在编写集合的时候，就限定了类型】
---------------------------------------------------------------------------
3.继承
public interface Inter<T> {
    public abstract void show(T t);

}
子类明确泛型类的类型参数变量
public class InterImpl implements Inter<String> {}
子类不明确泛型类的类型参数变量
public class InterImpl<T> implements Inter<T> {}

实现类的要是重写父类的方法，返回值的类型是要和父类一样的！
类上声明的泛形只对非静态成员有效
---------------------------------------------------------------------------------------
4.通配符 ？
?号通配符表示可以匹配任意类型，任意的Java类都可以匹配
public void test(List<?> list){}
只能调用与对象无关的方法，不能调用对象与类型有关的方法
---------------------------------------------------------------------------------------
5.通配符上限
List<? extends Number>
List集合装载的元素只能是Number的子类或自身
  public static void main(String[] args) {


        //List集合装载的是Integer，可以调用该方法
        List<Integer> integer = new ArrayList<>();
        test(integer);

        //List集合装载的是String，在编译时期就报错了
        List<String> strings = new ArrayList<>();
        test(strings);

    }


    public static void test(List<? extends Number> list) {

    }
---------------------------------------------------------------------------------------
6.通配符下限
 <? super Type>
 传递进来的只能是Type或Type的父类
 public TreeSet(Comparator<? super E> comparator) {
        this(new TreeMap<>(comparator));
    }

无论是设定通配符上限还是下限，都是不能操作与对象有关的方法，只要涉及到了通配符，它的类型都是不确定的！
---------------------------------------------------------------------------------------
7.泛型擦除
泛型是提供给javac编译器使用的，编译器编译完带有泛形的java程序后，生成的class文件中将不再带有泛形信息，
以此使程序运行效率不受到影响，这个过程称之为“擦除”。




