package securatta.test

import javax.inject.Inject
import spock.lang.Specification
import com.datastax.driver.core.Cluster
import securatta.data.DataMigrationService

/**
 * Base integration spec used for testing Cassandra interactions
 *
 * @since 0.1.2
 */
class CassandraIntegrationSpec extends Specification {

    /**
     * Instance of the database migration service
     *
     * @since 0.1.2
     */
    @Inject
    DataMigrationService migrationService

    /**
     * Need to get a Cassandra's connection
     *
     * @since 0.1.2
     */
    @Inject
    Cluster cluster

    /**
     * Runs the migration service before  each test to
     * make sure schema is up to date
     *
     * @since 0.1.2
     */
    def setup() {
        migrationService.migrate()
    }

    /**
     * Drops the keyspace after each test to ensure
     * consistency
     *
     * @since 0.1.2
     */
    def cleanup() {
        migrationService.dropKeyspace()
    }
}
