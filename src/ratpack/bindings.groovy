import seguratta.auth.AuthModule

import static ratpack.groovy.Groovy.ratpack

ratpack {
    bindings {
        module AuthModule
    }
}