package securatta.data

import com.datastax.driver.core.Cluster
import securatta.Config

import javax.inject.Inject
import javax.inject.Provider

/**
 * Creates an instance of {@link Cluster} to connect to a Cassandra
 * instance
 *
 * @since 0.1.0
 */
class ClusterProvider implements Provider<Cluster> {

    @Inject
    Config config

    @Override
    Cluster get() {
        return Cluster
        .builder()
        .addContactPoint(config.data.contactPoint)
        .build()
    }
}
