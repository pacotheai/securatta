package seguratta.auth

import ratpack.exec.Promise
import seguratta.domain.User
import seguratta.domain.UserToken

/**
 * Repository represents how user related data is going to be persisted
 *
 * @since 0.1.0
 */
interface Repository {

    /**
     * @param username
     * @param password
     * @return
     * @since 0.1.0
     */
    Promise<User> findUserByUsernameAndPassword(String username, String password)

    /**
     * @param user
     * @return
     * @since 0.1.0
     */
    Promise<UserToken> createUserToken(User user)

    /**
     * @param token
     * @return
     * @since 0.1.0
     */
    Promise<UserToken> verifyToken(String token)

}
