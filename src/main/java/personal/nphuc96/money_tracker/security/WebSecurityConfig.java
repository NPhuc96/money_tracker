package personal.nphuc96.money_tracker.security;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.SessionManagementFilter;
import personal.nphuc96.money_tracker.security.filters.CustomCorsFilter;
import personal.nphuc96.money_tracker.security.filters.InitialAuthenticationFilter;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomCorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(corsFilter.corsFilter(), SessionManagementFilter.class)
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/api/login/**").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated();
        http.addFilter(initialAuthenticationFilter());


    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/api/registration/**");


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public InitialAuthenticationFilter initialAuthenticationFilter() throws Exception {
        final InitialAuthenticationFilter filter =
                new InitialAuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/api/login");

        return filter;
    }


}
