package securatta.auth

import com.google.inject.AbstractModule
import com.google.inject.Scopes
import securatta.data.users.UserRepository

/**
 * Authentication module bindings
 *
 * @since 0.1.0
 */
class AuthModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AuthService).in(Scopes.SINGLETON)
        bind(UserRepository).in(Scopes.SINGLETON)
        bind(TokenProviderHandler).in(Scopes.SINGLETON)
        bind(TokenAuthenticationHandler).in(Scopes.SINGLETON)
    }
}
