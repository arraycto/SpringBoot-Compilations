package com.wjwcloud.oauth.utils;

import com.wjwcloud.oauth.config.AuthConfig;
import com.wjwcloud.oauth.config.AuthSource;
import com.wjwcloud.oauth.request.AuthWeChatRequest;
import org.junit.Assert;
import org.junit.Test;

/**
 * <p>
 * UrlBuilder测试类
 * </p>
 */
public class UrlBuilderTest {
    @Test
    public void testUrlBuilder() {
        AuthConfig config = AuthConfig.builder()
            .clientId("appid-110110110")
            .clientSecret("secret-110110110")
            .redirectUri("https://xkcoding.com")
            .build();
        String build = UrlBuilder.fromBaseUrl(AuthSource.WECHAT.authorize())
            .queryParam("appid", config.getClientId())
            .queryParam("redirect_uri", config.getRedirectUri())
            .queryParam("response_type", "code")
            .queryParam("scope", "snsapi_login")
            .queryParam("state", "")
            .build(false);
        System.out.println(build);
        AuthWeChatRequest request = new AuthWeChatRequest(config);
        String authorize = request.authorize("state");
        System.out.println(authorize);
    }

    @Test
    public void build() {
        String url = UrlBuilder.fromBaseUrl("https://www.zhyd.me")
            .queryParam("name", "yadong.zhang")
            .build();
        Assert.assertEquals(url, "https://www.zhyd.me?name=yadong.zhang");

        url = UrlBuilder.fromBaseUrl(url)
            .queryParam("github", "https://github.com/zhangyd-c")
            .build();
        Assert.assertEquals(url, "https://www.zhyd.me?name=yadong.zhang&github=https://github.com/zhangyd-c");
    }

    @Test
    public void build1() {
        String url = UrlBuilder.fromBaseUrl("https://www.zhyd.me")
            .queryParam("name", "yadong.zhang")
            .build(true);
        Assert.assertEquals(url, "https://www.zhyd.me?name=yadong.zhang");

        url = UrlBuilder.fromBaseUrl(url)
            .queryParam("github", "https://github.com/zhangyd-c")
            .build(true);
        Assert.assertEquals(url, "https://www.zhyd.me?name=yadong.zhang&github=https%3A%2F%2Fgithub.com%2Fzhangyd-c");
    }
}
