package de.mineformers.idea.minecraft.lang.i18n

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.FileBasedIndex
import de.mineformers.idea.minecraft.lang.i18n.file.I18nFile
import de.mineformers.idea.minecraft.lang.i18n.file.I18nFileType
import de.mineformers.idea.minecraft.lang.i18n.psi.I18nProperty

/**
 * I18nUtil

 * @author PaleoCrafter
 */
object I18nUtil {
    enum class Scope {
        GLOBAL, PROJECT
    }

    private fun files(project: Project, scope: Scope) =
        FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, I18nFileType.INSTANCE,
                                                        if (scope == Scope.GLOBAL)
                                                            GlobalSearchScope.allScope(project)
                                                        else
                                                            GlobalSearchScope.projectScope(project))
            .map { PsiManager.getInstance(project).findFile(it) as I18nFile? }
            .filter { it != null }

    private fun findPropertiesImpl(project: Project, scope: Scope,
                                   fileFilter: (I18nFile?) -> Boolean = { true },
                                   propertyFilter: (I18nProperty) -> Boolean = { true }) =
        files(project, scope)
            .filter(fileFilter)
            .flatMap {
                (PsiTreeUtil.getChildrenOfType(it, I18nProperty::class.java) ?: emptyArray()).asIterable()
            }
            .filter(propertyFilter)
            .toList()

    fun findProperties(project: Project, scope: Scope = Scope.GLOBAL, key: String? = null, file: VirtualFile? = null) =
        findPropertiesImpl(project,
                           scope,
                           { if (file != null) it?.virtualFile?.path == file.path else true },
                           { if ( key != null) it.key == key else true })

    fun findDefaultProperties(project: Project, scope: Scope = Scope.GLOBAL, key: String? = null, file: VirtualFile? = null) =
        findPropertiesImpl(project,
                           scope,
                           {
                               it?.virtualFile?.nameWithoutExtension == "en_US" &&
                               (if (file != null) it?.virtualFile?.path == file.path else true)
                           },
                           { if ( key != null) it.key == key else true })
}
