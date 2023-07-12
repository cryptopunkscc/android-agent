package cc.cryptopunks.astral.agent

import android.content.Context

private const val BackendFilepath = "bin/astral-goja-android-arm64"

fun Context.copyGojaBackend() {
    // prepare bind dir
    dataDir.resolve("bin").mkdir()

    // copy file
    val outFile = dataDir.resolve(BackendFilepath)
    val outStream = outFile.outputStream()
    val inStream = assets.open(BackendFilepath)

    inStream.use { i ->
        outStream.use { o ->
            i.copyTo(o)
            o.flush()
        }
    }

    // set executable
    outFile.setExecutable(true)
}
