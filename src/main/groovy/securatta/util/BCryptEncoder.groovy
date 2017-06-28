package securatta.util

import org.mindrot.jbcrypt.BCrypt

/**
 * A BCrypt password encoder
 *
 * @since 0.1.3
 */
class BCryptEncoder {

  static String encode(final String plain, final String salt) {
    return BCrypt.hashpw(plain, salt)
  }

  static boolean matches(final String plain, final String encoded) {
    return BCrypt.checkpw(plain, encoded)
  }
}
