package de.mineformers.idea.minecraft.project.wizard

import com.intellij.openapi.externalSystem.service.project.wizard.AbstractExternalModuleBuilder
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.StdModuleTypes
import com.intellij.openapi.roots.ModifiableRootModel
import de.mineformers.idea.minecraft.project.ForgeProjectSettings
import de.mineformers.idea.minecraft.util.MinecraftConstants

/**
 * ${JDOC}
 */
class ForgeModuleBuilder : AbstractExternalModuleBuilder<ForgeProjectSettings>(MinecraftConstants.SYSTEM_ID,
                                                                               ForgeProjectSettings()) {
    override fun setupRootModel(modifiableRootModel: ModifiableRootModel?) {
    }

    override fun getModuleType(): ModuleType<*>? = StdModuleTypes.JAVA
}