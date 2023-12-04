package cc.cryptopunks.astral.agent.js

import android.content.Context
import android.net.Uri
import android.util.Log
import cc.cryptopunks.astral.bind.astral.Astral
import cc.cryptopunks.astral.bind.astral.Worker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.UUID

class JsAppsManager(
    private val context: Context,
    private val activities: JsAppActivities,
) {
    private val tag = javaClass.simpleName

    private val _apps = MutableStateFlow(
        context.listApps().map { app -> app.copy(activity = activities[app.name]) }
    )

    private val running = mutableMapOf<String, Worker>()

    init {
        context.appsDir.mkdirs()
    }

    internal val apps: StateFlow<List<JsApp>> get() = _apps

    private operator fun get(name: String): JsApp? {
        return _apps.value.find { it.name == name }
    }

    internal fun startServices() {
        _apps.value.filterNot { app ->
            running[app.name]?.isActive == true
        }.forEach { app ->
            startService(app.name)
        }
    }

    internal fun stopServices() {
        _apps.value.filter { app ->
            running[app.name]?.isActive == true
        }.forEach { app ->
            stopService(app.name)
        }
    }

    @Throws(
        FileNotFoundException::class,
        UnpackZipException::class,
    )
    internal fun install(uri: Uri): JsApp {
        val activity = activities.nextId()
        val appsDir = context.appsDir

        // Resolve stream from bundle uri
        val stream = context.contentResolver.openInputStream(uri)
        stream ?: throw NullPointerException(
            "ContentResolver returned null while trying to retrieve InputStream for Uri $uri."
        )

        // Create app temporary dir
        val tempName = UUID.randomUUID().hashCode().toString()
        val tempDir = appsDir.resolve(tempName)

        // Unpack stream to temporary dir
        try {
            unpackZip(stream, tempDir)
        } catch (e: Throwable) {
            tempDir.deleteRecursively()
            throw e
        }

        // Verify app metadata
        var app = try {
            tempDir.getApp()
        } catch (e: Throwable) {
            tempDir.deleteRecursively()
            throw e
        }

        // Create app final dir
        val finalName = app.name.lowercase().replace(" ", "-").replace("_", "-")
        val finalDir = appsDir.resolve(finalName)

        // Remove previous installation if exist
        if (finalDir.exists()) {
            uninstall(finalName)
        }

        try {
            // Rename temporary dir to final
            val renamed = tempDir.renameTo(finalDir)
            renamed || throw IOException(
                "Failed to rename temporary directory to the final location"
            )
        } catch (e: Throwable) {
            tempDir.deleteRecursively()
            finalDir.deleteRecursively()
            throw e
        }

        // Finalize
        app = app.copy(
            dir = finalName,
            activity = activity,
        )
        activities[app.name] = activity
        Log.d(tag, "installed js app ${app.name}")
        _apps.update { list ->
            if (list.contains(app)) list
            else list + app
        }
        startService(app.name)

        return app
    }

    private fun startService(name: String) {
        running[name]?.isActive == true && return
        val app = get(name) ?: return
        app.service ?: return
        try {
            val dir = context.appsDir.resolve(app.dir)
            val file = dir.resolve(app.service)
            val source = file.readText()
            Log.d(tag, "starting service $name")
            running[name] = Astral.runGojaJsBackend(source)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    internal fun uninstall(name: String) {
        val app = get(name) ?: return

        // cancel running service
        stopService(name)

        // remove app directory
        val dir = context.appsDir.resolve(app.dir)
        dir.deleteRecursively()

        // detach activity id
        activities[app.name] = -1

        // remove from state
        _apps.update { list ->
            list.filter { next ->
                next.name != name
            }
        }
        Log.d(tag, "uninstalled js app $name")
    }

    private fun stopService(name: String) {
        val job = running.remove(name) ?: return
        job.cancel()
        Log.d(tag, "stopped service $name")
    }
}

private fun Context.listApps(): List<JsApp> {
    return appsDir.listFiles(File::isDirectory).orEmpty().mapNotNull { dir ->
        try {
            dir.getApp()
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }
}

private fun File.getApp(): JsApp {
    val manifestFile = resolve("manifest.json")
    val manifest = manifestFile.readText()
    val json = JSONObject(manifest)

    return JsApp(
        dir = path,
        name = json.getString("short_name"),
        description = json.optString("name"),
        icon = json.getJSONArray("icons").toList().map {
            it.getString("src")
        }.firstOrNull {
            it.endsWith("png")
        },
        service = json.optString("service").takeIf { it.isNotBlank() },
    )
}

private fun JSONArray.toList() =
    buildList<JSONObject> {
        (0 until length()).forEach { index ->
            add(getJSONObject(index))
        }
    }
