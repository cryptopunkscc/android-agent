package cc.cryptopunks.astral.agent

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import astral.Astral
import astral.Worker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

data class JsApp(
    val dir: String,
    val name: String,
    val description: String,
    val icon: String?,
    val service: String?,
)

val Context.appsDir get() = dataDir.resolve("apps")

class JsAppsManager(
    private val context: Application,
) {
    private val tag = javaClass.simpleName

    private val _apps = MutableStateFlow(context.listApps())

    private val running = mutableMapOf<String, Worker>()

    init {
        context.appsDir.mkdirs()
    }

    val apps: StateFlow<List<JsApp>> get() = _apps

    fun startServices() {
        _apps.value.filterNot { app ->
            running[app.name]?.isActive == true
        }.forEach { app ->
            startService(app.name)
        }
    }

    fun install(uri: Uri): JsApp? {
        val stream = context.contentResolver.openInputStream(uri)
        val name = Uri.parse(uri.lastPathSegment)
            .lastPathSegment
            ?.replace(".zip", "")

        if (stream != null && name != null) {
            context.appsDir.unpackZip(stream, name)

            val appDir = context.appsDir.resolve(name)
            val app = appDir.getApp()

            _apps.update { it + app }
            Log.d(tag, "installed js app $name")

            startService(app.name)

            return app
        }
        return null
    }

    operator fun get(name: String): JsApp? {
        return _apps.value.find { it.name == name }
    }

    fun startService(name: String) {
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

    fun stopService(name: String) {
        val job = running.remove(name) ?: return
        job.cancel()
        Log.d(tag, "stopped service $name")
    }

    fun uninstall(name: String) {
        val app = get(name) ?: return

        // cancel running service
        stopService(name)

        // remove app directory
        val dir = context.appsDir.resolve(app.dir)
        dir.deleteRecursively()

        // remove from state
        _apps.update { list ->
            list.filter { next ->
                next.name != name
            }
        }
        Log.d(tag, "uninstalled js app $name")
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
    val manifest = resolve("manifest.json").readText()
    val json = JSONObject(manifest)

    return JsApp(
        dir = path,
        name = json.getString("short_name"),
        description = json.getString("name"),
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
