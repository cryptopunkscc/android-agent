package cc.cryptopunks.astral.agent.js

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import cc.cryptopunks.astral.bind.astral.Astral
import cc.cryptopunks.astral.bind.astral.JsAppHostClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID

internal class JsAppHostAdapter(
    private val webView: WebView,
    private val client: JsAppHostClient = Astral.newJsAppHostClient(),
) : CoroutineScope {

    override val coroutineContext = SupervisorJob()

    @JavascriptInterface
    fun log(var1: String) = client.log(var1)

    @JavascriptInterface
    fun logArr(var1: String) = client.logArr(var1)

    @JavascriptInterface
    fun sleep(var1: Long) = promise { client.sleep(var1) }

    @JavascriptInterface
    fun connAccept(var1: String) = promise { client.connAccept(var1) }

    @JavascriptInterface
    fun connClose(var1: String) = promise { client.connClose(var1) }

    @JavascriptInterface
    fun connRead(var1: String) = promise { client.connRead(var1) }

    @JavascriptInterface
    fun connWrite(var1: String, var2: String) = promise { client.connWrite(var1, var2) }

    @JavascriptInterface
    fun query(var1: String?, var2: String) = promise { client.query(var1, var2) }

    @JavascriptInterface
    fun queryName(var1: String, var2: String) = promise { client.queryName(var1, var2) }

    @JavascriptInterface
    fun resolve(var1: String) = promise { client.resolve(var1) }

    @JavascriptInterface
    fun serviceClose(var1: String) = promise { client.serviceClose(var1) }

    @JavascriptInterface
    fun serviceRegister(var1: String) = promise { client.serviceRegister(var1) }

    @JavascriptInterface
    fun nodeInfo(var1: String) = promise {
        client.getNodeInfo(var1)?.run {
            """{"name":"$name","identity":"$identity"}"""
        } ?: "{}"
    }

    private fun <T> promise(block: suspend CoroutineScope.(UUID) -> T): String {
        val id = UUID.randomUUID()
        launch(Dispatchers.IO) {
            val fn = try {
                val result = block(id).takeIf { it != Unit }
                val quoted = JSONObject.quote(result?.toString())
                "window._resolve(\"$id\", $quoted)"
            } catch (e: Throwable) {
                val message = e.message
                "window._reject(\"$id\", \"$message\")"
            }
            launch(Dispatchers.Main) {
                webView.evaluateJavascript(fn) {
                    Log.d(Tag, "done: $fn, $it")
                }
            }
        }
        return id.toString()
    }

    companion object {
        private val Tag = JsAppHostAdapter::class.java.simpleName
    }
}
