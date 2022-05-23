package com.work.commuity2.controller;


import com.work.commuity2.dto.AccessTokenDto;

import com.work.commuity2.dto.GithubUser;
import com.work.commuity2.mapper.UserMapper;
import com.work.commuity2.model.User;
import com.work.commuity2.provider.GithubProvider;
import com.work.commuity2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AuthController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.Redirect_uri}")
    private String Redirect_uri;



    @Autowired
    private UserService userService;


    private GithubUser githubUser=new GithubUser();


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {


        System.setProperty("sun.net.client.defaultConnectTimeout", String
                .valueOf(1000000));// （单位：毫秒）
        System.setProperty("sun.net.client.defaultReadTimeout", String
                .valueOf(1000000)); // （单位：毫秒）


        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setState(state);
        accessTokenDto.setRedirect_uri(Redirect_uri);

/*        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        GithubUser githubUser =githubProvider.getUser(accessToken);

        System.out.println(githubUser.getName());*/



         //测试模拟
        githubUser.setId(33L);
        githubUser.setName("wwx1");
        githubUser.setAvatar_url("https://avatars.githubusercontent.com/u/24893223?s=400&u=e77dc993653b87e44e0d47028e74d4a92f3c8766&v=4");




        if(githubUser!=null){

            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatar_url());



            userService.createOrUpdate(user);


        /*如果登录成功,写入session和cookie*/
            response.addCookie(new Cookie("token", user.getToken()));




            return "redirect:/";

        }else{

            /*登录失败,重新登录*/
            System.out.println("登录失败");
        }


        return "redirect:/";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){

        System.out.println("退出登录");

        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }


}
