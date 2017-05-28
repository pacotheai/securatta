package seguratta.util

import ratpack.func.Action

/**
 * @since 0.1.0
 */
class Promises {

    static Action<Throwable> emptyOptional() {
        return (Throwable th) -> { Optional.empty() } as Action<Throwable>
    }
}
