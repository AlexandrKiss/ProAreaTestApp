package co.proarea.configuration;

import co.proarea.security.JwtAccessDeniedHandler;
import co.proarea.security.JwtAuthenticationEntryPoint;
import co.proarea.security.jwt.JwtConfigurer;
import co.proarea.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint authenticationErrorHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private static final String HOME = "/";
    private static final String ADMIN_ENDPOINT = "/api/v1/admin/**";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/**";
    private static final String SWAGGER_API_ENDPOINT = "/v2/api-docs";
    private static final String SWAGGER_UI_ENDPOINT = "/swagger-ui.html";
    private static final String SWAGGER_RES_ENDPOINT = "/swagger-resources/**";
    private static final String SWAGGER_JSON_ENDPOINT = "/swagger.json";
    private static final String SWAGGER_WJ_ENDPOINT = "/webjars/**";

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, JwtAuthenticationEntryPoint authenticationErrorHandler, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationErrorHandler = authenticationErrorHandler;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationErrorHandler)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers(HOME,LOGIN_ENDPOINT,
                            SWAGGER_API_ENDPOINT,
                            SWAGGER_UI_ENDPOINT,
                            SWAGGER_RES_ENDPOINT,
                            SWAGGER_JSON_ENDPOINT,
                            SWAGGER_WJ_ENDPOINT)
                        .permitAll()
                    .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .apply(new JwtConfigurer(jwtTokenProvider));
    }
}