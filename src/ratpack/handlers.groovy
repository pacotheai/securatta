import ratpack.server.ServerConfigBuilder
import securatta.Config
import securatta.auth.TokenAuthenticationHandler
import securatta.auth.TokenProviderHandler

import static ratpack.groovy.Groovy.ratpack

ratpack {
    serverConfig { ServerConfigBuilder config ->
        config
        .port(8080)
        .yaml("securatta.yml")
        .require("", Config)
    }

    handlers {
        prefix('api/v1') {
            prefix('auth') {
                post('check', TokenAuthenticationHandler)
                post('token', TokenProviderHandler)
            }
        }
    }
}
