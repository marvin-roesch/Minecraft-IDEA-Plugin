package de.mineformers.idea.minecraft.frameworkSupport

import com.intellij.framework.FrameworkTypeEx
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModifiableModelsProvider
import com.intellij.openapi.roots.ModifiableRootModel
import de.mineformers.idea.minecraft.icons.MCIcons

/**
 * ${JDOC}
 */
class ForgeFrameworkSupportProvider : MinecraftFrameworkSupportProvider() {
    companion object {
        final val ID = "forge"
    }

    override fun addSupport(module: Module, rootModel: ModifiableRootModel, modifiableModelsProvider: ModifiableModelsProvider) {

    }

    override fun getFrameworkType(): FrameworkTypeEx {
        return object : FrameworkTypeEx(ID) {
            override fun createProvider() = this@ForgeFrameworkSupportProvider

            override fun getPresentableName() = "Forge"

            override fun getIcon() = MCIcons.MINECRAFT
        }
    }
}