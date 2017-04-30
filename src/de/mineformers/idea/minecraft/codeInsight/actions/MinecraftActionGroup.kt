package de.mineformers.idea.minecraft.codeInsight.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import de.mineformers.idea.minecraft.icons.MCIcons

/**
 * MinecraftActionGroup

 * @author PaleoCrafter
 */
class MinecraftActionGroup : DefaultActionGroup() {
    override fun update(e: AnActionEvent?) {
        super.update(e)
        e!!.presentation.icon = MCIcons.MINECRAFT
    }
}
