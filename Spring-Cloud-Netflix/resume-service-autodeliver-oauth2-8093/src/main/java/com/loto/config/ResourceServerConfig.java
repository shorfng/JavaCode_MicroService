package com.loto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer  // 开启资源服务器功能
@EnableWebSecurity     // 开启web访问安全
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    // jwt 签名密钥
    private String sign_key = "td123";

    @Autowired
    private TDAccessTokenConvertor tdAccessTokenConvertor;

    /**
     * 用于定义资源服务器向远程认证服务器发起请求，进行token校验等事宜
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //// 设置当前资源服务的资源id
        //resources.resourceId("autodeliver");
        //
        //// 定义 token 服务对象（用于 token 校验）
        //RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        //
        //// 校验端点/接口设置
        //remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9999/oauth/check_token");
        //
        //// 携带客户端id和客户端安全码
        //remoteTokenServices.setClientId("client_td");
        //remoteTokenServices.setClientSecret("abcxyz");
        //
        //resources.tokenServices(remoteTokenServices);

        // jwt 令牌改造
        resources.resourceId("autodeliver").tokenStore(tokenStore()).stateless(true);// 无状态设置
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
     * 返回 jwt令牌转换器（帮助我们生成jwt令牌的），在这里，可以把签名密钥传递进去给转换器对象
     */
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(sign_key);               // 签名密钥
        jwtAccessTokenConverter.setVerifier(new MacSigner(sign_key));  // 验证时使用的密钥，和签名密钥保持一致
        jwtAccessTokenConverter.setAccessTokenConverter(tdAccessTokenConvertor);

        return jwtAccessTokenConverter;
    }

    /**
     * 场景：一个服务中可能有很多资源（API接口）<p>
     * 某一些API接口，需要先认证，才能访问<p>
     * 某一些API接口，压根就不需要认证，本来就是对外开放的接口<p>
     * 所以需要对不同特点的接口区分对待（在当前configure方法中完成），设置是否需要经过认证
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 设置session的创建策略（IF_REQUIRED 根据需要创建）
                .and()
                .authorizeRequests()
                .antMatchers("/autodeliver/**").authenticated()  // autodeliver 为前缀的请求需要认证
                .antMatchers("/demo/**").authenticated()         // demo 为前缀的请求需要认证
                .anyRequest().permitAll();  //  其他请求不认证
    }
}
