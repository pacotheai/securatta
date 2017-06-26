package securatta.auth

import static ratpack.jackson.Jackson.json

import ratpack.handling.Context
import ratpack.handling.Handler
import securatta.util.Handlers
import javax.inject.Inject

/**
 * Authenticates a user by the token passed as a header.
 * If an invalid token is found a 401 Unauthorized status code
 * will be provided.
 *
 * @since 0.1.0
 */
class TokenAuthenticationHandler implements Handler {

  @Inject
  AuthService service

  @Override
  void handle(Context ctx) throws Exception {
    Handlers
      .header(ctx, 'Authorization')
      .map(Handlers.&removeBearerFromToken)
      .flatMap(service.&authenticateToken)
      .onNull(Handlers.showStatus(ctx, 401))
      .then {
        ctx.response.status(200)
        ctx.render(json(it))
      }
  }
}
