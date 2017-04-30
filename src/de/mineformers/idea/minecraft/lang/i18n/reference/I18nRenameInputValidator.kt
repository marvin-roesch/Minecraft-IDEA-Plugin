package de.mineformers.idea.minecraft.lang.i18n.reference

import com.intellij.openapi.project.Project
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.refactoring.rename.RenameInputValidatorEx
import com.intellij.util.ProcessingContext
import de.mineformers.idea.minecraft.lang.i18n.I18nUtil
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty

/**
 * I18nRenameInputValidator

 * @author PaleoCrafter
 */
class I18nRenameInputValidator : RenameInputValidatorEx {
    override fun getErrorMessage(newName: String, project: Project): String? {
        if (newName.contains("="))
            return "Key must not contain separator character ('=')"
        if (newName.isEmpty())
            return "Key must not be empty"
        if (!I18nUtil.findDefaultProperties(project, key = newName).isEmpty())
            return "The given name already exists"
        return null
    }

    override fun getPattern(): ElementPattern<out PsiElement> {
        return PlatformPatterns.psiElement(I18nProperty::class.java)
    }

    override fun isInputValid(newName: String, element: PsiElement, context: ProcessingContext): Boolean {
        return !newName.contains("=") && !newName.isEmpty() && I18nUtil.findDefaultProperties(element.project,
                                                                                              key = newName).isEmpty()
    }
}
