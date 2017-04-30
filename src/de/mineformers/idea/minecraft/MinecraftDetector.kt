package de.mineformers.idea.minecraft

import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import com.intellij.psi.PsiManager
import com.intellij.util.lang.UrlClassLoader
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile
import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrCodeBlock
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall
import org.jetbrains.plugins.groovy.lang.psi.impl.GroovyFileImpl

/**
 * ${JDOC}
 */
class MinecraftDetector(val project: Project) : ProjectComponent {
    override fun projectOpened() {
        val manager = StartupManager.getInstance(project)
        manager.registerStartupActivity {

        }

        manager.registerPostStartupActivity {
            for (child in project.baseDir.children) {
                if (child.name == "build.gradle") {
                    val file = PsiManager.getInstance(project).findFile(child)
                    if (file is GroovyFile) {
                        val mcCall = file.children.find { it is GrMethodCall && it.resolveMethod()?.name == "minecraft" } as GrMethodCall?
                        if (mcCall != null) {
                            val block = mcCall.children.find { it is GrCodeBlock } as GrCodeBlock?
                            println("MEGA TEST: $block")
                        }
                    }
                }
            }
        }
    }

    override fun projectClosed() {
    }

    override fun initComponent() {
    }

    override fun disposeComponent() {
    }

    override fun getComponentName(): String = "MinecraftDetector"
}