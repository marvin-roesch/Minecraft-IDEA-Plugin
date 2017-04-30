package de.mineformers.idea.minecraft.codeInsight.inspections

import com.intellij.codeInspection.BaseJavaLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.*

/**
 * PsiElementInspection

 * @author PaleoCrafter
 */
abstract class PsiElementInspection : BaseJavaLocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : JavaElementVisitor() {
            override fun visitMethod(method: PsiMethod) {
                addDescriptors(checkMethod(method, holder.manager, isOnTheFly))
            }

            override fun visitClass(aClass: PsiClass) {
                addDescriptors(checkClass(aClass, holder.manager, isOnTheFly))
            }

            override fun visitField(field: PsiField) {
                addDescriptors(checkField(field, holder.manager, isOnTheFly))
            }

            override fun visitElement(element: PsiElement?) {
                addDescriptors(checkElement(element, holder.manager, isOnTheFly))
            }

            override fun visitFile(file: PsiFile) {
                addDescriptors(checkFile(file, holder.manager, isOnTheFly))
            }

            private fun addDescriptors(descriptors: Array<ProblemDescriptor>?) {
                if (descriptors != null) {
                    for (descriptor in descriptors) {
                        holder.registerProblem(descriptor)
                    }
                }
            }
        }
    }

    open fun checkElement(element: PsiElement?, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        return null
    }
}
