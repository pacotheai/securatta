package securatta.auth

import groovy.util.logging.Slf4j
import ratpack.exec.Promise
import securatta.data.users.UserRepository
import securatta.data.users.UserCredentials
import securatta.data.users.UserToken
import securatta.util.Promises

import javax.inject.Inject

/**
 * @since 0.1.0
 */
@Slf4j
class AuthService {

    @Inject
    UserRepository repository

    /**
     * Authenticates a given user by the {@link UserCredentials} passed as
     * parameter. If the user is successfully authenticated a {@link UserToken}
     * will be provided
     *
     * @param credentials credentials used by the user for authentication
     * @return an {@link Optional} {@link UserToken}. It will be present if authentication succeeded
     * @since 0.1.0
     */
    Promise<Optional<UserToken>> authenticateUser(UserCredentials credentials) {
        return repository
            .findUserByUsernameAndPassword(credentials.username, credentials.password)
            .flatMap(repository.&createUserToken)
    }

    /**
     * Authenticates a given user by the {@link UserToken} passed as parameter
     *
     * @param token token used by the user for authentication
     * @return an {@link Optional} {@link UserToken}. It will be present if authentication succeeded
     * @since 0.1.0
     */
    Promise<Optional<UserToken>> authenticateToken(String token) {
        return repository
            .verifyToken(token)
            .map(Optional.&ofNullable)
            .onError(Promises.&emptyOptional)
    }
}
