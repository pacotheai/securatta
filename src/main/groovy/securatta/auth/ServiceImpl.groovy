package securatta.auth

import groovy.util.logging.Slf4j
import ratpack.exec.Promise
import securatta.data.users.UserCredentials
import securatta.data.users.UserToken
import securatta.util.Promises

import javax.inject.Inject

/**
 * @since 0.1.0
 */
@Slf4j
class ServiceImpl implements Service {

    @Inject
    Repository repository

    @Override
    Promise<Optional<UserToken>> authenticateUser(UserCredentials credentials) {
        return repository
            .findUserByUsernameAndPassword(credentials.username, credentials.password)
            .flatMap(repository.&createUserToken)
    }

    @Override
    Promise<Optional<UserToken>> authenticateToken(String token) {
        return repository
            .verifyToken(token)
            .map(Optional.&ofNullable)
            .onError(Promises.&emptyOptional)
    }
}
