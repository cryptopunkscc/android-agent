package cc.cryptopunks.astral.agent

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebView

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
            addJavascriptInterface(AppHostAdapter, "_app_host")
//            loadUrl("file:///android_asset/apphost.js")
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
