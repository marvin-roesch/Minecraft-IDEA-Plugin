package de.mineformers.idea.minecraft.lang.i18n.psi

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import de.mineformers.idea.minecraft.lang.i18n.file.I18nFile
import de.mineformers.idea.minecraft.lang.i18n.file.I18nFileType
import javax.swing.JList

object I18nElementFactory {
    fun addTranslation(module: Module?, name: String, value: String?) {
        if (module == null || value == null)
            return
        fun write(files: Iterable<VirtualFile>) {
            for (file in files) {
                val simpleFile = PsiManager.getInstance(module.project).findFile(file) as I18nFile?
                if (simpleFile != null) {
                    object : WriteCommandAction.Simple<Unit>(module.project, simpleFile) {
                        @Throws(Throwable::class)
                        override fun run() {
                            simpleFile.add(createCRLF(module.project))
                            simpleFile.add(createProperty(module.project, name, value))
                        }
                    }.execute()

                    FileDocumentManager.getInstance().saveDocument(FileDocumentManager.getInstance().getDocument(file)!!)
                }
            }
        }

        val files = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, I18nFileType.INSTANCE,
                                                                    GlobalSearchScope.moduleScope(module))
        val fileNames = files.map { it.nameWithoutExtension }.toSet()
        if (fileNames.size != files.size) {
            val pattern = Regex("^.*?/assets/(.*?)/lang.*?\$")
            val choices =
                files
                    .map { pattern.matchEntire(it.path)?.groupValues?.get(1) }
                    .filter { it != null }
                    .sortedBy { it }
            val swingList = JList(choices.toTypedArray())
            JBPopupFactory.getInstance()
                .createListPopupBuilder(swingList)
                .setTitle("Choose resource domain")
                .setAdText("There are multiple resource domains with localization files, choose one for this translation.")
                .setItemChoosenCallback {
                    if (swingList.selectedValue != null) {
                        val validPattern = Regex("^.*?/assets/${Regex.escape(swingList.selectedValue!!)}/lang.*?\$")
                        write(files.filter { validPattern.matches(it.path) })
                    }
                }
                .createPopup()
                .showInBestPositionFor(FileEditorManager.getInstance(module.project).selectedTextEditor!!)
        } else
            write(files)
    }

    fun createProperty(project: Project, name: String, value: String): I18nProperty {
        val file = createFile(project, name + "=" + value)
        return file.firstChild as I18nProperty
    }

    fun createProperty(project: Project, name: String): I18nProperty {
        val file = createFile(project, name + "=x")
        return file.firstChild as I18nProperty
    }

    fun createCRLF(project: Project): PsiElement {
        val file = createFile(project, "\n")
        return file.firstChild
    }

    fun createFile(project: Project, text: String): I18nFile {
        val name = "dummy.simple"
        return PsiFileFactory.getInstance(project).createFileFromText(name, I18nFileType.INSTANCE, text) as I18nFile
    }
}
