<html>
<body>
<h2>Hello World!</h2>
</body>
</html>
<%--
WEB-INF下资源访问问题
web-inf目录是不对外开放的，外部没办法直接访问到（即不能通过URL访问）。
所有只能通过映射来访问，比如映射为一个action或者servlet通过服务器端跳转来访问到具体的页面。
这样可以限制访问，提高安全性。

1、把页面资源文件只能放在webroot下面,如 CSS,JS,image等.放在WEB-INF下引用不了。

2、只能用转发方式来访问WEB-INF目录下的JSP,不用采用重定向的方式请求该目录里面的任何资源。
重定向方式: 如struts-config文件中配置<forward name="success" path="/WEB-INF/main.jsp" redirect="true"/>
或在action中response.sendRedirect("/error.jsp");
重定向的含义就是服务器把地址发给客户端,让客户端去访问.这种办法显然针对WEB-INF目录是无用功.

3、WEB-INF目录下文件访问资源文件时,可以忽略WEB-INF这一层目录。
如index.jsp 要用css目录里的一个css文件.
<link rel="stylesheet" type="text/css" href="css/**.css " />这样就行了,
从客户端的地址可以看出来服务器转向index.jsp就是在webroot下面.所以index.jsp和css目录可以讲是同一级目录。

4、WEB-INF目录下的文件之间如何访问呢。
（1）方式一：
用<a href="oa.do ">测试OA的路径</a>
或者<jsp:forward page ="/WEB-INF/jsp/test/test.jsp" />
访问。
（2）方式二：在action类中或者struts.xml中
如果main.jsp有很多处链接到WEB-INF目录下的其它页面.那就得有10个转向Action。这个可以用DispatchAction类加参数专门处理转向工作。
request.getRequestDispatcher("/WEB-INF /main.jsp").forward(request, response);

最后：在jsp页面中访问js、css等静态资源时，推荐使用绝对路径，避免路径问题而导致页面效果出不来。
获取项目绝对路径(根路径)的方法：<%path=request.getContextPath();%>
在需要访问的资源路径前加上path即可，如：path/js/jquery.js。
--%>