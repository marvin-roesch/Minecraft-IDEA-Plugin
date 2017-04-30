package de.mineformers.idea.mcmeta

import com.intellij.json.JsonLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import de.mineformers.idea.minecraft.icons.MCIcons
import javax.swing.Icon

/**
 * @author PaleoCrafter
 */
class MCMetaFileType : LanguageFileType(JsonLanguage.INSTANCE) {

    override fun getName(): String {
        return "MCMeta"
    }

    override fun getDescription(): String {
        return "Minecraft metadata files"
    }

    override fun getDefaultExtension(): String {
        return DEFAULT_EXTENSION
    }

    override fun getIcon(): Icon? {
        return MCIcons.MCMETA_FILE
    }

    companion object {
        val INSTANCE = MCMetaFileType()
        val DEFAULT_EXTENSION = "mcmeta"
    }
}
