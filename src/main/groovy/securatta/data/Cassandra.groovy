package securatta.data

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Row
import com.datastax.driver.core.Session
import com.google.common.util.concurrent.ListenableFuture
import ratpack.exec.Downstream
import ratpack.exec.Promise
import ratpack.exec.Upstream

import java.util.function.Predicate

/**
 * Cassandra related functions
 *
 * @since 0.1.0
 */
class Cassandra {

  /**
   * Executes a Cassandra's query asynchronously
   *
   * @param session Cassandra's session
   * @param stmt query statement
   * @param args query arguments
   * @return a {@link ResultSet} promise
   * @since 0.1.0
   */
  static Promise<ResultSet> executeAsync(Session session, String stmt, Object...args) {
    return Promise.async(accept(session.executeAsync(stmt, args)))
  }

  /**
   * Returns a single {@link Row} from the instance of {@link
   * ResultSet} passed as argument
   *
   * @param rs instance of {@link ResultSet}
   * @return the first row out of the {@link ResultSet} passed as parameter
   * @since 0.1.0
   */
  static Row singleRow(ResultSet rs) {
    return rs.one()
  }

  private static <T> Upstream<T> accept(final ListenableFuture<T> listenable) {
    return { Downstream<T> down ->
      down.accept(listenable)
    } as Upstream<T>
  }
}
