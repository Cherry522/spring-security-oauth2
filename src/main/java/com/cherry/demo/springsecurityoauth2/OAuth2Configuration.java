package com.cherry.demo.springsecurityoauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.sql.DataSource;

/**
 * OAuth2配置
 * @author chenyan
 * @date 下午2:49
 */
@Configuration
public class OAuth2Configuration {

    /**
     * 配置资源服务器，控制哪些资源需要验证后可以访问，哪些资源不需要验证就能访问
     */
    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter{

        @Autowired
        CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

        @Autowired
        CustomLogoutSuccessHandler customLogoutSuccessHandler;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    //定义登出的连接和处理方式
                    .and()
                    .logout()
                    .logoutUrl("/oauth/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler)

                    .and()
                    .authorizeRequests()
                    .antMatchers("/hello/").permitAll()//定义"/hello/"这个资源不需要认证
                    .antMatchers("/secure/**").authenticated();//定义"/secure/"下的所有资源都需要验证
        }
    }


    /**
     * 配置OAuth2.0 授权服务机制
     */
    @Configuration
    @EnableAuthorizationServer//开启验证服务器
    protected static class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private DataSource dataSource;

        /**
         * 管理令牌（Managing Token）：
         * AuthorizationServerTokenServices 接口定义了一些操作使得你可以对令牌进行一些必要的管理，在使用这些操作的时候请注意以下几点：
         * 当一个令牌被创建了，你必须对其进行保存，这样当一个客户端使用这个令牌对资源服务进行请求的时候才能够引用这个令牌。
         * 当一个令牌是有效的时候，它可以被用来加载身份信息，里面包含了这个令牌的相关权限。
         * <p>
         * 当你自己创建 AuthorizationServerTokenServices 这个接口的实现时，你可能需要考虑一下使用 DefaultTokenServices 这个类，里面包含了一些有用实现，你可以使用它来修改令牌的格式和令牌的存储。默认的，当它尝试创建一个令牌的时候，是使用随机值来进行填充的，除了持久化令牌是委托一个 TokenStore 接口来实现以外，这个类几乎帮你做了所有的事情。并且 TokenStore 这个接口有一个默认的实现，它就是 InMemoryTokenStore ，如其命名，所有的令牌是被保存在了内存中。除了使用这个类以外，你还可以使用一些其他的预定义实现，下面有几个版本，它们都实现了TokenStore接口：
         * InMemoryTokenStore：这个版本的实现是被默认采用的，它可以完美的工作在单服务器上（即访问并发量压力不大的情况下，并且它在失败的时候不会进行备份），大多数的项目都可以使用这个版本的实现来进行尝试，你可以在开发的时候使用它来进行管理，因为不会被保存到磁盘中，所以更易于调试。
         * JdbcTokenStore：这是一个基于JDBC的实现版本，令牌会被保存进关系型数据库。使用这个版本的实现时，你可以在不同的服务器之间共享令牌信息，使用这个版本的时候请注意把"spring-jdbc"这个依赖加入到你的classpath当中。
         * JwtTokenStore：这个版本的全称是 JSON Web Token（JWT），它可以把令牌相关的数据进行编码（因此对于后端服务来说，它不需要进行存储，这将是一个重大优势），但是它有一个缺点，那就是撤销一个已经授权令牌将会非常困难，所以它通常用来处理一个生命周期较短的令牌以及撤销刷新令牌（refresh_token）。另外一个缺点就是这个令牌占用的空间会比较大，如果你加入了比较多用户凭证信息。JwtTokenStore 不会保存任何数据，但是它在转换令牌值以及授权信息方面与 DefaultTokenServices 所扮演的角色是一样的。
         * <p>
         * 定义的JdbcStore来操作数据库中的Token
         *
         * @return
         */
        @Bean
        public TokenStore tokenStore() {
            //可以用redis存储(RedisTokenStore)或者用数据库存储(JdbcTokenStore)，这里用数据库方式，如果用数据库方式存储会
            return new JdbcTokenStore(dataSource);
        }

        /**
         * 认证管理器，当你选择了资源所有者密码（password）授权类型的时候，请设置这个属性注入一个 AuthenticationManager 对象。
         */
        @Autowired
        @Qualifier("authenticationManager")
        private AuthenticationManager authenticationManager;

        /**
         * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
         * 配置授权类型（Grant Types）：
         * 授权是使用 AuthorizationEndpoint 这个端点来进行控制的，你能够使用 AuthorizationServerEndpointsConfigurer 这个对象的实例来进行配置(AuthorizationServerConfigurer 的一个回调配置项，见上的概述) ，如果你不进行设置的话，默认是除了资源所有者密码（password）授权类型以外，支持其余所有标准授权类型的（RFC6749），我们来看一下这个配置对象有哪些属性可以设置吧，如下列表：
         * authenticationManager：认证管理器，当你选择了资源所有者密码（password）授权类型的时候，请设置这个属性注入一个 AuthenticationManager 对象。
         * userDetailsService：如果啊，你设置了这个属性的话，那说明你有一个自己的 UserDetailsService 接口的实现，或者你可以把这个东西设置到全局域上面去（例如 GlobalAuthenticationManagerConfigurer 这个配置对象），当你设置了这个之后，那么 "refresh_token" 即刷新令牌授权类型模式的流程中就会包含一个检查，用来确保这个账号是否仍然有效，假如说你禁用了这个账户的话。
         * authorizationCodeServices：这个属性是用来设置授权码服务的（即 AuthorizationCodeServices 的实例对象），主要用于 "authorization_code" 授权码类型模式。
         * implicitGrantService：这个属性用于设置隐式授权模式，用来管理隐式授权模式的状态。
         * tokenGranter：这个属性就很牛B了，当你设置了这个东西（即 TokenGranter 接口实现），那么授权将会交由你来完全掌控，并且会忽略掉上面的这几个属性，这个属性一般是用作拓展用途的，即标准的四种授权模式已经满足不了你的需求的时候，才会考虑使用这个。
         * <p>
         * 在XML配置中呢，你可以使用 "authorization-server" 这个标签元素来进行设置。
         *
         * @param endpoints
         * @throws Exception
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints.tokenStore(tokenStore())//配置token的存储方式，可以用redis存储(RedisTokenStore)或者用数据库存储(JdbcTokenStore)
                    //认证管理器，当你选择了资源所有者密码（password）授权类型的时候，请设置这个属性注入一个 AuthenticationManager 对象。
                    .authenticationManager(authenticationManager)
            //配置授权端点的URL（Endpoint URLs）：
            //AuthorizationServerEndpointsConfigurer 这个配置对象(AuthorizationServerConfigurer 的一个回调配置项，见上的概述) 有一个叫做 pathMapping() 的方法用来配置端点URL链接，它有两个参数：
            //第一个参数：String 类型的，这个端点URL的默认链接。
            //第二个参数：String 类型的，你要进行替代的URL链接。
            //以上的参数都将以 "/" 字符为开始的字符串，框架的默认URL链接如下列表，可以作为这个 pathMapping() 方法的第一个参数：
            ///oauth/authorize：授权端点。
            ///oauth/token：令牌端点。
            ///oauth/confirm_access：用户确认授权提交端点。
            ///oauth/error：授权服务错误信息端点。
            ///oauth/check_token：用于资源服务访问的令牌解析端点。
            ///oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
//                    .pathMapping("/oauth/authorize","/oauth/aa")
            ;
        }

        /**
         * 用来配置令牌端点(Token Endpoint)的安全约束.
         *
         * @param security
         * @throws Exception
         */
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            super.configure(security);
        }

        /**
         * 配置客户端详情信息（Client Details)：
         * ClientDetailsServiceConfigurer (AuthorizationServerConfigurer 的一个回调配置项，见上的概述) 能够使用内存或者JDBC来实现客户端详情服务（ClientDetailsService），有几个重要的属性如下列表：
         * clientId：（必须的）用来标识客户的Id。
         * secret：（需要值得信任的客户端）客户端安全码，如果有的话。
         * scope：用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。
         * authorizedGrantTypes：此客户端可以使用的授权类型，默认为空。
         * authorities：此客户端可以使用的权限（基于Spring Security authorities）。
         * <p>
         * 客户端详情（Client Details）能够在应用程序运行的时候进行更新，可以通过访问底层的存储服务（例如将客户端详情存储在一个关系数据库的表中，就可以使用 JdbcClientDetailsService）或者通过 ClientDetailsManager 接口（同时你也可以实现 ClientDetailsService 接口）来进行管理。
         * （译者注：不过我并没有找到 ClientDetailsManager 这个接口文件，只找到了 ClientDetailsService）
         *
         * @param clients
         * @throws Exception
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients
                    .inMemory()//用内存方式获取，也可以改成从数据库方式获取
                    .withClient("client")//客户端id
                    .scopes("read", "write")//用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。
                    .authorities(Authorities.ROLE_ADMIN.name(), Authorities.ROLE_USER.name())
                    //authorizedGrantTypes可以取值：
                    //authorization_code：授权码类型。
                    //implicit：隐式授权类型。
                    //password：资源所有者（即用户）密码类型。
                    //client_credentials：客户端凭据（客户端ID以及Key）类型。
                    //refresh_token：通过以上授权获得的刷新令牌来获取新的令牌。
                    .authorizedGrantTypes("password", "refresh_token")
                    .secret("secret")//密码
                    .accessTokenValiditySeconds(1800);//设定客户端的access_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时).
        }
    }
}
