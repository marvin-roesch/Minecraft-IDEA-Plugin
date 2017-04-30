package de.mineformers.idea.minecraft.lang.i18n.style

import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * I18nSyntaxHighlighterFactory

 * @author PaleoCrafter
 */
class I18nSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        return I18nSyntaxHighlighter()
    }
}
