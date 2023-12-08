package cs309.dormiselect.backend.config

import cs309.dormiselect.backend.domain.account.Account
import cs309.dormiselect.backend.domain.account.Administrator
import cs309.dormiselect.backend.domain.account.Student
import cs309.dormiselect.backend.domain.account.Teacher
import cs309.dormiselect.backend.repo.AccountRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.web.session.HttpSessionEventPublisher

@Configuration
@EnableWebSecurity
private class Security(@Autowired val accountRepo: AccountRepo) {
    private fun Account.asUserDetails() = object : UserDetails {
        override fun getAuthorities() = buildSet {
            add(SimpleGrantedAuthority("account"))

            if (this@asUserDetails is Student || this@asUserDetails is Teacher || this@asUserDetails is Administrator) {
                add(SimpleGrantedAuthority("student"))
            }

            if (this@asUserDetails is Teacher || this@asUserDetails is Administrator) {
                add(SimpleGrantedAuthority("teacher"))
            }

            if (this@asUserDetails is Administrator) {
                add(SimpleGrantedAuthority("administrator"))
            }
        }

        override fun getUsername() = name

        override fun getPassword() = this@asUserDetails.password

        override fun isAccountNonExpired() = true

        override fun isAccountNonLocked() = true

        override fun isCredentialsNonExpired() = true

        override fun isEnabled() = true

        val account = this@asUserDetails
    }

    @Bean
    fun customUserDetailService() = UserDetailsService {
        accountRepo.findByName(it)?.asUserDetails() ?: throw UsernameNotFoundException("User $it not found.")
    }

    @Bean
    fun passwordEncoder() = PasswordEncoderFactories.createDelegatingPasswordEncoder()!!

    @Bean
    fun authenticationManager(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationManager {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService)
        authenticationProvider.setPasswordEncoder(passwordEncoder)

        return ProviderManager(authenticationProvider)
    }

    @Bean
    fun httpSessionEventPublisher() = HttpSessionEventPublisher()

    @Bean
    fun securityContextHolderStrategy() = SecurityContextHolder.getContextHolderStrategy()!!

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/api/account/**", authenticated)
                authorize("/api/student/**", hasAuthority("Student"))
                authorize("/api/teacher/**", hasAuthority("Teacher"))
                authorize("/api/administrator/**", hasAuthority("Administrator"))
                authorize(anyRequest, permitAll)
            }

            csrf {
                csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse()
                csrfTokenRequestHandler = CsrfTokenRequestAttributeHandler()
                if (System.getenv("CSRF_DISABLE") != null) {
                    disable()
                }
            }

            formLogin {
                //loginPage = "/api/login"
                //permitAll()
            }

            sessionManagement {
                sessionConcurrency {
                    maximumSessions = 1
                }
            }
        }
        return http.build()
    }
}

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@AuthenticationPrincipal(expression = "account")
annotation class CurrentAccount