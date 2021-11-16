package com.loto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * Oauth2 server的配置类（需要继承特定的父类 AuthorizationServerConfigurerAdapter）
 */
@Configuration
@EnableAuthorizationServer  // 开启认证服务器功能
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TDAccessTokenConvertor tdAccessTokenConvertor;

    // jwt签名密钥
    private String sign_key = "td123";

    /**
     * 配置令牌端点的安全约束，接口访问权限 ------- 认证服务器最终是以api接口的方式对外提供服务（校验合法性并生成令牌、校验令牌等）
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);

        // 相当于打开 endpoints 访问接口的开关
        security
                .allowFormAuthenticationForClients()  // 允许客户端表单认证
                .tokenKeyAccess("permitAll()")        // 开启端口 /oauth/token_key 访问权限（允许）
                .checkTokenAccess("permitAll()");     // 开启端口 /oauth/check_token 访问权限（允许）
    }

    /**
     * 用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这初始化，可以把客户端详情信息写死或是通过数据库来存储调取详情信息
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);

        // 方式1：从内存中加载客户端详情
        //clients.inMemory()
        //        .withClient("client_td")     // 添加一个client配置，指定其 client_id
        //        .secret("abcxyz")                   // 指定客户端的密码/安全码
        //        .resourceIds("autodeliver")         // 指定客户端所能访问资源id清单，此处的资源id是需要在具体的资源服务器上也配置一样
        //        .authorizedGrantTypes("password", "refresh_token") // 认证类型/令牌颁发模式，可以配置多个在这里，但是不一定都用，具体使用哪种方式颁发token，需要客户端调用的时候传递参数指定
        //        .scopes("all");                     // 客户端的权限范围，配置为 all 表示全部

        // 方式2：从数据库中加载客户端详情
        clients.withClientDetails(createJdbcClientDetailsService());
    }

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcClientDetailsService createJdbcClientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        return jdbcClientDetailsService;
    }

    /**
     * 用来配置令牌（token）的访问端点和令牌服务(token services)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);

        endpoints
                .tokenStore(tokenStore())    // 指定 token 的存储方法
                .tokenServices(authorizationServerTokenServices())   // token服务的一个描述（生成细节的描述），比如有效时间多少等
                .authenticationManager(authenticationManager)        // 指定认证管理器，随后注入一个到当前类使用即可
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    /**
     * 用于创建 tokenStore 对象（令牌存储对象），token以什么形式存储
     */
    public TokenStore tokenStore() {
        // 使用内存存储 token
        //return new InMemoryTokenStore();

        // 使用 jwt 令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 获取一个 token 服务对象（该对象描述了 token 有效期等信息）
     */
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        // 使用默认实现
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true); // 是否开启令牌刷新
        defaultTokenServices.setTokenStore(tokenStore());

        // 针对 jwt 令牌的添加
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());

        // 设置令牌有效时间（一般设置为2个小时）
        // access_token 就是请求资源需要携带的令牌
        defaultTokenServices.setAccessTokenValiditySeconds(7200);

        // 设置刷新令牌的有效时间（此处设置为 3 天）
        defaultTokenServices.setRefreshTokenValiditySeconds(259200);

        return defaultTokenServices;
    }

    /**
     * 返回 jwt令牌转换器（帮助我们生成jwt令牌的），在这里，可以把签名密钥传递进去给转换器对象
     */
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(sign_key);               // 签名密钥
        jwtAccessTokenConverter.setVerifier(new MacSigner(sign_key));  // 验证时使用的密钥，和签名密钥保持一致
        jwtAccessTokenConverter.setAccessTokenConverter(tdAccessTokenConvertor);

        return jwtAccessTokenConverter;
    }

}
