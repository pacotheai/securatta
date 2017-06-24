package securatta.data.users

import javax.inject.Inject
import spock.lang.AutoCleanup
import spock.guice.UseModules
import ratpack.test.exec.ExecHarness
import securatta.test.IntegrationSpecModule
import securatta.test.CassandraIntegrationSpec
import securatta.data.DataModule
import securatta.auth.AuthModule

/**
 * Test {@link UserRepository} interactions
 *
 * @since 0.1.2
 */
@UseModules([IntegrationSpecModule, DataModule, AuthModule])
class UserRepositoryIntegrationSpec extends CassandraIntegrationSpec {

    @AutoCleanup
    ExecHarness execHarness = ExecHarness.harness()

    @Inject
    UserRepository userRepository

    void 'create and find a user entry by username'() {
        given: 'a new user'
        assert execHarness.yield {
            userRepository.create('John', 'username')
        }.value

        when:'searching for created user'
        User user = execHarness.yieldSingle {
            userRepository.findByUsername('username')
        }.value

        then: 'we should be able to find it'
        user.username == 'username'
    }
}
