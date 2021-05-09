package com.guorui.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guorui.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity  //启用Web安全功能
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;
    @Autowired
    CorsFilter corsFilter;
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .addFilter(corsFilter)
                .sessionManagement().invalidSessionUrl("/session/invalid").
                maximumSessions(1).sessionRegistry(sessionRegistry()).and()
                .and()
                .authorizeRequests()
                .antMatchers("/register","/session/invalid","/sendMail","/rePsw").permitAll()
                //而其他的请求都需要认证
                .anyRequest()
                .authenticated()
                .and()
                //修改Spring Security默认的登陆界面
                .formLogin()
                .permitAll()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        System.out.println("登陆成功");
                        Map<String, Object> map = new HashMap<>();
                        map.put("msg", "登录成功！");
                        map.put("principal", authentication.getPrincipal());
                        resp.setContentType("application/json;charset=utf-8");
                        //resp.setHeader("Access-Control-Allow-Origin","http://localhost:8081");

                        resp.setHeader("Access-Control-Allow-Origin","https://www.guorii.cn");
                        resp.setHeader("Access-Control-Allow-Credentials", "true");
                        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
                        resp.setHeader("Access-Control-Max-Age", "3600");
                        resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
                        PrintWriter out = resp.getWriter();
                        // 对象转json传输给前端
                        out.write(new ObjectMapper().writeValueAsString(map));
                        out.flush();
                        out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
                        System.out.println("登录失败");
                        Map<String, Object> map = new HashMap<>();
                        resp.setContentType("application/json;charset=utf-8");
                        //resp.setHeader("Access-Control-Allow-Origin","http://localhost:8081");

                        resp.setHeader("Access-Control-Allow-Origin", "https://www.guorii.cn");
                        resp.setHeader("Access-Control-Allow-Credentials", "true");
                        resp.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH");
                        resp.setHeader("Access-Control-Max-Age", "3600");
                        resp.addHeader("Access-Control-Allow-Headers", "token,accesstoken,Content-type");
                        System.out.println("设置resp" + resp.toString());
                        PrintWriter out = resp.getWriter();
                        if (e instanceof BadCredentialsException){
                            map.put("msg","账号或密码输入错误,登录失败！");
                        }else{
                            map.put("msg","出现异常,登录失败！");
                        }
                        // 对象转json传输给前端
                        out.write(new ObjectMapper().writeValueAsString(map));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .permitAll()
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("state", 200);
                        map.put("msg", "注销成功！");
                        resp.setContentType("application/json;charset=utf-8");
                        //resp.setHeader("Access-Control-Allow-Origin","http://localhost:8081");
                        resp.setHeader("Access-Control-Allow-Origin","https://www.guorii.cn");
                        resp.setHeader("Access-Control-Allow-Credentials", "true");
                        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
                        resp.setHeader("Access-Control-Max-Age", "3600");
                        resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
                        PrintWriter out = resp.getWriter();
                        // 对象转json传输给前端
                        out.write(new ObjectMapper().writeValueAsString(map));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll();
    }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            System.out.println("权限管理");
            auth.userDetailsService(userService);
        }
        @Bean
        PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}