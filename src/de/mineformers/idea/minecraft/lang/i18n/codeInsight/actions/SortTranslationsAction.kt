package de.mineformers.idea.minecraft.lang.i18n.codeInsight.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.Messages
import de.mineformers.idea.minecraft.icons.MCIcons
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.intentions.SortTranslationsIntention
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.intentions.SortTranslationsIntention.Ordering
import de.mineformers.idea.minecraft.lang.i18n.file.I18nFileType

/**
 * ConvertToTranslationAction

 * @author PaleoCrafter
 */
class SortTranslationsAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(LangDataKeys.PSI_FILE)!!
        val editor = e.getData(PlatformDataKeys.EDITOR)!!
        val values = arrayOf("Ascending", "Descending")
        val i = Messages.showChooseDialog("Sort order:", "Select Sort Order", values, "Ascending", null)
        val order = Ordering.values()[i]
        SortTranslationsIntention(order).invoke(editor.project!!, editor, file)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.icon = MCIcons.LANGUAGE_FILE
        val file = e.getData(LangDataKeys.PSI_FILE)
        val editor = e.getData(PlatformDataKeys.EDITOR)
        if (file == null || editor == null) {
            e.presentation.isEnabled = false
            return
        }
        e.presentation.isEnabled = file.fileType === I18nFileType.INSTANCE
    }
}
