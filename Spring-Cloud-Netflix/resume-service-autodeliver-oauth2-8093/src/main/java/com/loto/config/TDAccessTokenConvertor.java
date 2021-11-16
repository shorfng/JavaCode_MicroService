package com.loto.config;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TDAccessTokenConvertor extends DefaultAccessTokenConverter {
    // 资源服务器获取 JWT 令牌中的扩展信息
    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication oAuth2Authentication = super.extractAuthentication(map);

        // 将map放入认证对象中，认证对象在 controller 中可以拿到
        oAuth2Authentication.setDetails(map);
        return oAuth2Authentication;
    }
}
