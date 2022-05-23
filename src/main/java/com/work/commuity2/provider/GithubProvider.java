package com.work.commuity2.provider;


import com.alibaba.fastjson.JSON;
import com.work.commuity2.dto.AccessTokenDto;
import com.work.commuity2.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDto acessTokenDto){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");




        OkHttpClient client = new OkHttpClient();


            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(acessTokenDto));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();


            try (Response response = client.newCall(request).execute()) {
                String s=response.body().string();
                System.out.println(s);
                s=s.split("&")[0].split("=")[1];
                return s;
            } catch (IOException e) {
                e.printStackTrace();
            }

        return null;
    }

    public GithubUser getUser(String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token"+accessToken)
                .build();
        Response response = client.newCall(request).execute();
        String s=response.body().string();
        System.out.println(s);
        GithubUser githubUser = JSON.parseObject(s, GithubUser.class);
        return githubUser;
    }




}
