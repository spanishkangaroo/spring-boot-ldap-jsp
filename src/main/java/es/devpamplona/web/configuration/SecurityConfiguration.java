package es.devpamplona.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import es.devpamplona.web.ldap.CustomLdapUserDetailsContextMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private static String LDAP_URL;

	@Value("${ldap.url}")
	private void setLDAPURL(String property) {
		LDAP_URL = property;
	}

	private static String LDAP_MANAGER_DN;

	@Value("${ldap.managerDN}")
	private void setLDAPManagerDN(String property) {
		LDAP_MANAGER_DN = property;
	}

	private static String LDAP_MANAGER_PASSWORD;

	@Value("${ldap.managerPassword}")
	private void setLDAPManagerPassword(String property) {
		LDAP_MANAGER_PASSWORD = property;
	}

	private static String GROUP_SEARCH_BASE;

	@Value("${ldap.groupSearchBase}")
	private void setLDAPGroupSearchBase(String property) {
		GROUP_SEARCH_BASE = property;
	}

	private static String GROUP_SEARCH_FILTER;

	@Value("${ldap.groupSearchFilter}")
	private void setLDAPGroupSearchFilter(String property) {
		GROUP_SEARCH_FILTER = property;
	}

	private static String USER_SEARCH_BASE;

	@Value("${ldap.userSearchBase}")
	private void setLDAPUserSearchBase(String property) {
		USER_SEARCH_BASE = property;
	}

	private static String USER_SEARCH_FILTER;

	@Value("${ldap.userSearchFilter}")
	private void setLDAPUserSearchFilter(String property) {
		USER_SEARCH_FILTER = property;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// auth.authenticationProvider(ldapAuthenticationProvider());

	}

	@Configuration
	@Order(1)
	public static class MVCWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Bean
		public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() throws Exception {
			DefaultLdapAuthoritiesPopulator ldapAuthoritiesPopulator = new DefaultLdapAuthoritiesPopulator(
					ldapContextSource(), GROUP_SEARCH_BASE);
			ldapAuthoritiesPopulator.setGroupSearchFilter(GROUP_SEARCH_FILTER);
			return ldapAuthoritiesPopulator;
		}

		@Bean
		public DefaultSpringSecurityContextSource ldapContextSource() throws Exception {
			DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(LDAP_URL);
			contextSource.setUserDn(LDAP_MANAGER_DN);
			contextSource.setPassword(LDAP_MANAGER_PASSWORD);
			return contextSource;
		}

		@Bean
		public LdapUserSearch ldapUserSearch() throws Exception {
			LdapUserSearch ldapUserSearch = new FilterBasedLdapUserSearch(USER_SEARCH_BASE, USER_SEARCH_FILTER,
					ldapContextSource());
			return ldapUserSearch;
		}

		@Bean
		public LdapAuthenticator ldapAuthenticator() throws Exception {
			BindAuthenticator authenticator = new BindAuthenticator(ldapContextSource());
			authenticator.setUserSearch(ldapUserSearch());
			return authenticator;
		}

		@Bean
		public LdapAuthenticationProvider ldapAuthenticationProvider() throws Exception {
			LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(ldapAuthenticator(),
					ldapAuthoritiesPopulator());
			ldapAuthenticationProvider.setUserDetailsContextMapper(new CustomLdapUserDetailsContextMapper());
			return ldapAuthenticationProvider;
		}

		protected void configure(HttpSecurity http) throws Exception {
			http.authenticationProvider(ldapAuthenticationProvider())
					// access-denied-page: this is the page users will be
					// redirected to when they try to access protected areas.
					.exceptionHandling().accessDeniedPage("/login?error").and()

					// The intercept-url configuration is where we specify what
					// roles are
					// allowed access to what areas.
					// We will not force the connection to https for all the
					// pages,
					// although
					// it could be done here.
					// The access parameter is where the expressions are used to
					// control
					// which
					// roles can access specific areas. One of the most
					// important
					// things is
					// the order of the intercept-urls,
					// the most catch-all type patterns should at the bottom of
					// the
					// list as
					// the matches are executed
					// in the order they are configured below. So /**
					// (anyRequest())
					// should
					// always be at the bottom of the list.
					.authorizeRequests()
						.antMatchers("/login**").permitAll()
						.antMatchers("/resources/**").permitAll()
						.antMatchers("/*").access("hasRole('ROLE_USERS')")
					.anyRequest().authenticated()
					// .and().requiresChannel().anyRequest().requiresSecure()
					.and()

					// This is where we configure our login form.
					// login-page: the page that contains the login screen
					// login-processing-url: this is the URL to which the login
					// form
					// should
					// be submitted
					// default-target-url: the URL to which the user will be
					// redirected if
					// they login successfully
					// authentication-failure-url: the URL to which the user
					// will be
					// redirected if they fail login
					// username-parameter: the name of the request parameter
					// which
					// contains
					// the username
					// password-parameter: the name of the request parameter
					// which
					// contains
					// the password
					.formLogin()
						.loginPage("/login")
							.defaultSuccessUrl("/")
							.failureUrl("/login?error")
					//.loginProcessingUrl("/login")
					//.usernameParameter( "j_username" )
					//.passwordParameter( "j_password" )
					.and()
					// This is where the logout page and process is configured.
					// The
					// logout-url is the URL to send
					// the user to in order to logout, the logout-success-url is
					// where they
					// are taken if the logout
					// is successful, and the delete-cookies and
					// invalidate-session
					// make
					// sure that we clean up after logout
					.logout()
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login?logout");
		}
	}
}
