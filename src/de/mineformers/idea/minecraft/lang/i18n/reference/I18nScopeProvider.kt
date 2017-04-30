package de.mineformers.idea.minecraft.lang.i18n.reference

import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.openapi.project.Project
import com.intellij.psi.impl.search.CustomPropertyScopeProvider
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import de.mineformers.idea.minecraft.lang.i18n.file.I18nFileType

/**
 * I18nScopeAccessProvider

 * @author PaleoCrafter
 */
class I18nScopeProvider : CustomPropertyScopeProvider {
    override fun getScope(project: Project): SearchScope {
        return GlobalSearchScope.getScopeRestrictedByFileTypes(GlobalSearchScope.allScope(project),
                                                               I18nFileType.INSTANCE)
    }
}
