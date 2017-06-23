import static ratpack.groovy.Groovy.ratpack
import static securatta.util.SystemResources.classpath

ratpack {
    include(classpath('handlers.groovy'))
    include(classpath('bindings.groovy'))
}
