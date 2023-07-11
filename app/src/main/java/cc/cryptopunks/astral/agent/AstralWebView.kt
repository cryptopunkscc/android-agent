package cc.cryptopunks.astral.agent

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import astral.Astral

class AstralWebView : Activity() {

    private val webView: WebView by lazy { WebView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(webView)
        webView.apply {
            settings.apply {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            addJavascriptInterface(AppHostAdapter(), "_app_host")
            loadUrl("file:///android_asset/apphost.js")
            loadUrl("file:///android_asset/apphost_android.js")
//            loadUrl("file:///android_asset/node_info.html")
            loadDataWithBaseURL("file:///android_asset/apphost.js", index, "html/text", "UTF-8", null)
//            loadDataWithBaseURL(null, index, "html/text", "UTF-8", null)
        }
    }
}

val index = """
<!DOCTYPE html>
<html lang="en">
<script src="apphost_android.js"></script>
<script src="apphost.js"></script>
<p id="info">uninitialized</p>
<script>
    async function run() {
        let id = await appHost.resolve("localnode")
        let info = await appHost.nodeInfo(id)
        document.getElementById("info").innerText = JSON.stringify(info)
    }

    run()
</script>
</html>
"""


private class AppHostAdapter {
    private val client = Astral.newAppHostClient()

    @JavascriptInterface
    fun log(var1: String) = client.log(var1)

    @JavascriptInterface
    fun sleep(var1: Long) = client.sleep(var1)

    @JavascriptInterface
    fun connAccept(var1: String): String = client.connAccept(var1)

    @JavascriptInterface
    fun connClose(var1: String) = client.connClose(var1)

    @JavascriptInterface
    fun connRead(var1: String): String = client.connRead(var1)

    @JavascriptInterface
    fun connWrite(var1: String, var2: String) = client.connWrite(var1, var2)

    @JavascriptInterface
    fun query(var1: String, var2: String): String = client.query(var1, var2)

    @JavascriptInterface
    fun queryName(var1: String, var2: String): String = client.queryName(var1, var2)

    @JavascriptInterface
    fun resolve(var1: String): String = client.resolve(var1)

    @JavascriptInterface
    fun serviceClose(var1: String) = client.serviceClose(var1)

    @JavascriptInterface
    fun serviceRegister(var1: String) = client.serviceRegister(var1)

    @JavascriptInterface
    fun nodeInfo(var1: String): String = client.getNodeInfo(var1)?.run {
        println("node info: $this")
        """{"name":"$name","identity":"$identity"}"""
    } ?: "{}"
}
