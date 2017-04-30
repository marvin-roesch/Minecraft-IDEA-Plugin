package de.mineformers.idea.minecraft.codeInsight.inspections

import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.ide.util.gotoByName.ChooseByNamePopup
import com.intellij.ide.util.gotoByName.ChooseByNamePopupComponent
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.util.IncorrectOperationException
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.identifiers.IdentifierUtil
import de.mineformers.idea.minecraft.lang.i18n.codeInsight.reference.identifiers.LiteralTranslationIdentifier
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nElementFactory
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty
import de.mineformers.idea.minecraft.lang.i18n.reference.GotoI18nModel
import org.jetbrains.annotations.Nls

/**
 * NoTranslationInspection

 * @author PaleoCrafter
 */
class NoTranslationInspection : PsiElementInspection() {
    private val createQuickFix = CreateTranslationQuickFix()
    private val changeQuickFix = ChangeTranslationQuickFix()

    @Nls
    override fun getDisplayName(): String {
        return "Detect missing translation"
    }

    override fun getDefaultLevel(): HighlightDisplayLevel {
        return HighlightDisplayLevel.ERROR
    }

    @Nls
    override fun getGroupDisplayName(): String {
        return "Minecraft"
    }

    override fun checkElement(element: PsiElement?, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        if (element is PsiLiteralExpression) {
            val result = LiteralTranslationIdentifier().identify(element)
            if (result != null && !result.containsVariable && result.text == null)
                return arrayOf(manager.createProblemDescriptor(element,
                                                               "The given translation key does not exist",
                                                               arrayOf(createQuickFix, changeQuickFix),
                                                               ProblemHighlightType.GENERIC_ERROR,
                                                               isOnTheFly,
                                                               false))
        }
        return null
    }

    override fun isEnabledByDefault(): Boolean {
        return true
    }

    private class CreateTranslationQuickFix : LocalQuickFix {
        override fun getName(): String {
            return "Create translation"
        }

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            try {
                val literal = descriptor.psiElement as PsiLiteralExpression
                val translation = LiteralTranslationIdentifier().identify(literal)
                val literalValue = literal.value as String
                val key = translation?.varKey?.replace(IdentifierUtil.VALUE_VARIABLE, literalValue) ?: literalValue
                val result = Messages.showInputDialog("Enter default value for \"$key\":",
                                                      "Create Translation",
                                                      Messages.getQuestionIcon())
                if (result != null) {
                    I18nElementFactory.addTranslation(
                        ProjectRootManager
                            .getInstance(project).fileIndex
                            .getModuleForFile(literal.containingFile.virtualFile),
                        key,
                        result)
                }
            } catch (ignored: IncorrectOperationException) {
            }

        }

        override fun getFamilyName(): String {
            return name
        }
    }

    private class ChangeTranslationQuickFix : LocalQuickFix {
        override fun getName(): String {
            return "Use existing translation"
        }

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            try {
                val literal = descriptor.psiElement as PsiLiteralExpression
                val translation = LiteralTranslationIdentifier().identify(literal)
                val popup = ChooseByNamePopup.createPopup(project,
                                                          GotoI18nModel(project, translation?.regexPattern),
                                                          null)
                popup.invoke(object : ChooseByNamePopupComponent.Callback() {
                    override fun elementChosen(element: Any) {
                        val selectedProperty = element as I18nProperty
                        object : WriteCommandAction.Simple<Unit>(project, literal.containingFile) {
                            @Throws(Throwable::class)
                            override fun run() {
                                val match = translation?.regexPattern?.matchEntire(selectedProperty.key)
                                val insertion =
                                    if (match == null || match.groups.size <= 1)
                                        selectedProperty.key
                                    else
                                        match.groupValues[1]
                                literal.replace(JavaPsiFacade.getInstance(project).elementFactory.createExpressionFromText(
                                    "\"$insertion\"",
                                    literal.context))
                            }
                        }.execute()
                    }
                }, ModalityState.current(), false)
            } catch (ignored: IncorrectOperationException) {
            }

        }

        override fun getFamilyName(): String {
            return name
        }
    }
}
