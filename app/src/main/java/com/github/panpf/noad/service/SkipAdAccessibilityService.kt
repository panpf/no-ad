package com.github.panpf.noad.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.github.panpf.noad.util.deepIterator
import com.github.panpf.tools4a.toast.ktx.showShortToast
import kotlinx.coroutines.*

class SkipAdAccessibilityService : AccessibilityService() {

    companion object {
        const val TAG = "SkipAd"
        const val CLICK_INTERVAL = 5000
    }

    private var scope: CoroutineScope? = null
    private var job: Job? = null
    private val skipAppList = listOf(
        SkipApp(
            "com.tencent.qqlive",
            listOf(SkipButton("^跳过$", "android.widget.TextView", "android.widget.LinearLayout"))
        )
    )
    private val skipAppMap = skipAppList.map { it.packageName to it }.toMap()
    private var lastClickButton: SkipButton? = null
    private var lastClickTime: Long = 0
    private var printStructure = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        serviceInfo = (serviceInfo ?: AccessibilityServiceInfo()).apply {
            packageNames = skipAppList.map { it.packageName }.toTypedArray()
            eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        job?.cancel()
        val skipApp = skipAppMap[event.packageName] ?: return
        val rootInActiveWindow = rootInActiveWindow ?: return
        job = scope?.launch {
            delay(100)
            work(rootInActiveWindow, skipApp)
        }
    }

    private fun work(rootInActiveWindow: AccessibilityNodeInfo, skipApp: SkipApp) {
        if (printStructure) {
            printlnNode(rootInActiveWindow)
        }

        val pair = findTargetNode(rootInActiveWindow, skipApp)
        if (pair != null) {
            if (canClick(pair.first)) {
                if (clickNode(pair.second, pair.first)) {
                    Log.i(TAG, "click skip node")
                    showShortToast("自动点击跳过按钮")
                } else {
                    Log.w(TAG, "not found clickable parent node")
                }
            } else {
                Log.w(TAG, "Filter duplicate click")
            }
        } else {
            Log.w(TAG, "not found target node")
        }
    }

    private fun printlnNode(rootNode: AccessibilityNodeInfo?) {
        rootNode?.deepIterator()?.forEach { node ->
            Log.d(
                TAG,
                "Node: ${node.className}, text='${node.text}', isClickable='${node.isClickable}', contentDescription='${node.contentDescription}'"
            )
        }
    }

    private fun findTargetNode(
        rootNode: AccessibilityNodeInfo?,
        skipApp: SkipApp
    ): Pair<SkipButton, AccessibilityNodeInfo>? {
        rootNode ?: return null
        var targetNode: AccessibilityNodeInfo? = null
        var targetButton: SkipButton? = null
        for (node in rootNode.deepIterator()) {
            val nodeText = node.text?.takeIf { it.isNotEmpty() }
            if (nodeText != null) {
                val nodeClassName = node.className
                targetButton = skipApp.buttons.find { button ->
                    button.className == nodeClassName && button.textRegex.matches(nodeText)
                }
            }
            if (targetButton != null) {
                targetNode = node
                break
            }
        }
        return if (targetNode != null && targetButton != null) {
            targetButton to targetNode
        } else {
            null
        }
    }

    private fun canClick(button: SkipButton): Boolean {
        val lastClickButton = lastClickButton
        return lastClickButton == null
                || lastClickButton !== button
                || (System.currentTimeMillis() - lastClickTime) >= CLICK_INTERVAL
    }

    private fun clickNode(node: AccessibilityNodeInfo, button: SkipButton): Boolean {
        val clickableParentNode = findClickableParentNode(node)
        return if (clickableParentNode != null) {
            clickableParentNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            lastClickButton = button
            lastClickTime = System.currentTimeMillis()
            true
        } else {
            false
        }
    }

    private fun findClickableParentNode(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        if (node.isClickable) {
            return node
        }
        val parent = node.parent
        if (parent != null) {
            return findClickableParentNode(parent)
        }
        return null
    }

    override fun onInterrupt() {
        scope?.cancel("onInterrupt")
    }
}