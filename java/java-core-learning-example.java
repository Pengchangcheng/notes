1.浅拷贝和深拷贝的区别？
浅拷贝：对基本数据类型进行值传递，对引用数据类型进行引用传递般的拷贝；
深拷贝：对基本数据类型进行值传递，对引用数据类型，创建一个新的对象，并复制其内容。
-----------------------------------------------------------------------------
2.获取接口名称
this.getClass().getSimpleName()
-----------------------------------------------------------------------------
3.String是值传递还是引用传递？
值传递改变原来的值，引用传递传递变量的地址，不改变原来的值。
java只有值传递，String初始化以后无法修改，String比较特殊，是引用传递。
-----------------------------------------------------------------------------
4.PriorityQueue 采用堆排序
-----------------------------------------------------------------------------
5.给定 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0)。
找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。说明：你不能倾斜容器，且 n 的值至少为 2。
public class t1 {

    static Integer maxArea(Integer[] integers) {
        int maxValue = 0;
        int left = 0;
        int right = integers.length - 1;
        while (left < right) {
            int leftValue  = integers[left];
            int rightValue  = integers[right];
            maxValue = Math.max(maxValue, Math.min(leftValue, rightValue) * (right - 1));
            if (leftValue < rightValue) {
                left++;
            } else {
                right--;
            }
        }
        return maxValue;
    }

    public static void main(String[] args) {
        Integer[] integers = {1,8,6,2,5,4,8,3,7};
        System.out.println(maxArea(integers));
    }

}
-----------------------------------------------------------------------------
6.lambda函数
-----------------------------------------------------------------------------
7.Stream的使用:
创建/获取流 -> 中间操作（过滤、转换等） -> 终止操作（ 聚合、收集结果）
 (1)List<String> list = new ArrayList<>();
        list.add("aa");
        list.add("cccc");
        list.add("bbb");

        /**
         * Stream的使用:
         *  创建/获取流 -> 中间操作（过滤、转换等） -> 终止操作（ 聚合、收集结果）
         */
        list.stream().forEach(System.out::println);
        System.out.println();

        /**
         * 过滤
         *      collect语法 {@link StreamCollectTest}
         */
        List list0 = list.stream().filter(str -> str.startsWith("cc")).collect(Collectors.toList());
        List list1 = list.stream().filter(str -> str.startsWith("aa")).collect(Collectors.toList());

        list0.stream().forEach(System.out::println);
        list1.stream().forEach(System.out::println);
        System.out.println();

        /**
         * 转换
         */
        List list2 = list.stream().map(str -> str.replace("c","*")).collect(Collectors.toList());

        list2.stream().forEach(System.out::println);
        System.out.println();

        /**
         * 提取
         *      从skip开始至limit位置为止
         */
        List list3 = list.stream().skip(0).limit(1).collect(Collectors.toList());

        list3.stream().forEach(System.out::println);
        System.out.println();

        /**
         * 组合
         */
        List list4 = Stream.concat(list.stream(),list.stream()).collect(Collectors.toList());

        list4.stream().forEach(System.out::println);
        System.out.println();
 
(2) List<String> strList = Arrays.asList("a1", "a2", "c3", "c6", "c4");
        strList
        .stream()
        .filter(str -> str.startsWith("c"))
        .map(String::toUpperCase)
        .sorted()
        .forEach(System.out::println);
(3)// 去重
List result = list1.stream().distinct().collect(Collectors.toList());
-----------------------------------------------------------------------------
8.thread.Join把指定的线程加入到当前线程，可以将两个交替执行的线程合并为顺序执行的线程。比如在线程B中调用了线程A的Join()方法，
直到线程A执行完毕后，才会继续执行线程B。
-----------------------------------------------------------------------------
9.线程局部变量ThreadLocal对象不会被所有线程共享
私有变量会被所有线程共享
-----------------------------------------------------------------------------
10.Thread中start和run的区别?
start线程开始，但是还没执行，run线程立即执行
-----------------------------------------------------------------------------
11.RPC通信
package org.jee.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 描述:Rpc本地服务代理类
 * 1. 将本地接口调用转化为JDK的动态调用,在动态调用中实现接口的远程调用
 * 2. 创建Socket客户端,根据制定地址连接远程服务提供者
 * 3. 将远程服务调用所需的接口类,方法名,参数列表等编码后发送给服务提供者
 * 4. 同步阻塞等待服务端返回应答,获取应答后返回
 */
public class RpcImporter<S> {
    public S importer(final Class<?> serviceClass, final InetSocketAddress address) {
        // JDK动态代理,实现接口的远程调用
        return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass.getInterfaces()[0]},
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = null;
                        ObjectOutputStream output = null;
                        ObjectInputStream  input  = null;

                        try {
                            // 连接远程服务提供者
                            socket = new Socket();
                            socket.connect(address);

                            // 对象输出流
                            output = new ObjectOutputStream(socket.getOutputStream());
                            output.writeUTF(serviceClass.getName());
                            output.writeUTF(method.getName());
                            output.writeObject(method.getParameterTypes());
                            output.writeObject(args);

                            input = new ObjectInputStream(socket.getInputStream());
                            return input.readObject();
                        } finally {
                            if (socket != null) {
                                socket.close();
                            }
                            if (output != null) {
                                output.close();
                            }
                            if (input != null) {
                                input.close();
                            }
                        }
                    }
                });
    }
}

***************************************************************************************
package org.jee.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 描述:RPC服务发布者
 * Created by bysocket on 16/2/28.
 */
public class RpcExporter {
    // 创建线程池
    static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void exporter(String hostName,int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(hostName,port));
        try {
            while (true) {
                /**
                 * 监听Client的TCP连接,将其封装成Task,由线程池执行.
                 */
                executor.execute(new ExporterTask(serverSocket.accept()));
            }
        } finally {
          serverSocket.close();  
        }
    }

    /**
     * 线程Task:
     * 1. 将客户端发送的二进制流反序列化成对象,反射调用服务实现者,获取执行结果
     * 2. 将执行结果对象反序列化,通过Socket发送给客户端
     * 3. 远程服务调用完成之后,释放Socket等连接资源,防止句柄泄漏
     */
    private static class ExporterTask implements Runnable {
        Socket client = null;
        public ExporterTask(Socket accept) {
            this.client = accept;
        }

        @Override
        public void run() {
            ObjectInputStream   input  = null;
            ObjectOutputStream  output = null;
            try {
                // 对象输入流
                input = new ObjectInputStream(client.getInputStream());

                // 获取接口名
                String interfaceName = input.readUTF();
                // 获取方法名
                String methodName = input.readUTF();
                // 获取方法的参数数组
                Class<?>[] paramTypes = (Class<?>[]) input.readObject();
                // 获取传入参数对象数组
                Object[] arguments = (Object[]) input.readObject();

                // 获取服务对象类
                Class<?> service = Class.forName(interfaceName);
                // 获取服务方法
                Method method = service.getMethod(methodName,paramTypes);
                // 获取服务方法返回对象
                Object result = method.invoke(service.newInstance(),arguments);

                // 对象输出流
                output = new ObjectOutputStream(client.getOutputStream());
                output.writeObject(result);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭流的操作
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (client != null) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
***************************************************************************************
public interface EchoService {
    String echo(String ping);
}
***************************************************************************************
public class EchoServiceImpl implements EchoService {
    @Override
    public String echo(String ping) {
        return ping != null ? ping + " --> I am ok." : "I am ok.";
    }
}
***************************************************************************************
public class RpcTest {
    public static void main(String[] args) {
        // 启动服务提供者
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RpcExporter.exporter("localhost",8088);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 创建服务本地代理
        RpcImporter<EchoService> importer = new RpcImporter<>();

        // 从服务本地代理获取服务对象类
        EchoService echo = importer.importer(EchoServiceImpl.class,new InetSocketAddress("localhost",8088));
        System.out.println(echo.echo("Are you OK?"));
    }
}

