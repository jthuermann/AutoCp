package com.github.pushpavel.autocp.testcasetool.components

import com.github.pushpavel.autocp.common.helpers.doDisposal
import com.github.pushpavel.autocp.common.ui.helpers.setter
import com.github.pushpavel.autocp.core.persistance.storables.testcases.Testcase
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.ui.CollectionListModel
import com.intellij.ui.components.JBPanel
import javax.swing.BoxLayout
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class TestcaseListPanel : JBPanel<TestcaseListPanel>(), ListDataListener, Disposable {

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
    }

    var model: CollectionListModel<Testcase>? by setter(null) {
        value?.removeListDataListener(this@TestcaseListPanel)
        if (it != null) {
            value = it
            if (!it.isEmpty)
                intervalAdded(ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, it.items.size - 1))
            it.addListDataListener(this@TestcaseListPanel)
        }
    }

    override fun intervalAdded(e: ListDataEvent) {
        for (i in e.index0..e.index1) {
            model?.let {
                val content = TestcaseContent(it)
                add(content, i)
                content.update(it.getElementAt(i))
                Disposer.register(this, content)
            }
        }

        updateUI()
    }

    override fun intervalRemoved(e: ListDataEvent) {
        for (i in e.index0..e.index1) {
            val component = getComponent(e.index0) as TestcaseContent
            component.doDisposal()
            remove(component)
        }
        updateUI()
    }

    override fun contentsChanged(e: ListDataEvent) {
        for (i in e.index0..e.index1) {
            val testcase = model?.getElementAt(i) ?: continue
            val component = getComponent(i) as TestcaseContent?
            component?.update(testcase)
        }
    }

    override fun dispose() {}
}