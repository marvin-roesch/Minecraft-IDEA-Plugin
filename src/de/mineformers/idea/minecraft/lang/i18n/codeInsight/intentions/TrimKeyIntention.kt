package de.mineformers.idea.minecraft.lang.i18n.codeInsight.intentions

import com.intellij.codeInsight.FileModificationService
import com.intellij.codeInsight.intention.BaseElementAtCaretIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.util.IncorrectOperationException
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nTypes

/**
 * TrimKeyIntention

 * @author PaleoCrafter
 */
class TrimKeyIntention : BaseElementAtCaretIntentionAction() {
    override fun getText() = "Trim translation key"

    override fun getFamilyName() = "Minecraft localization"

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean {
        if (element.node.elementType === I18nTypes.KEY) {
            if (element.text.endsWith(" "))
                return true
        }
        return false
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        if (!FileModificationService.getInstance().preparePsiElementForWrite(element.parent)) return
        val property = element.parent as I18nProperty
        property.setName(property.name!!.trim { it <= ' ' })
    }
}
