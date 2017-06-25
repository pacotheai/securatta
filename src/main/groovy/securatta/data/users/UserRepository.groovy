package securatta.data.users

import org.pac4j.jwt.profile.JwtGenerator
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Row
import groovy.util.logging.Slf4j
import ratpack.exec.Promise
import javax.inject.Inject
import securatta.Config
import securatta.data.Cassandra
import securatta.util.BCryptEncoder

/**
 * Repository using Cassandra database to store user related data
 *
 * @since 0.1.0
 */
@Slf4j
class UserRepository {

  static final String NAME = 'name'
  static final String USERNAME = 'username'

  /**
   * Cassandra cluster connection
   *
   * @since 0.1.0
   */
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
    INSERT INTO
      securatta.user (name, username)
    VALUES
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
   * Adds the user password to the user
   *
   * @param username user's username
   * @param password plain password
   * @return the {@link User} identified by the username
   * @since 0.1.3
   */
  Promise<User> createCredentials(String username, String password) {
    String stmt = """
    INSERT INTO
      securatta.user_credentials (username, password)
    VALUES
      (?,?)
    """

    BCryptEncoder encoder = new BCryptEncoder(salt: config.security.secret)
    String encodedPassword = encoder.encode(password)

    return Cassandra
    .executeAsync(cluster.connect(), stmt, username, encodedPassword)
    .flatMap {
      findByUsername(username)
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
      name: row.getString(NAME), username: row.getString(USERNAME),
    )
  }

  /**
   * @param user
   * @return
   * @since 0.1.0
   */
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

  /**
   *
   * @param username
   * @return
   * @since 0.1.0
   */
  Promise<String> createToken(String username) {
    return Promise
    .value(config.security.secret)
    .map { String secret ->
      JwtGenerator jwtGenerator = new JwtGenerator(new SecretSignatureConfiguration(secret))
      Map<String,Object> claims = [USERNAME: username] as Map<String, Object>

      jwtGenerator.generate(claims)
    }
  }

  /**
   * @param token
   * @return
   * @since 0.1.0
   */
  Promise<UserToken> verifyToken(String token) {
    return Promise
    .value(config.security.secret)
    .map { String secret ->
      new JwtAuthenticator().with {
        addSignatureConfiguration(new SecretSignatureConfiguration(secret))
        validateTokenAndGetClaims(token)
      }
    }.map { Map claims ->
      new User(username: "${claims.username}", name: "${claims.name}")
    }.map { User user ->
      new UserToken(user: user, token: token)
    }
  }
}
