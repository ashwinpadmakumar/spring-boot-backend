package org.trebol.config;

import javax.crypto.SecretKey;
import javax.servlet.Filter;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;
import org.trebol.exceptions.CorsMappingParseException;

import org.trebol.security.JwtTokenVerifierFilter;
import org.trebol.security.JwtUsernamePasswordAuthenticationFilter;
import org.trebol.security.IAuthorizationHeaderParserService;

@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig
    extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final SecretKey secretKey;
  private final SecurityProperties securityProperties;
  private final CorsProperties corsProperties;
  private final IAuthorizationHeaderParserService<Claims> jwtClaimsParserService;

  @Autowired
  public SecurityConfig(UserDetailsService userDetailsService, SecretKey secretKey,
    SecurityProperties securityProperties, IAuthorizationHeaderParserService<Claims> jwtClaimsParserService,
    CorsProperties corsProperties) {
    this.userDetailsService = userDetailsService;
    this.secretKey = secretKey;
    this.securityProperties = securityProperties;
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.corsProperties = corsProperties;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .headers()
            .frameOptions().sameOrigin()
          .and()
        .cors()
          .and()
        .csrf()
            .disable()
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
        .logout()
            .logoutUrl("/public/logout")
            .invalidateHttpSession(true)
          .and()
        .addFilter(
            this.loginUrl("/public/login"))
        .addFilterAfter(
            new JwtTokenVerifierFilter(jwtClaimsParserService),
            JwtUsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
            .antMatchers(
              "/",
              "/public/login",
              "/public/register",
              "/public/about",
              "/public/categories",
              "/public/categories/*",
              "/public/products",
              "/public/products/*",
              "/public/receipt/*",
              "/public/checkout",
              "/public/checkout/validate")
                .permitAll()
            .antMatchers(
              "/data/customers",
              "/data/customers/*",
              "/data/images",
              "/data/images/*",
              "/data/people",
              "/data/people/*",
              "/data/product_categories",
              "/data/product_categories/*",
              "/data/products",
              "/data/products/*",
              "/data/sales",
              "/data/sales/*",
              "/data/salespeople",
              "/data/salespeople/*",
              "/data/sell_statuses",
              "/data/user_roles",
              "/data/users",
              "/data/users/*",
              "/access",
              "/access/*",
              "/profile")
                .authenticated();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(userDetailsService);
    return provider;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() throws CorsMappingParseException {
    return new CorsConfigurationSourceBuilder(corsProperties).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    Integer strength = securityProperties.getBcryptEncoderStrength();
    return new BCryptPasswordEncoder(strength);
  }

  private Filter loginUrl(String url) throws Exception {
    JwtUsernamePasswordAuthenticationFilter filter = new JwtUsernamePasswordAuthenticationFilter(
      super.authenticationManager(),
      securityProperties,
      secretKey);
    filter.setFilterProcessesUrl(url);
    return filter;
  }

}
