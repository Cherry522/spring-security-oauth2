# spring-security-oauth2
用于提供RESTful API接口，用SpringBoot + Spring Security + oAuth2实现

## 名词说明
### OAuth2.0
OAuth2.0是OAuth协议的延续版本，但不向后兼容OAuth 1.0即完全废止了OAuth1.0。 OAuth 2.0关注客户端开发者的简易性。要么通过组织在资源拥有者和HTTP服务商之间的被批准的交互动作代表用户，要么允许第三方应用代表用户获得访问的权限。同时为Web应用，桌面应用和手机，和起居室设备提供专门的认证流程。2012年10月，OAuth 2.0协议正式发布为RFC 6749.<br/> 

#### OAuth2.0里的Role：
>Client：可以理解成一个app，需要访问用户资源完成业务逻辑。
>Authorization server：授权服务器。主要用来向clients颁发access token并校验token有效性。
>Resource server：资源服务器。为Client提供资源访问服务，需要访问authorization server验证access token。
>Resource owner：可以认为是user，资源的拥有者。与authorization server交互完成身份认证和确认授权。
#### OAuth2为我们提供了四种授权方式：
>1、授权码模式（authorization code）<br/> 
>2、简化模式（implicit） <br/>
>3、密码模式（resource owner password credentials） <br/>
>4、客户端模式（client credentials）<br/>

本文用是密码模式：
>密码模式也是比较常用到的一种，客户端向授权服务器提供用户名、密码然后得到授权令牌。这种模式不过有种弊端，我们的客户端需要存储用户输入的密码，但是对于用户来说信任度不高的平台是不可能让他们输入密码的

#### OAuth2资料：
 Spring Security OAuth2 开发指南官方文档翻译:https://www.cnblogs.com/xingxueliao/p/5911292.html</br>
 阮一峰理解OAuth 2.0：http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html</br>

## 数据库准备
### 从阿里镜像库下载官方镜像
    `docker pull mysql:5.7`
### 在服务器上运行mysql容器
（如果想开机启动可以将`-rm`替换成：`--restart=always`）<br>
`docker run --rm --name mysql  -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=demo_oauth2 -d mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --max_connections=500 --max_allowed_packet=5M`<br>
如果想数据外挂，可以添加如下命令：`-v /demo/mysql/data:/var/lib/mysql `

### 测试
可以通过postman测试</br>
    
    访问地址，GET请求：
    http://127.0.0.1:8080/hello
    返回:
    Hello User!
    
    结果分析：
        因为我们定义"/hello/"这个资源不需要认证。
</br>

    访问地址，GET请求：
    http://127.0.0.1:8080/secure
    报错，返回：
    {
        "timestamp": 1532498438423,
        "status": 401,
        "error": "Unauthorized",
        "message": "Accesss Denied!",
        "path": "/secure"
    }
    结果分析：
    返回的结果是我们在类CustomAuthenticationEntryPoint里定义好的错误信息，因为我们定义"/secure/"下的所有资源都需要验证
</br>
    
    执行认证，获取对应的token，POST请求：
    用curl测试：
    curl -X POST -u client:secret -d 'grant_type=password&username=admin&password=admin' 'http://127.0.0.1:8080/oauth/token'
    
    http://127.0.0.1:8080/oauth/token
    需要在Basic Auth里输入Username为ClientDetailsServiceConfigurer里配置的withClient的值：client
                     输入Password为ClientDetailsServiceConfigurer里配置的secret的值：secret
                     然后点击Refresh heanders按钮
    加入参数：
        username为admin
        password为admin
        grant_type为password
        这些参数从数据库中获取
    返回：
    {
        "access_token": "d1f9cb70-08f3-4dff-a5ab-e71af80af843",
        "token_type": "bearer",
        "refresh_token": "380a2e78-f4ba-4113-82fc-16a03401ca4c",
        "expires_in": 1799,
        "scope": "read write"
    }
    结果分析：
    认证通过后，返回这些信息，这些信息是我们之前在程序里定义好的
    认证通过后，可以拿着access_token和refresh_token去访问受保护的资源了。
        127.0.0.1:8080/secure?access_token=d1f9cb70-08f3-4dff-a5ab-e71af80af843
    
</br>
    
    刷新token，POST请求：
    127.0.0.1:8080/oauth/token?grant_type=refresh_token&refresh_token=380a2e78-f4ba-4113-82fc-16a03401ca4c
    需要在Basic Auth里输入Username为配置文件application.properties中配置的authentication.oauth.clientid的值：yuqiyu_home_pc
                     恕瑞玛Password为配置文件application.properties中authentication.oauth.secret的值：yuqiyu_secret
                     然后点击Refresh heanders按钮
    加入参数：
        grant_type为refresh_token
        refresh_token为380a2e78-f4ba-4113-82fc-16a03401ca4c
        这些参数从数据库中获取
    返回：
    {
        "access_token": "4a05c111-cc9c-4e63-a7ad-ecee7ccd6153",
        "token_type": "bearer",
        "refresh_token": "380a2e78-f4ba-4113-82fc-16a03401ca4c",
        "expires_in": 1799,
        "scope": "read write"
    }
    结果分析：
    刷新认证通后，返回这些信息，这些信息是我们之前在程序里定义好的
    认证通过后，可以拿着access_token去访问受保护的资源了。
        127.0.0.1:8080/secure?access_token=d1f9cb70-08f3-4dff-a5ab-e71af80af843
    


## 参考链接
.md2语法使用参考：https://blog.csdn.net/ljc_563812704/article/details/53464039

原作者文章参考： https://blog.csdn.net/weixin_42033269/article/details/80086422

原作者代码参考：
    https://gitee.com/hengboy/spring-boot-chapter