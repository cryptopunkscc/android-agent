package cc.cryptopunks.astral.agent

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class AstralWebView : Activity() {

    private val webView: WebView by lazy { WebView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(webView)
        webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.apply {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            addJavascriptInterface(AppHostAdapter, "_app_host")
            loadUrl("file:///android_asset/apphost_android.js")
            loadDataWithBaseURL("file:///android_asset/apphost.js", index2, "html/text", "UTF-8", null)
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

val index2 = """
<!DOCTYPE html>
<html lang="en">
<script src="apphost_android.js"></script>
<script src="apphost.js"></script>
<script>
    log("test log")
    async function run() {
        let conn = await appHost.queryName("", "hello")
        await conn.write("hello I am frontend")
        let result = await conn.read()
        await conn.close()
        alert(result)
    }
    run()
    run()
    run()
    run()
    run()
    run()
    run()
    run()
    run()
</script>
</html>
""".trimIndent()
