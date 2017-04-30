package de.mineformers.idea.minecraft.lang.i18n

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.ui.EditorNotifications
import de.mineformers.idea.minecraft.lang.i18n.file.I18nFileType

/**
 * I18nFileListener

 * @author PaleoCrafter
 */
class I18nFileListener : ApplicationComponent, BulkFileListener {
    private val connection = ApplicationManager.getApplication().messageBus.connect()

    override fun initComponent() {
        connection.subscribe(VirtualFileManager.VFS_CHANGES, this)
    }

    override fun disposeComponent() {
        connection.disconnect()
    }

    override fun before(events: List<VFileEvent>) {

    }

    override fun after(events: List<VFileEvent>) {
        for (event in events) {
            if (event.file != null && event.file!!.fileType === I18nFileType.INSTANCE) {
                EditorNotifications.updateAll()
            }
        }
    }

    override fun getComponentName() = "I18nFileListener"
}
