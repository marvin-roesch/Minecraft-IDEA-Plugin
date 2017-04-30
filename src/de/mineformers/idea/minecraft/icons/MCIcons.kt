package de.mineformers.idea.minecraft.icons

import com.intellij.openapi.util.IconLoader

/**
 * MCIcons

 * @author PaleoCrafter
 */
object MCIcons {
    private fun load(file: String) = IconLoader.getIcon("/minecraft/icons/$file.png")

    val LANGUAGE_FILE = load("i18n")
    val MCMETA_FILE = load("mcmeta")
    val MINECRAFT = load("minecraft")
}
