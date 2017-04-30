package de.mineformers.idea.minecraft.codeInsight.inspections

import com.intellij.codeInspection.InspectionToolProvider

/**
 * MinecraftInspectionsProvider

 * @author PaleoCrafter
 */
class MinecraftInspectionsProvider : InspectionToolProvider {
    override fun getInspectionClasses() =
        arrayOf(NoTranslationInspection::class.java, MissingFormatInspection::class.java)
}
