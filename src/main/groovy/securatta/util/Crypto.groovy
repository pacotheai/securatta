package securatta.util

import org.mindrot.jbcrypt.BCrypt
import org.pac4j.jwt.profile.JwtGenerator
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration

/**
 * Cryptographic utilities
 *
 * @since 0.1.3
 */
@SuppressWarnings('EmptyClass')
class Crypto {

  /**
   * BCrypt related functions
   *
   * @since 0.1.3
   */
  static class Blowfish {

    /**
     * Encodes plain text to a hashed password using Blowfish hashing
     * mechanism
     *
     * @param plain the plain text you want to hash
     * @param salt the salt used as seed
     * @return the hashed version of the plain text
     * @since 0.1.3
     */
    static String encode(final String plain, final String salt) {
      return BCrypt.hashpw(plain, salt)
    }

    /**
     * Checks that the plain text and the hashed text are the
     * same
     *
     * @param plain
     * @param hashed
     * @return
     * @since 0.1.3
     */
    static boolean matches(final String plain, final String hashed) {
      return BCrypt.checkpw(plain, hashed)
    }
  }

  /**
   * JWT related functions
   *
   * @since 0.1.3
   */
  static class JWT {

    /**
     * @param claims
     * @param secret
     * @return
     * @since 0.1.3
     */
    static String generateToken(Map<String,Object> claims, String secret) {
      return new JwtGenerator(new SecretSignatureConfiguration(secret)).generate(claims)
    }

    /**
     * @param token
     * @param secret
     * @return
     * @since 0.1.3
     */
    static Map<String,Object> verifyTokenAndGetClaims(String token, String secret) {
      JwtAuthenticator authenticator = new JwtAuthenticator()
      authenticator.addSignatureConfiguration(new SecretSignatureConfiguration(secret))

      return authenticator.validateTokenAndGetClaims(token)
    }
  }
}
