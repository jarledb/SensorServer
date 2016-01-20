package no.stonehill.config;

import no.stonehill.domain.Apiuser;
import no.stonehill.persistence.AuthenticationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.NoResultException;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authenticationProvider(daoAuthenticationProvider)
                .authorizeRequests()
                .antMatchers(
                        "/src/main/webapp/css/**",
                        "/src/main/webapp/js/**",
                        "/src/main/webapp/img/**"
                ).permitAll()
                .antMatchers("/register/**").fullyAuthenticated()
//                .anyRequest().fullyAuthenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            try {
                Apiuser apiUser = authenticationRepository.getApiUserbyName(username);
                LOG.info("Got user: " + apiUser.getName());
                return new User(apiUser.getName(), apiUser.getPassword(), true, true, true, true, AuthorityUtils.createAuthorityList("USER"));
            } catch (NoResultException | EmptyResultDataAccessException e) {
                throw new UsernameNotFoundException("Could not find the user '" + username + "'");
            }
        };
    }
}
