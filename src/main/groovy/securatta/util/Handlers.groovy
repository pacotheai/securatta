package securatta.util

import static ratpack.jackson.Jackson.json

import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.handling.Context

/**
 * Utilities for handlers
 *
 * @since 0.1.0
 */
class Handlers {

    /**
     * Renders the payload in case is present otherwise will use
     * the status passed as parameter to set the response HTTP status
     *
     * @param context Ratpack's context needed to render the result
     * @param status HTTP status code in case the param is not present
     * @return an instance of type {@link Action} that can be used in a handler's `then` clause
     * @since 0.1.0
     */
    static <T> Action<T> ifNotPresent(Context context, Integer status) {
        return { T param ->
            if (param) {
                context.render(json(param))
            } else {
                context.response.status(status)
                context.render("")
            }
        } as Action<T>
    }

    static Promise<String> header(Context ctx, String name) {
        return Promise.value(ctx.request.headers.get(name))
    }

    static String removeBearerFromToken(String token) {
        return token - 'Bearer '
    }
}
