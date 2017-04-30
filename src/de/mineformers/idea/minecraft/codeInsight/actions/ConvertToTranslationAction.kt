package de.mineformers.idea.minecraft.codeInsight.actions

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.InputValidatorEx
import com.intellij.openapi.ui.Messages
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiLiteral
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import de.mineformers.idea.minecraft.icons.MCIcons
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nElementFactory

/**
 * ConvertToTranslationAction

 * @author PaleoCrafter
 */
class ConvertToTranslationAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val file = e.getData(LangDataKeys.PSI_FILE)
        val editor = e.getData(PlatformDataKeys.EDITOR)
        val element = file?.findElementAt(editor!!.caretModel.offset)!!.parent
        val value = (element as PsiLiteral).value as String?
        val result = Messages.showInputDialogWithCheckBox("Enter translation key:", "Convert String Literal to Translation", "Replace literal with call to StatCollector", true, true, Messages.getQuestionIcon(), null, object : InputValidatorEx {
            override fun getErrorText(inputString: String): String? {
                if (inputString.contains("="))
                    return "Key must not contain separator character ('=')"
                if (inputString.isEmpty())
                    return "Key must not be empty"
                return null
            }

            override fun checkInput(inputString: String): Boolean = !inputString.contains("=") && !inputString.isEmpty()

            override fun canClose(inputString: String): Boolean = !inputString.contains("=") && !inputString.isEmpty()
        })
        if (result.getFirst() != null) {
            I18nElementFactory.addTranslation(ProjectRootManager.getInstance(e.project!!).fileIndex.getModuleForFile(FileDocumentManager.getInstance().getFile(editor!!.document)!!), result.getFirst(), value)
            if (result.getSecond())
                object : WriteCommandAction.Simple<Unit>(e.project, PsiDocumentManager.getInstance(e.project!!).getPsiFile(editor.document)) {
                    @Throws(Throwable::class)
                    override fun run() {
                        val expression = JavaPsiFacade.getElementFactory(e.project!!).createExpressionFromText("net.minecraft.util.StatCollector.translateToLocal(\"" + result.getFirst() + "\")", element.getContext())
                        if (PsiDocumentManager.getInstance(project).getPsiFile(editor.document)!!.language === JavaLanguage.INSTANCE)
                            JavaCodeStyleManager.getInstance(project).shortenClassReferences(element.getParent().replace(expression))
                        else
                            element.getParent().replace(expression)
                    }
                }.execute()
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.icon = MCIcons.LANGUAGE_FILE
        val file = e.getData(LangDataKeys.PSI_FILE)
        val editor = e.getData(PlatformDataKeys.EDITOR)
        if (file == null || editor == null) {
            e.presentation.isEnabled = false
            return
        }
        val element = file.findElementAt(editor.caretModel.offset)
        e.presentation.isEnabled = element != null && element.parent is PsiLiteral && (element.parent as PsiLiteral).value is String
    }
}
