package securatta.init

import com.google.inject.Scopes
import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder

import ratpack.service.Service
import securatta.data.DataMigrationService

/**
 * Guice module loading all startup services
 *
 * @since 0.1.2
 */
class InitModule extends AbstractModule {

  @Override
  void configure() {

    Multibinder<Service> updaterBindings = Multibinder.newSetBinder(binder(), Service)

    updaterBindings
      .addBinding()
      .to(DataMigrationService).in(Scopes.SINGLETON)
  }
}
