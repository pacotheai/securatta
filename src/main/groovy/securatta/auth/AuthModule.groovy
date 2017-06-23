package securatta.auth

import com.google.inject.AbstractModule
import com.google.inject.Scopes

/**
 * Authentication module bindings
 *
 * @since 0.1.0
 */
class AuthModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Repository).to(CassandraRepository).in(Scopes.SINGLETON)
        bind(Service).to(ServiceImpl).in(Scopes.SINGLETON)

        bind(TokenProviderHandler).in(Scopes.SINGLETON)
        bind(TokenAuthenticationHandler).in(Scopes.SINGLETON)
    }
}
