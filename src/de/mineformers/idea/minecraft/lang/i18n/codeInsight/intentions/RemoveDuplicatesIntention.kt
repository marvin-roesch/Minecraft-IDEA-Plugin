package de.mineformers.idea.minecraft.lang.i18n.codeInsight.intentions

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.util.IncorrectOperationException
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nTypes

/**
 * RemoveDuplicatesIntention

 * @author PaleoCrafter
 */
class RemoveDuplicatesIntention(private val keep: I18nProperty) : BaseIntentionAction() {
    override fun getText() = "Remove duplicates (keep this translation)"

    override fun getFamilyName() = "Minecraft localization"

    override fun isAvailable(project: Project, editor: Editor, psiFile: PsiFile) = true

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {
        for (elem in psiFile.children) {
            if (elem is I18nProperty && elem !== keep && keep.key == elem.key) {
                if (elem.getNextSibling() != null && elem.getNextSibling().node.elementType === I18nTypes.CRLF)
                    elem.getNextSibling().delete()
                elem.delete()
            }
        }
    }
}
