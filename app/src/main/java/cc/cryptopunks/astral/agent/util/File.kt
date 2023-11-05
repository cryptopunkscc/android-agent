package cc.cryptopunks.astral.agent.util

import android.os.Build
import android.os.FileObserver
import java.io.File

fun File.fileObserver(
    mask: Int,
    onEvent: (event: Int, path: String?) -> Unit,
): FileObserver = when {

    Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
    -> object : FileObserver(absolutePath, mask) {
        override fun onEvent(event: Int, path: String?) = onEvent(event, path)
    }

    else
    -> object : FileObserver(this, mask) {
        override fun onEvent(event: Int, path: String?) = onEvent(event, path)
    }
}
