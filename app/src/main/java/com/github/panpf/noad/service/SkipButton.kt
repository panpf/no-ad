package com.github.panpf.noad.service

class SkipButton(
    val textRegexString: String,
    val className: String,
    val parentClassName: String? = null
) {
    val textRegex = Regex(textRegexString)
}