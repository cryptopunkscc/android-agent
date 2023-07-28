package cc.cryptopunks.astral.agent

import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun unpackZip(inputStream: InputStream, outputDir: File): Unit = try {
    outputDir.mkdirs()

    ZipInputStream(BufferedInputStream(inputStream)).use { zis ->

        val buffer = ByteArray(1024)
        var ze: ZipEntry
        var filename: String
        var count: Int
        while (true) {
            ze = zis.nextEntry ?: break

            filename = ze.name

            // Need to create directories if not exists, or
            // it will generate an Exception...
            if (ze.isDirectory) {
                val fmd = outputDir.resolve(filename)
                fmd.mkdirs()
                continue
            }
            val file = outputDir.resolve(filename)
            val out = file.outputStream()
            out.use {
                while (zis.read(buffer).also { count = it } != -1) {
                    out.write(buffer, 0, count)
                }
            }

            zis.closeEntry()
        }
    }
} catch (e: Throwable) {
    throw UnpackZipException(e)
}

class UnpackZipException(cause: Throwable) : IOException(cause.message, cause)
