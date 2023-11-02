package cc.cryptopunks.astral.agent.js

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.core.net.toFile
import astral.AppHostClient
import astral.Astral
import java.io.File

internal fun Context.startJsAppActivity(app: JsApp) {
    val intent = Intent(this, JsAppActivity::class.java)
    val dir = appsDir.resolve(app.dir)
    val file = dir.resolve("index.html")
    val uri = Uri.fromFile(file)
    intent.data = uri
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

class JsAppActivity : ComponentActivity() {

    private val webView: WebView by lazy { WebView(this) }
    private val appHostClient: AppHostClient by lazy { Astral.newAppHostClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(webView)

        val uri = intent.data ?: return
        val dir = uri.toFile().parentFile ?: File("")

        webView.apply {
            webViewClient = FileResolvingWebViewClient(dir)
            webChromeClient = WebChromeClient()
            settings.apply {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                allowFileAccess = true
                allowContentAccess = true
                domStorageEnabled = true
                databaseEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
            }

            addJavascriptInterface(JsAppHostAdapter(webView, appHostClient), "_app_host")
            loadUrl(uri.toString())
        }

        onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appHostClient.close()
    }
}

private class FileResolvingWebViewClient(
    private val dir: File,
) : WebViewClient() {

    private val tag = javaClass.simpleName

    override fun shouldInterceptRequest(
        view: WebView,
        request: WebResourceRequest,
    ): WebResourceResponse? {
        when (request.url.scheme) {
            "file" -> {

                // Resolve requested file
                val path = request.url.path ?: return null
                Log.d(tag, "requesting path: $path")
                val relative = if (path.startsWith('/')) path.drop(1) else path
                val file = dir.resolve(relative)
                Log.d(tag, "resolved file: ${file.absolutePath}, exist: ${file.exists()}")
                file.exists() || return null

                // Prepare response data
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
                val encoding = "UTF-8"
                val data = file.inputStream()

                return WebResourceResponse(mimeType, encoding, data)
            }

            else -> {
                return super.shouldInterceptRequest(view, request)
            }
        }
    }
}
