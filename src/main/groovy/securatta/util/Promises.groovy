package securatta.util

import ratpack.func.Action

/**
 * Created by dev on 5/28/17.
 */
class Promises {

    static Action<Throwable> emptyOptional() {
        return { Throwable th ->
            Optional.empty()
        } as Action<Throwable>
    }
}
