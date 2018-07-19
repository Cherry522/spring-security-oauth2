# spring-security-oauth2
用于提供RESTful API接口，用SpringBoot + Spring Security + oAuth2实现
##数据库准备：  
###从阿里镜像库下载官方镜像：
    `docker pull mysql:5.7`
###在服务器上运行mysql容器
（如果想开机启动可以将`-rm`替换成：`--restart=always`）<br>
`docker run --rm --name mysql  -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=demo_oauth2 -d mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --max_connections=500 --max_allowed_packet=5M`<br>
如果想数据外挂，可以添加如下命令：`-v /demo/mysql/data:/var/lib/mysql `

OAuth2为我们提供了四种授权方式：
>1、授权码模式（authorization code）<br/> 
>2、简化模式（implicit） <br/>
>3、密码模式（resource owner password credentials） <br/>
>4、客户端模式（client credentials）<br/>

本文用是密码模式：
>密码模式也是比较常用到的一种，客户端向授权服务器提供用户名、密码然后得到授权令牌。这种模式不过有种弊端，我们的客户端需要存储用户输入的密码，但是对于用户来说信任度不高的平台是不可能让他们输入密码的


.md2语法使用参考：https://blog.csdn.net/ljc_563812704/article/details/53464039

原作者文章参考： https://blog.csdn.net/weixin_42033269/article/details/80086422

原作者代码参考：
    https://gitee.com/hengboy/spring-boot-chapter