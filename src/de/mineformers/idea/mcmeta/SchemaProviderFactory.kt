package de.mineformers.idea.mcmeta

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory
import com.jetbrains.jsonSchema.extension.SchemaType

/**
 * ${JDOC}
 */
class SchemaProviderFactory : JsonSchemaProviderFactory {
    companion object {
        val SCHEMA_FILE = JsonSchemaProviderFactory.getResourceFile(SchemaProviderFactory::class.java,
                                                                    "/sounds.schema.json")
    }

    override fun getProviders(project: Project?): List<JsonSchemaFileProvider> = listOf(SoundsSchemaProvider())

    private class SoundsSchemaProvider : JsonSchemaFileProvider {
        override fun getName() = "Minecraft Sounds JSON"

        override fun isAvailable(project: Project, file: VirtualFile) = file.name == "sounds.json"

        override fun getSchemaType(): SchemaType = SchemaType.embeddedSchema

        override fun getSchemaFile(): VirtualFile = SCHEMA_FILE
    }
}
