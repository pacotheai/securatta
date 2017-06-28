package securatta.auth

import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.payload.PayloadDocumentation.requestHeaders
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import static org.springframework.restdocs.snippet.Attributes.key
import static org.springframework.restdocs.snippet.Attributes.attributes

import com.jayway.restassured.RestAssured
import com.jayway.restassured.response.Response
import com.jayway.restassured.response.Header
import org.springframework.restdocs.restassured.RestDocumentationFilter
import securatta.test.ApiDocumentationSpec
import securatta.data.users.UserRepository

class TokenAuthenticationHandlerSpec extends ApiDocumentationSpec {

  void 'check successful authentication'() {
    given: 'an existent user'
    remote.exec {
      get(UserRepository).with {
        create('john', 'username')
        createCredentials('username', 'password')
      }
    }

    and: 'getting a new token to check'
    Header tokenHeader = getNewToken("username", "password")

    when: "authenticating with the token"
    Response response = RestAssured
      .given(baseRequestSpec)
      .filter(authenticationSpecFor(200))
      .header(tokenHeader)
      .contentType('application/json')
      .accept('application/json')
      .when()
      .post("${app.address}api/v1/auth/check")

    then: "authentication is correct"
    response.statusCode == 200
  }

  void 'check failing authentication [NO TOKEN]'() {
    when: "authenticating with the token"
    Response response = RestAssured
      .given(baseRequestSpec)
      .filter(authenticationSpecFor(401))
      .header("Authorization", "")
      .contentType('application/json')
      .accept('application/json')
      .when()
      .post("${app.address}api/v1/auth/check")

    then: "authentication is correct"
    response.statusCode == 401
  }

  RestDocumentationFilter authenticationSpecFor(Integer code) {
    return document("auth-check-$code",
      requestPreprocessors,
      responsePreprocessors,
      requestHeaders(
        headerWithName("Authorization").description("description")))
  }
}
