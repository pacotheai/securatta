package securatta.data.users

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Row
import groovy.util.logging.Slf4j
import ratpack.exec.Promise
import securatta.Config
import securatta.data.Cassandra

import javax.inject.Inject

/**
 * Repository using Cassandra database to store user related data
 *
 * @since 0.1.0
 */
@Slf4j
class UserRepository {

    static final String NAME = 'name'
    static final String USERNAME = 'username'

    @Inject
    Cluster cluster

    /**
     * Configuration is needed to get instance security
     * properties
     *
     * @since 0.1.0
     */
    @Inject
    Config config

    /**
     * Adds a new user
     *
     * @param fullName the human readable name
     * @param username the user's username
     * @return the created user entry
     * @since 0.1.2
     */
    Promise<User> create(String fullName, String username) {
        String stmt = """
            INSERT INTO securatta.user
              (name, username) VALUES
              (?,?)
        """

        return Cassandra
          .executeAsync(cluster.connect(), stmt, fullName, username)
          .map(Cassandra.&singleRow)
          .map {
            new User(name: fullName, username: username)
          }
    }

    /**
     * Searchs for a given user by username
     *
     * @param username the user's username
     * @return a {@link User} promise
     * @since 0.1.2
     */
    Promise<User> findByUsername(String username) {
        String stmt = "SELECT * FROM securatta.user WHERE username = ?"

        return Cassandra
            .executeAsync(cluster.connect(), stmt, username)
            .map(Cassandra.&singleRow)
            .map(UserRepository.&toUser)
    }

    static User toUser(Row row) {
        return new User(
            name: row.getString(NAME),
            username: row.getString(USERNAME),
        )
    }

    Promise<UserToken> createUserToken(User user) {
        return Promise
            .value(user)
            .flatMap { User pUser ->
              createToken(pUser.username).map { String token ->
                  new UserToken(
                      user: new User(username: pUser.username),
                      token: token,
                      expirationDate: new Date(),
                  )
              }
            }
    }

    Promise<String> createToken(String username) {
        Promise
            .value(config.security.secret)
            .map(Algorithm.&HMAC256)
            .map { Algorithm algorithm ->
              JWT
                .create()
                .withClaim(USERNAME, username)
                .withIssuer(config.security.issuer)
                .sign(algorithm)
            }
    }

    Promise<UserToken> verifyToken(String token) {
        return Promise
            .value(token)
            .map(Algorithm.&HMAC256)
            .map { Algorithm algorithm ->
              JWT
              .require(algorithm)
              .withIssuer(config.security.issuer)
              .build() //Reusable verifier instance
              .verify(token)

        }.map { DecodedJWT jwt ->
            String name = jwt.getClaim(NAME).asString()
            String username = jwt.getClaim(USERNAME).asString()

            new User(username: username, name: name)
        }.map { User user ->
            new UserToken(user: user, token: token)
        }
    }
}
