package securatta.data

import javax.inject.Inject
import com.datastax.driver.core.Cluster
import org.cognitor.cassandra.migration.Database
import org.cognitor.cassandra.migration.MigrationTask
import org.cognitor.cassandra.migration.MigrationRepository

/**
 * Triggers the Cassandra data migration tool
 *
 * @since 0.1.2
 */
class DataMigrationService {

    /**
     * Securatta keyspace
     *
     * @since 0.1.2
     */
    static final String SECURATTA_KEYSPACE = 'securatta'

    /**
     * Query to create Securatta keyspace in Cassandra
     * @since 0.1.2
     */
    static final String SECURATTA_CREATE_KEYSPACE = """
       CREATE KEYSPACE IF NOT EXISTS $SECURATTA_KEYSPACE
          WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
    """

    /**
     * Query to drop  Securatta keyspace in Cassandra
     * @since 0.1.2
     */
    static final String SECURATTA_DROP_KEYSPACE = """
        DROP KEYSPACE $SECURATTA_KEYSPACE
    """

    /**
     * Cassandra cluster connection
     *
     * @since 0.1.2
     */
    @Inject
    Cluster cluster

    /**
     * Launches the migration process
     *
     * @since 0.1.2
     */
    void migrate() {
        // migration requires the keyspace to be present
        cluster.connect().execute(SECURATTA_CREATE_KEYSPACE)

        // migration
        Database databaseConnection = new Database(cluster, SECURATTA_KEYSPACE)
        MigrationTask migrationTask = new MigrationTask(databaseConnection, new MigrationRepository())

        migrationTask.migrate()
    }

    /**
     * Drops the keyspace. Only used in testing environment
     *
     * @since 0.1.2
     */
    void dropKeyspace() {
        cluster.connect().execute(SECURATTA_DROP_KEYSPACE)
    }
}
