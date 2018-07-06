# spring-security-oauth2
用于提供RESTful API接口，用SpringBoot + Spring Security + oAuth2实现

参考链接：
    https://blog.csdn.net/weixin_42033269/article/details/80086422
    
数据库准备：
    从阿里镜像库下载官方镜像：
    `docker pull mysql:5.7`
在服务器上运行mysql容器（如果想开机启动可以将`-rm`替换成：`--restart=always`）：
`    docker run --rm \
    --name mysql \
    -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=123456 \
    -e MYSQL_DATABASE=demo_oauth2 \
    -d mysql:5.7 \
    --character-set-server=utf8mb4 \
    --collation-server=utf8mb4_unicode_ci \
    --max_connections=500 \
    --max_allowed_packet=5M`
如果想数据外挂，可以添加如下命令：`-v /demo/mysql/data:/var/lib/mysql `