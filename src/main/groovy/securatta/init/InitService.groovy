package securatta.init

import javax.inject.Inject
import ratpack.service.Service
import ratpack.service.StartEvent

import securatta.data.DataMigrationService

class InitService implements Service {

    @Inject
    DataMigrationService migrationService

    @Override
    void onStart(final StartEvent event) {
        migrationService.migrate()
    }
}
