package de.mineformers.idea.minecraft.lang.i18n.file

import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher
import com.intellij.openapi.fileTypes.FileNameMatcher
import com.intellij.openapi.fileTypes.LanguageFileType
import de.mineformers.idea.minecraft.icons.MCIcons
import de.mineformers.idea.minecraft.lang.i18n.I18nLanguage
import org.jetbrains.annotations.NonNls

import javax.swing.*

/**
 * I18nFileType

 * @author PaleoCrafter
 */
class I18nFileType private constructor() : LanguageFileType(I18nLanguage.INSTANCE) {
    override fun getName() = "Minecraft language file"

    override fun getDescription() = "Minecraft language file"

    override fun getDefaultExtension() = CODE_EXTENSION

    override fun getIcon() = MCIcons.LANGUAGE_FILE

    companion object {
        val INSTANCE = I18nFileType()
        @NonNls
        val CODE_EXTENSION = "lang"

        fun fileNameMatchers(): Array<FileNameMatcher> {
            return arrayOf(ExtensionFileNameMatcher(CODE_EXTENSION))
        }
    }
}
