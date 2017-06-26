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

  @Override
  String encode(final String password, final String salt = BCrypt.gensalt()) {
    assert password, REQUIRED_PASS

    return BCrypt.hashpw(password, salt)
  }

  @Override
  boolean matches(final String plainPassword, final String encodedPassword) {
    return BCrypt.checkpw(plainPassword, encodedPassword)
  }
}
