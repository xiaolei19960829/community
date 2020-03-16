package life.xl.community.provider;


import com.alibaba.fastjson.JSON;
import life.xl.community.dto.AccessTokenDTO;
import life.xl.community.dto.GitHUbUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: XiaoLei
 * @Date Created in 10:12 2020/3/16
 * 要传入五个参数，如果参数超过两个以上，就要封装成对象来做
 */
@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
         MediaType mediaType= MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string= response.body().string();
                String[] split = string.split("&");
                String tokenStr = split[0];
                String token = tokenStr.split("=")[1];
                return token;
            }catch (Exception e){
                e.printStackTrace();
            }
        return null;
    }

    public GitHUbUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //能够把string的json格式自动转为java类对象
            GitHUbUser gitHUbUser = JSON.parseObject(string, GitHUbUser.class);
            return gitHUbUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
