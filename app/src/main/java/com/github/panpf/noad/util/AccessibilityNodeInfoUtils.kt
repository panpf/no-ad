package com.github.panpf.noad.util

import android.view.accessibility.AccessibilityNodeInfo
import java.util.*

fun AccessibilityNodeInfo.childIterator(): AccessibilityNodeInfoChildIterator {
    return AccessibilityNodeInfoChildIterator(this)
}

fun AccessibilityNodeInfo.childIterable(): Iterable<AccessibilityNodeInfo> {
    return Iterable { childIterator() }
}

fun AccessibilityNodeInfo.deepIterator(): AccessibilityNodeInfoDeepIterator {
    return AccessibilityNodeInfoDeepIterator(this)
}

fun AccessibilityNodeInfo.deepIterable(): Iterable<AccessibilityNodeInfo> {
    return Iterable { deepIterator() }
}

class AccessibilityNodeInfoChildIterator(private val node: AccessibilityNodeInfo) :
    Iterator<AccessibilityNodeInfo> {

    private var index = 0
    private var nextItem: AccessibilityNodeInfo? = null
    private val childCount = node.childCount

    init {
        generateNextItem()
    }

    override fun hasNext(): Boolean {
        return nextItem != null
    }

    private fun generateNextItem() {
        nextItem = if (index < childCount) node.getChild(index++) else null
    }

    override fun next(): AccessibilityNodeInfo {
        val currentItem = nextItem
        generateNextItem()
        return currentItem!!
    }
}

class AccessibilityNodeInfoDeepIterator(node: AccessibilityNodeInfo) :
    Iterator<AccessibilityNodeInfo> {

    private val queue = LinkedList<AccessibilityNodeInfo>()

    init {
        queue.add(node)
    }

    override fun hasNext(): Boolean {
        return queue.isNotEmpty()
    }

    override fun next(): AccessibilityNodeInfo {
        return queue.poll()!!.apply {
            childIterator().forEach {
                queue.add(it)
            }
        }
    }
}