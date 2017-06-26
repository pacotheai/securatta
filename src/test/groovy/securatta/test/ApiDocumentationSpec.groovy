package securatta.test

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris

import com.jayway.restassured.response.Header
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.JUnitRestDocumentation
import com.jayway.restassured.RestAssured
import com.jayway.restassured.response.Response
import org.junit.Rule
import spock.lang.Specification
import com.jayway.restassured.specification.RequestSpecification
import com.jayway.restassured.builder.RequestSpecBuilder
import spock.lang.AutoCleanup
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.test.remote.RemoteControl
import ratpack.guice.Guice
import ratpack.impose.ImpositionsSpec
import ratpack.impose.UserRegistryImposition

/**
 * Base for Restdocs specs
 *
 * @since 0.1.3
 */
class ApiDocumentationSpec extends Specification {

  @Rule
  JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation('build/generated-snippets')

  @AutoCleanup
  GroovyRatpackMainApplicationUnderTest app = new GroovyRatpackMainApplicationUnderTest() {
    @Override
    void addImpositions(ImpositionsSpec impositions) {
      impositions.add(UserRegistryImposition.of(
        Guice.registry {
          it.bindInstance ratpack.remote.RemoteControl.handlerDecorator()
        }
      ))
    }
  }

  RequestSpecification baseRequestSpec
  RemoteControl remote =
    new RemoteControl(app, io.remotecontrol.client.UnserializableResultStrategy.STRING)

  def setup() {
    this.baseRequestSpec = new RequestSpecBuilder()
    .addFilter(documentationConfiguration(restDocumentation))
    .build()
    .contentType('application/json')
    .accept('application/json')
  }

  /**
   * Returns an authorization token that can be used in every call
   * requiring authorization.
   *
   * @return an authorization header with the token
   * needed for every subsequent call.
   * @since 0.1.3
   */
  Header getNewToken(String username, String password) {
    Response authentication = RestAssured
    .given(baseRequestSpec)
    .body("{\"username\":\"$username\", \"password\":\"$password\"}")
    .contentType('application/json')
    .accept('application/json')
    .when()
    .post("${app.address}api/v1/auth/token")

    String token = authentication
    .jsonPath()
    .get('token')

    return new Header("Authorization", "Bearer $token")
  }

  /**
   * Returns all default request preprocessors. For example, Ratpack's
   * functional tests startup application in different random ports,
   * we would like the documentation to reflect always port 5050.
   *
   * @return all default preprocessors
   * @since 0.1.3
   */
  OperationRequestPreprocessor getRequestPreprocessors() {
    return preprocessRequest(prettyPrint(),
                             modifyUris().port(5050))
  }

  /**
   * Returns all default response preprocessors, such as
   * pretty-printing response json output
   *
   * @return all default preprocessors for documentation response
   * @since 0.1.3
   */
  OperationResponsePreprocessor getResponsePreprocessors() {
    return preprocessResponse(prettyPrint())
  }
}
