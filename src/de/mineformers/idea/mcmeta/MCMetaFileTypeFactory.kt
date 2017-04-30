package de.mineformers.idea.mcmeta

import com.intellij.openapi.fileTypes.ExactFileNameMatcher
import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher
import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

/**
 * @author PaleoCrafter
 */
class MCMetaFileTypeFactory : FileTypeFactory() {
    override fun createFileTypes(consumer: FileTypeConsumer) {
        consumer.consume(MCMetaFileType.INSTANCE,
                         ExtensionFileNameMatcher("mcmeta"),
                         ExactFileNameMatcher("mcmod.info"))
    }
}
