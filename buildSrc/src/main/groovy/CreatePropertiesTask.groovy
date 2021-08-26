import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

class CreatePropertiesTask extends DefaultTask {

    @Input
    String path = ""

    @Input fileName = ""

    @TaskAction
    def createProperties() {
        if (path.isBlank()) {
            throw new IllegalStateException("The path property is not define.")
        }

        File file = new File(path)
        file.getParentFile().mkdirs()
        file.createNewFile()
        println "File exists: " + file.exists()

        file.withWriter { w ->
            Map<String, String> property = new HashMap<>()
            property.put "version", project.version.toString()
            ObjectMapper om = new ObjectMapper(new YAMLFactory())
            om.writeValue w, property
        }
    }
}
