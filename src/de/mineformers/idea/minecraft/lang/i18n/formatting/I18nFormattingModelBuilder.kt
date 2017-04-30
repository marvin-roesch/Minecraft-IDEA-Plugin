package de.mineformers.idea.minecraft.lang.i18n.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import de.mineformers.idea.minecraft.lang.i18n.I18nLanguage
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nTypes

/**
 * I18nFormattingModelBuilder

 * @author PaleoCrafter
 */
class I18nFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile,
                                                                       I18nBlock(element.node,
                                                                                 Wrap.createWrap(WrapType.NONE, false),
                                                                                 Alignment.createAlignment(),
                                                                                 createSpaceBuilder(settings)),
                                                                       settings)
    }

    private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder {
        return SpacingBuilder(settings,
                              I18nLanguage.INSTANCE).around(I18nTypes.SEPARATOR).none().before(I18nTypes.PROPERTY).none()
    }

    override fun getRangeAffectingIndent(file: PsiFile, offset: Int, elementAtOffset: ASTNode): TextRange? {
        return null
    }
}
