import securatta.data.DataModule
import securatta.auth.AuthModule
import securatta.init.InitModule

import static ratpack.groovy.Groovy.ratpack

ratpack {
    bindings {
        module DataModule
        module AuthModule
        module InitModule
    }
}
