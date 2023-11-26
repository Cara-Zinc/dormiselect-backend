package cs309.dormiselect.backend.config

import cs309.dormiselect.backend.domain.Account
import cs309.dormiselect.backend.repo.AccountRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.session.HttpSessionEventPublisher

@Configuration
@EnableWebSecurity
private class Security(@Autowired val accountRepo: AccountRepo) {
    private fun Account.asUserDetails() = object : UserDetails {
        override fun getAuthorities() = setOf(SimpleGrantedAuthority(this@asUserDetails::class.simpleName))

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
    fun httpSessionEventPublisher() = HttpSessionEventPublisher()

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