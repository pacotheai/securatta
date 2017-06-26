package securatta.auth

import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.snippet.Attributes.key
import static org.springframework.restdocs.snippet.Attributes.attributes

import com.jayway.restassured.RestAssured
import com.jayway.restassured.response.Response
import org.springframework.restdocs.restassured.RestDocumentationFilter
import securatta.test.ApiDocumentationSpec
import securatta.data.users.UserRepository

class TokenProviderHandlerSpec extends ApiDocumentationSpec {

  void 'get a new token sucessfully'() {
    given: 'a stored user in database'
    remote.exec {
      get(UserRepository).with {
        create('john', 'username')
        createCredentials('username', 'password')
      }
    }

    when: 'asking for a new token'
    Response response = RestAssured
      .given(baseRequestSpec)
      .filter(authenticationDocSpec)
      .body('{"username":"username", "password":"password"}')
      .contentType('application/json')
      .accept('application/json')
      .when()
      .post("${app.address}api/v1/auth/token")

    then: 'the status code should be accepted'
    response.statusCode == 201
  }

  RestDocumentationFilter getAuthenticationDocSpec() {
    return document("auth-token",
      requestPreprocessors,
      responsePreprocessors,
      requestFields(
        attributes(key("title").value("Request fields")),
        fieldWithPath("username")
          .description("user's username")
          .attributes(key('constraints').value('required')),
        fieldWithPath("password")
          .description("user's password")
          .attributes(key('constraints').value('required'))))
  }
}
