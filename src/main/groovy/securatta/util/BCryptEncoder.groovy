package securatta.util

import org.pac4j.core.credentials.password.PasswordEncoder
import org.mindrot.jbcrypt.BCrypt
import groovy.transform.Immutable

/**
 * A password encoder for bcrypt and using a salt.
 *
 * @since 0.1.3
 */
@Immutable
class BCryptEncoder implements PasswordEncoder {

  static final String REQUIRED_SALT = 'Salt required!'
  static final String REQUIRED_PASS = 'Password required!'

  /**
   * the salt to hash with (perhaps generated using {@link BCrypt#gensalt()})
   *
   * @since 0.1.3
   */
  String salt

  @Override
  String encode(final String password) {
    assert salt,     REQUIRED_SALT
    assert password, REQUIRED_PASS

    return BCrypt.hashpw(password, salt)
  }

  @Override
  boolean matches(final String plainPassword, final String encodedPassword) {
    assert salt, REQUIRED_SALT

    return BCrypt.checkpw(plainPassword, encodedPassword)
  }
}
