package de.mineformers.idea.minecraft.lang.i18n.file

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import de.mineformers.idea.minecraft.lang.i18n.I18nLanguage

/**
 * I18nFile

 * @author PaleoCrafter
 */
class I18nFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, I18nLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return I18nFileType.INSTANCE
    }
}
