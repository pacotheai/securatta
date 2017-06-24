package securatta.test

import com.google.inject.AbstractModule
import com.google.inject.Provides
import securatta.Config

/**
 * This module loads all test properties
 *
 * @since 0.1.2
 */
class IntegrationSpecModule extends AbstractModule {
    @Override
    void configure() {
        //
    }
    @Provides Config loadConfig() {
        return new Config(data: new Config.Data(contactPoint: 'cassandra'))
    }
}
