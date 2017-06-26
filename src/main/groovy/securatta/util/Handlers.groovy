package securatta.util

import ratpack.exec.Promise
import ratpack.func.Block
import ratpack.handling.Context

/**
 * Utilities for handlers
 *
 * @since 0.1.0
 */
class Handlers {

  /**
   * Uses the {@link Context} to set the respose status
   * with the status passed as argument
   *
   * @param context the current Ratpack {@link Context}
   * @param status the response status
   * @return an {@link Block} instance that can be used
   * in a routing expression
   * @since 0.1.3
   */
  static Block showStatus(Context context, Integer status) {
    return {
      context.response.status(status)
      context.render("")
    } as Block
  }

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
