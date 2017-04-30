package de.mineformers.idea.minecraft.lang.i18n.codeInsight.intentions

import com.google.common.collect.Lists
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.util.IncorrectOperationException
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nElementFactory
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty

import java.util.Collections

/**
 * RemoveDuplicatesIntention

 * @author PaleoCrafter
 */
class SortTranslationsIntention(private val ordering: SortTranslationsIntention.Ordering) : BaseIntentionAction() {
    enum class Ordering {
        ASCENDING, DESCENDING
    }

    override fun getText() = "Sort translations"

    override fun getFamilyName() = "Minecraft localization"

    override fun isAvailable(project: Project, editor: Editor, psiFile: PsiFile) = true

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {
        val sorted = Lists.newArrayList<I18nProperty>()
        for (elem in psiFile.children) {
            if (elem is I18nProperty) {
                sorted.add(elem)
            }
        }
        Collections.sort(sorted) { p1, p2 ->
            if (ordering == Ordering.ASCENDING) p1.key.compareTo(p2.key) else p2.key.compareTo(p1.key)
        }
        object : WriteCommandAction.Simple<Unit>(project, psiFile) {
            @Throws(Throwable::class)
            override fun run() {
                for (elem in psiFile.children) {
                    elem.delete()
                }
                var lastStart: Char = 0.toChar()
                for (property in sorted) {
                    if (lastStart.toInt() != 0 && property.key[0] != lastStart) {
                        psiFile.add(I18nElementFactory.createCRLF(project))
                    }
                    psiFile.add(I18nElementFactory.createProperty(project, property.key, property.value))
                    psiFile.add(I18nElementFactory.createCRLF(project))
                    lastStart = property.key[0]
                }
            }
        }.execute()
    }
}
