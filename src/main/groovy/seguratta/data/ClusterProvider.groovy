package seguratta.data

import com.datastax.driver.core.Cluster
import seguratta.Config

import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by dev on 5/27/17.
 */
class ClusterProvider implements Provider<Cluster> {

    @Inject
    Config config

    @Override
    Cluster get() {
        return Cluster.builder().addContactPoint('127.0.0.1').build()
    }
}
