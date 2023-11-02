package cc.cryptopunks.astral.agent.util

import android.app.Activity
import android.app.Service
import android.content.Context

inline fun <reified T> Context.use(): T = when(this) {
    is Activity -> application
    is Service -> application
    else -> applicationContext
} as? T ?: throw IllegalArgumentException()
