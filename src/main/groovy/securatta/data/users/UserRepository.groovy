package securatta.data.users

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Row
import groovy.util.logging.Slf4j
import ratpack.exec.Promise
import javax.inject.Inject
import securatta.Config
import securatta.data.Cassandra
import securatta.util.Crypto
import com.datastax.driver.core.ResultSet

/**
 * Repository using Cassandra database to store user related data
 *
 * @since 0.1.0
 */
@Slf4j
class UserRepository {

  static final String SECURATTA = 'securatta'
  static final String NAME = 'name'
  static final String USERNAME = 'username'
  static final String PASSWORD = 'password'

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

    String encodedPassword = Crypto.Blowfish.encode(password, config.security.salt)

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
      .map { ResultSet rs ->
        if (!rs.isEmpty()) {
          return toUser(rs.one())
        }
      }
  }

  /**
   * Searchs for a given user by username
   *
   * @param username the user's username
   * @return a {@link User} promise
   * @since 0.1.3
   */
  Promise<UserCredentials> findCredentialsByUsername(String username) {
    String stmt = "SELECT * FROM securatta.user_credentials WHERE username = ?"

    return Cassandra
      .executeAsync(cluster.connect(), stmt, username)
      .map { ResultSet rs ->
        if (!rs.isEmpty()) {
          return toUserCredentials(rs.one())
        }
    }
  }

  Promise<UserToken> checkCredentials(final UserCredentials untrusted) {
    return findCredentialsByUsername(untrusted.username)
    .map { UserCredentials trusted ->
      compareCredentials(trusted, untrusted)
    }.flatMap { UserCredentials trusted ->
      findByUsername(trusted.username)
    }.flatMap { User user ->
      createUserToken(user)
    }
  }

  static UserCredentials compareCredentials(UserCredentials trusted, UserCredentials untrusted) {
    Boolean matches = Crypto.Blowfish.matches(untrusted.password, trusted.password)

    if (matches) {
      return trusted
    }

    throw new IllegalStateException('Bad credentials')
  }

  static User toUser(Row row) {
    return new User(
      name: row.getString(NAME), username: row.getString(USERNAME),
    )
  }

  static UserCredentials toUserCredentials(Row row) {
    return new UserCredentials(
      username: row.getString(USERNAME), password: row.getString(PASSWORD),
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
      createToken(pUser).map { String token ->
        new UserToken(
          user: new User(name: pUser.name, username: pUser.username),
          token: token,
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
  Promise<String> createToken(User user) {
    assert user, "user required"

    return Promise
      .value(sub: SECURATTA, name: user.name, username: user.username)
      .map { Map<String,Object> claims ->
        Crypto.JWT.generateToken(claims, config.security.secret)
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
        Crypto.JWT.verifyTokenAndGetClaims(token, secret)
      }.map { Map claims ->
        new User(username: "${claims.username}", name: "${claims.name}")
      }.map { User user ->
        new UserToken(user: user, token: token)
      }
  }
}
