package de.mineformers.idea.minecraft.lang.i18n.codeInsight.intentions

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.util.IncorrectOperationException
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nTypes

/**
 * RemoveNonMatchedPropertyIntention

 * @author PaleoCrafter
 */
class RemoveNonMatchedPropertyIntention : PsiElementBaseIntentionAction() {
    override fun getText() = "Remove translation"

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        val elem = element.parent
        if (elem.nextSibling != null && elem.nextSibling.node.elementType === I18nTypes.CRLF)
            elem.nextSibling.delete()
        elem.delete()
    }

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement) = true

    override fun getFamilyName() = "Minecraft localization"
}
