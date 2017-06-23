import seguratta.data.DataModule
import seguratta.auth.AuthModule

import static ratpack.groovy.Groovy.ratpack

ratpack {
    bindings {
        module DataModule
        module AuthModule
    }
}
