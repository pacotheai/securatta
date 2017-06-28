package securatta.util

import ratpack.exec.Promise
import ratpack.handling.Context

/**
 * Utilities for handlers
 *
 * @since 0.1.0
 */
class Handlers {

  /**
   * Extracts a given header by its name from the current
   * {@link Context}
   *
   * @param ctx the context we want the header from
   * @param name the name of the header
   * @return a {@link Promise} with the content of the header
   * @since 0.1.0
   */
  static Promise<String> header(Context ctx, String name) {
    return Promise.value(ctx.request.headers.get(name))
  }

  /**
   * Utility function to remove the 'Bearer ' literal from
   * the authorization header in order to get only the token
   * string
   *
   * @param token an authorization header content
   * @return only the token string
   * @since 0.1.0
   */
  static String removeBearerFromToken(String token) {
    return token - 'Bearer '
  }
}
