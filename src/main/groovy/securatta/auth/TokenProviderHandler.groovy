package securatta.auth

import ratpack.handling.Context
import ratpack.handling.Handler
import securatta.domain.UserCredentials
import securatta.util.Handlers

import javax.inject.Inject

/**
 * This service authenticates a given user's credentials
 * and if the credentials are valid then a user token
 * will be provided
 *
 * If the credentials are not valid the handler will
 * return a 400 meaning credentials are not valid
 *
 * @since 0.1.0
 */
class TokenProviderHandler implements Handler {

    @Inject
    Service service

    @Override
    void handle(Context ctx) throws Exception {
        ctx
        .parse(UserCredentials)
        .flatMap(service.&authenticateUser)
        .then(Handlers.ifNotPresent(ctx, 400))
    }
}
