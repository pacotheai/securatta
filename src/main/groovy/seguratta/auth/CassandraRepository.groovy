package seguratta.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Row
import com.datastax.driver.core.ResultSet
import groovy.util.logging.Slf4j
import ratpack.exec.Promise
import seguratta.Config
import seguratta.data.Cassandra
import seguratta.domain.User
import seguratta.domain.UserToken

import javax.inject.Inject

/**
 * Repository using Cassandra database to store user related data
 *
 * @since 0.1.0
 */
@Slf4j
class CassandraRepository implements Repository {

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

    @Override
    Promise<User> findUserByUsernameAndPassword(String username, String password) {
        String stmt = "SELECT * FROM seguratta.users WHERE username = ?"

        return Cassandra
            .executeAsync(cluster.connect(), stmt, username)
            .map(ResultSet::one)
            .map(CassandraRepository::toUser)
    }

    @Override
    Promise<UserToken> createUserToken(User user) {
        return Promise
            .value(user)
            .flatMap { User pUser ->
              createToken(pUser.username).map { String token ->
                  new UserToken(
                      user: new User(username: pUser.username),
                      token: token,
                      expirationDate: new Date()
                  )
              }
            }
    }

    Promise<String> createToken(String username) {
        return Promise
        .value(config.security.secret)
        .map(Algorithm::HMAC256)
        .map((Algorithm algorithm) -> {
              JWT
                .create()
                .withClaim("username", username)
                .withIssuer(config.security.issuer)
                .sign(algorithm)
             })
    }

    @Override
    Promise<UserToken> verifyToken(String token) {
        return Promise
        .value(token)
        .map(Algorithm::HMAC256)
        .map((Algorithm algorithm) -> {
              JWT
              .require(algorithm)
              .withIssuer(config.security.issuer)
              .build() //Reusable verifier instance
              .verify(token)
             })
        .map(CassandraRepository::toUser)
        .map((User user) -> new UserToken(user: user, token: token))
    }

    private static User toUser(Row row) {
        return new User(
            name: row.getString('name'),
            username: row.getString('username'))
    }

    private static User toUser(DecodedJWT jwt) {
        String name = jwt.getClaim('name').asString()
        String username = jwt.getClaim('username').asString()

        new User(username: username, name: name)
    }
}
