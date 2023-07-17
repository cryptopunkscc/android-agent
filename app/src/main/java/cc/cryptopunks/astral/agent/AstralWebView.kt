package cc.cryptopunks.astral.agent

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class AstralWebView : Activity() {

    private val webView: WebView by lazy { WebView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(webView)
        val name = intent.getStringExtra("name") ?: return
        webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.apply {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            addJavascriptInterface(AppHostAdapter(webView), "_app_host")
            loadUrl("file:///android_asset/apphost_android.js")
            val source = assets.open(name).reader().readText()
            loadDataWithBaseURL("file:///android_asset/apphost.js", source, "html/text", "UTF-8", null)
        }
    }

    companion object {
        fun intent(context: Context, appName: String) =
            Intent(context, AstralWebView::class.java).apply {
                putExtra("name", appName)
            }
    }
}

fun Context.jsAppIntent(name: String) = AstralWebView.intent(this, name)
