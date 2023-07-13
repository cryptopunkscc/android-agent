package cc.cryptopunks.astral.agent

import android.content.Context

const val GojaBackendFilepath = "bin/astral-goja-android-arm64"
const val GojaBackendFilename = "astral-goja-android-arm64"

fun Context.copyGojaBackend() {
    // prepare bind dir
    dataDir.resolve("bin").mkdir()

    // copy file
    val outFile = dataDir.resolve(GojaBackendFilepath)
    val outStream = outFile.outputStream()
    val inStream = assets.open(GojaBackendFilepath)

    inStream.use { i ->
        outStream.use { o ->
            i.copyTo(o)
            o.flush()
        }
    }

    // set executable
    outFile.setExecutable(true, false)
}
