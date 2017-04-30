package de.mineformers.idea.minecraft.lang.i18n.file

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

/**
 * I18nFileTypeFactory

 * @author PaleoCrafter
 */
class I18nFileTypeFactory : FileTypeFactory() {
    override fun createFileTypes(fileTypeConsumer: FileTypeConsumer) {
        fileTypeConsumer.consume(I18nFileType.INSTANCE, "lang")
    }
}