1.Session的定义
Session是另一种记录浏览器状态的机制，Http是无状态的，所以需要Session或者Cookie来记录状态。
Session报错在服务器中，Cookie保存在客户端，用户使用浏览器访问服务器的时候，服务器把用户的信息
以某种的形式记录在服务器，就是Session。Session比Cookie使用方便，Session可以解决Cookie解决不了的事情
【Session可以存储对象，Cookie只能存储字符串。】。
----------------------------------------------------------------------------------------
2.Cookie的定义
给每一个用户都发一个通行证，无论谁访问的时候都需要携带通行证，这样服务器就可以从通行证上确认用户的信息。
通行证就是Cookie
----------------------------------------------------------------------------------------
3.Session API
long getCreationTime();【获取Session被创建时间】

String getId();【获取Session的id】

long getLastAccessedTime();【返回Session最后活跃的时间】

ServletContext getServletContext();【获取ServletContext对象】

void setMaxInactiveInterval(int var1);【设置Session超时时间】

int getMaxInactiveInterval();【获取Session超时时间】

Object getAttribute(String var1);【获取Session属性】

Enumeration

 getAttributeNames();【获取Session所有的属性名】
void setAttribute(String var1, Object var2);【设置Session属性】

void removeAttribute(String var1);【移除Session属性】

void invalidate();【销毁该Session】

boolean isNew();【该Session是否为新的】
----------------------------------------------------------------------------------------
4.Cookie API
Cookie类用于创建一个Cookie对象

response接口中定义了一个addCookie方法，它用于在其响应头中增加一个相应的Set-Cookie头字段

request接口中定义了一个getCookies方法，它用于获取客户端提交的Cookie

常用的Cookie方法：

public Cookie(String name,String value)

setValue与getValue方法

setMaxAge与getMaxAge方法

setPath与getPath方法

setDomain与getDomain方法

getName方法
----------------------------------------------------------------------------------------
5.Cookie的流程：
浏览器访问服务器，如果服务器需要记录该用户的状态，就使用response向浏览器发送一个Cookie，浏览器会把Cookie
保存起来。当浏览器再次访问服务器的时候，浏览器会把请求的网址连同Cookie一同交给服务器。

浏览器本身没有任何Cookie

发送Cookie给浏览器是需要设置Cookie的时间，即Cookie的有效期
//设置Cookie的时间
 cookie.setMaxAge(1000);

Cookie具有不可跨域名性。浏览器判断一个网站是否能操作另一个网站的Cookie的依据是域名。

 //对Unicode字符进行编码  主要是中文乱码
 Cookie cookie = new Cookie("country", URLEncoder.encode(name, "UTF-8"));

 //经过URLEncoding就要URLDecoding  服务端解码
String value = URLDecoder.decode(cookies[i].getValue(), "UTF-8");
----------------------------------------------------------------------------------------
6.Cookie有效期
Cookie的有效期是通过setMaxAge()来设置的。

MaxAge=正数，浏览器会把Cookie写到硬盘中，只要还在MaxAge秒之前，登陆网站时该Cookie就有效【不论关闭了浏览器还是电脑】

MaxAge=负数，**Cookie是临时性的，仅在本浏览器内有效，关闭浏览器Cookie就失效了，Cookie不会写到硬盘中。
Cookie默认值就是-1。

MaxAge=0，则表示删除该Cookie。Cookie机制没有提供删除Cookie对应的方法，把MaxAge设置为0等同于删除Cookie
----------------------------------------------------------------------------------------
7.Cookie的域名， 跨域相关
Cookie的domain属性决定运行访问Cookie的域名。domain的值规定为“.域名”
Cookie cookie = new Cookie("name", "ouzicheng");
cookie.setMaxAge(1000);
cookie.setDomain(".****.com");
response.addCookie(cookie);
printWriter.write("使用www.****.com域名添加了一个Cookie,只要一级是****.com即可访问");
----------------------------------------------------------------------------------------
8.Session的生命周期和有效期
Session在用户第一次访问服务器Servlet，jsp等动态资源就会被自动创建，Session对象保存在内存里，可以直接使用
request对象获取得到Session对象。

如果访问HTML,IMAGE等静态资源Session不会被创建。

Session生成后，只要用户继续访问，服务器就会更新Session的最后访问时间，无论是否对Session进行读写，
服务器都会认为Session活跃了一次


由于会有越来越多的用户访问服务器，因此Session也会越来越多。为了防止内存溢出，服务器会把长时间没有活跃的Session
从内存中删除，这个时间也就是Session的超时时间。
Session的超时时间默认是30分钟，有三种方式可以对Session的超时时间进行修改
第一种方式：在tomcat/conf/web.xml文件中设置，时间值为20分钟，所有的WEB应用都有效
<session-config>
    <session-timeout>20</session-timeout>
</session-config>
第二种方式：在单个的web.xml文件中设置，对单个web应用有效，如果有冲突，以自己的web应用为准。
<session-config>
     <session-timeout>20</session-timeout>
</session-config>
第三种方式：通过setMaxInactiveInterval()方法设置
 //设置Session最长超时时间为60秒，这里的单位是秒
httpSession.setMaxInactiveInterval(60);
----------------------------------------------------------------------------------------
9.Session的有效期与Cookie的是不同的
Session的周期指的是不活动的时间，周期内没有访问则属性失效，周期内访问了则重新计时。
重启服务器，重载web应用Session也会失效，invalidate()可以是Session所有属性失效，removeAttribute()可以使
Session某个属性失效。
Cookie的生命周期是按照累计时间来算的，不管用户有没有访问过Session。
----------------------------------------------------------------------------------------
10.HTTP协议是无状态的，Session不能依据HTTP连接来判断是否为同一个用户。
服务器向用户浏览器发送了一个名为JESSIONID的Cookie，它的值是Session的id值。
其实Session依据Cookie来识别是否是同一个用户。
简单来说：Session 之所以可以识别不同的用户，依靠的就是Cookie

该Cookie是服务器自动颁发给浏览器的，不用我们手工创建的。该Cookie的maxAge值默认是-1，也就是说仅当前浏览器使用，
不将该Cookie存在硬盘中
----------------------------------------------------------------------------------------
11.浏览器禁用了Cookie，Session还能用吗？
禁用了Cookie，用户浏览器没有把Cookie带过去给服务器。
Java Web提供了解决方法：URL地址重写
HttpServletResponse类提供了两个URL地址重写的方法：
encodeURL(String url)
encodeRedirectURL(String url)
这两个方法会自动判断该浏览器是否支持Cookie，如果支持Cookie，重写后的URL地址就不会带有jsessionid了
【当然了，即使浏览器支持Cookie，第一次输出URL地址的时候还是会出现jsessionid（因为没有任何Cookie可带）】
response.sendRedirect(response.encodeURL(url));
URL地址重写的原理：将Session的id信息重写到URL地址中。服务器解析重写后URL，获取Session的id**。
这样一来，即使浏览器禁用掉了Cookie，但Session的id通过服务器端传递，还是可以使用Session来记录用户的状态。
----------------------------------------------------------------------------------------
12.Session禁用Cookie
----------------------------------------------------------------------------------------
13.利用Session防止表单重复提交
向Session域对象存入的数据是一个随机数【Token--令牌】
在session域中存储一个token
然后前台页面的隐藏域获取得到这个token
在第一次访问的时候，我们就判断seesion有没有值，如果有就比对。对比正确后我们就处理请求，接着就把session存储的数据给删除了
等到再次访问的时候，我们session就没有值了，就不受理前台的请求了！
----------------------------------------------------------------------------------------
14.一次性校验码
防止暴力猜测密码
生成验证码后，把验证码的数据存进Session域对象中，判断用户输入验证码是否和Session域对象的数据一致。

生成验证码图片，并将验证码存进Session域中，判断用户带过来验证码的数据是否和Session的数据相同。
----------------------------------------------------------------------------------------
15.Session和Cookie的区别
从存储方式上比较
Cookie只能存储字符串，如果要存储非ASCII字符串还要对其编码。
Session可以存储任何类型的数据，可以把Session看成是一个容器

从隐私安全上比较
Cookie存储在浏览器中，对客户端是可见的。信息容易泄露出去。如果使用Cookie，最好将Cookie加密
Session存储在服务器上，对客户端是透明的。不存在敏感信息泄露问题。

从有效期上比较
Cookie保存在硬盘中，只需要设置maxAge属性为比较大的正整数，即使关闭浏览器，Cookie还是存在的
Session的保存在服务器中，设置maxInactiveInterval属性值来确定Session的有效期。并且Session依赖于
名为JSESSIONID的Cookie，该Cookie默认的maxAge属性为-1。如果关闭了浏览器，该Session虽然没有从服务器中消亡，
但也就失效了。

从对服务器的负担比较
Session是保存在服务器的，每个用户都会产生一个Session，如果是并发访问的用户非常多，是不能使用Session的，
Session会消耗大量的内存。
Cookie是保存在客户端的。不占用服务器的资源。像baidu、Sina这样的大型网站，一般都是使用Cookie来进行会话跟踪。

从浏览器的支持上比较
如果浏览器禁用了Cookie，那么Cookie是无用的了！
如果浏览器禁用了Cookie，Session可以通过URL地址重写来进行会话跟踪。

从跨域名上比较
Cookie可以设置domain属性来实现跨域名
Session只在当前的域名内有效，不可夸域名
----------------------------------------------------------------------------------------
16.Cookie和Session共同使用
第一种方式：只需要在处理购买页面上创建Cookie，Cookie的值是Session的id返回给浏览器即可
  Cookie cookie = new Cookie("JSESSIONID",session.getId());
  cookie.setMaxAge(30*60);
  cookie.setPath("/****/");
  response.addCookie(cookie);
第二种方式： 在server.xml文件中配置，将每个用户的Session在服务器关闭的时候序列化到硬盘或数据库上保存。
但此方法不常用，知道即可






















