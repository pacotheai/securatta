package seguratta.auth

import ratpack.exec.Promise
import seguratta.domain.UserCredentials
import seguratta.domain.UserToken

/**
 *
 * @since 0.1.0
 */
interface Service {
    /**
     * Authenticates a given user by the {@link UserCredentials} passed as
     * parameter. If the user is successfully authenticated a {@link UserToken}
     * will be provided
     *
     * @param credentials credentials used by the user for authentication
     * @return an {@link Optional} {@link UserToken}. It will be present if authentication succeeded
     * @since 0.1.0
     */
    Promise<Optional<UserToken>> authenticateUser(UserCredentials credentials)

    /**
     * Authenticates a given user by the {@link UserToken} passed as parameter
     *
     * @param token token used by the user for authentication
     * @return an {@link Optional} {@link UserToken}. It will be present if authentication succeeded
     * @since 0.1.0
     */
    Promise<Optional<UserToken>> authenticateToken(String token)
}
