package de.mineformers.idea.minecraft.lang.i18n.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.psi.PsiFile
import de.mineformers.idea.minecraft.lang.i18n.file.I18nFile

/**
 * I18nStructureViewModel

 * @author PaleoCrafter
 */
class I18nStructureViewModel(psiFile: PsiFile) : StructureViewModelBase(psiFile,
                                                                        I18nStructureViewElement(psiFile)), StructureViewModel.ElementInfoProvider {

    override fun getSorters(): Array<Sorter> {
        return arrayOf(Sorter.ALPHA_SORTER)
    }


    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean {
        return false
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        return false
    }
}
