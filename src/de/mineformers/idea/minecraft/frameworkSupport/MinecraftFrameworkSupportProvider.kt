package de.mineformers.idea.minecraft.frameworkSupport

import com.intellij.framework.addSupport.FrameworkSupportInModuleConfigurable
import com.intellij.framework.addSupport.FrameworkSupportInModuleProvider
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ModifiableModelsProvider
import com.intellij.openapi.roots.ModifiableRootModel

import javax.swing.*

/**
 * ${JDOC}
 */
abstract class MinecraftFrameworkSupportProvider : FrameworkSupportInModuleProvider() {
    companion object {
        val EP_NAME = ExtensionPointName.create<MinecraftFrameworkSupportProvider>("de.mineformers.idea.minecraft.frameworkSupport")
    }

    abstract fun addSupport(module: Module, rootModel: ModifiableRootModel,
                            modifiableModelsProvider: ModifiableModelsProvider)

    override fun createConfigurable(model: FrameworkSupportModel): FrameworkSupportInModuleConfigurable {
        return object : FrameworkSupportInModuleConfigurable() {
            override fun createComponent(): JComponent? {
                return null
            }

            override fun addSupport(module: Module,
                                    rootModel: ModifiableRootModel,
                                    modifiableModelsProvider: ModifiableModelsProvider) {
                this@MinecraftFrameworkSupportProvider.addSupport(module, rootModel, modifiableModelsProvider)
            }
        }
    }

    override fun isEnabledForModuleType(moduleType: ModuleType<ModuleBuilder>): Boolean {
        return false
    }
}
