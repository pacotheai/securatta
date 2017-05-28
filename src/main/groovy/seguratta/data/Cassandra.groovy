package seguratta.data

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Row
import com.datastax.driver.core.Session
import com.google.common.util.concurrent.ListenableFuture
import ratpack.exec.Downstream
import ratpack.exec.Promise
import ratpack.exec.Upstream

import java.util.function.Predicate

/**
 * @since 0.1.0
 */
class Cassandra {

    static Promise<ResultSet> executeAsync(Session session, String stmt, Object...args) {
        return Promise.async(accept(session.executeAsync(stmt, args)))
    }

    private static <T> Upstream<T> accept(final ListenableFuture<T> listenable) {
        return { Downstream<T> down -> down.accept(listenable) } as Upstream<T>
    }
}
