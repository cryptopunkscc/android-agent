package cc.cryptopunks.astral.agent

import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

object ProcessRunner {
    fun run(dir: String, command: String) {
        // Create the process builder
        val processBuilder = ProcessBuilder()
        val cmd = command.split(" ").dropLastWhile { it.isEmpty() }
        processBuilder.directory(File(dir))
        processBuilder.command(cmd)
        processBuilder.redirectErrorStream(true) // Redirect stderr to stdout

        // Start the process
        val process = processBuilder.start()

        // Read the process output
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            Log.d(TAG, line!!) // Redirect output to logcat
        }

        // Wait for the process to complete
        val exitCode = process.waitFor()
        if (exitCode == 0)
            Log.d(TAG, "Process exited with code: 0")
        else
            throw Exception("Process exited with code: $exitCode")
    }

    private const val TAG = "ProcessRunner"
}
