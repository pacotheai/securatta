package securatta.auth

import groovy.util.logging.Slf4j
import ratpack.exec.Promise
import securatta.data.users.UserRepository
import securatta.data.users.UserCredentials
import securatta.data.users.UserToken

import javax.inject.Inject

/**
 * This service tries to determine whether someone or something is.
 *
 * @since 0.1.0
 */
@Slf4j
class AuthService {

    /**
     * This service uses the {@link UserRepository} in order to check
     * credentials against an underlying storage.
     *
     * @since 0.1.2
     */
    @Inject
    UserRepository repository

    /**
     * Authenticates a given user by the {@link UserCredentials} passed as
     * parameter. If the user is successfully authenticated a {@link UserToken}
     * will be provided
     *
     * @param credentials credentials used by the user for authentication
     * @return a {@link UserToken} promise.
     * @since 0.1.0
     */
    Promise<UserToken> authenticateUser(UserCredentials credentials) {
        return repository
            .findUserByUsernameAndPassword(credentials.username, credentials.password)
            .flatMap(repository.&createUserToken)
    }

    /**
     * Authenticates a given user by the {@link UserToken} passed as parameter
     *
     * @param token token used by the user for authentication
     * @return an {@link UserToken} promise.
     * @since 0.1.0
     */
    Promise<UserToken> authenticateToken(String token) {
        return repository.verifyToken(token)
    }
}
