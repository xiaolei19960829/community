package life.xl.community.controller;

import life.xl.community.dto.AccessTokenDTO;
import life.xl.community.dto.GitHUbUser;
import life.xl.community.mapper.UserMapper;
import life.xl.community.model.User;
import life.xl.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: XiaoLei
 * @Date Created in 10:03 2020/3/16
 * 配置授权路由
 */
@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.url}")
    private String redirectUri;

    //登录成功后，返回index
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GitHUbUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null){
            //登录成功
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(new Date());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            request.getSession().setAttribute("githubUser",githubUser);
            return "redirect:/";//重定向
        }else{
            //登录失败,重新登录
            return "redirect:/";
        }
    }
}
