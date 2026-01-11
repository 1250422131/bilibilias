package com.imcys.bilibilias.ui.tools.parser

import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.setWebContentsDebuggingEnabled
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.common.event.AnalysisEvent
import com.imcys.bilibilias.common.event.sendAnalysisEvent
import com.imcys.bilibilias.common.utils.openLink
import com.imcys.bilibilias.ui.weight.ASIconButton
import com.imcys.bilibilias.ui.weight.ASTopAppBar
import com.imcys.bilibilias.ui.weight.AsBackIconButton
import com.imcys.bilibilias.ui.weight.BILIBILIASTopAppBarStyle
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Serializable
data object WebParserRoute : NavKey

@Composable
fun WebParserScreen(webParserRoute: WebParserRoute, onToBack: () -> Unit) {
    val vm = koinViewModel<WebParserViewModel>()
    val uiState by vm.uiState.collectAsState()
    WebParserScaffold(onToBack, onToAs = {
        sendAnalysisEvent(AnalysisEvent(uiState.currentUrl))
    }) {
        WebParserContent(it, uiState.currentUrl, onUpdateUrl = { url ->
            vm.updateCurrentUrl(url)
        })
    }
}

@Composable
fun WebParserContent(
    paddingValues: PaddingValues,
    currentUrl: String,
    onUpdateUrl: (String) -> Unit = {}
) {
    var loadProgress by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.allowFileAccess = false

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setWebContentsDebuggingEnabled(true)
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    loadProgress = newProgress
                }
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return true
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    handleRouterChange(view)
                    handleHomePageTipButton(view)
                }

            }
            addJavascriptInterface(object : Any() {
                @JavascriptInterface
                fun onRouteChange(url: String?) {
                    onUpdateUrl(url ?: "")
                }

                @JavascriptInterface
                fun onUniversalLink(link: String) {
                    loadUrl(link)
                }
            }, "AndroidBridge")
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            webView.stopLoading()
            webView.removeAllViews()
            webView.destroy()
        }
    }
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { webView },
            update = { webView -> webView.loadUrl(currentUrl) }
        )

        if (loadProgress in 1..99) {
            LinearProgressIndicator(
                progress = { loadProgress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .align(Alignment.TopStart)
            )
        }
    }
}


private fun handleHomePageTipButton(view: WebView?) {
    val js =
        """
            var btns = document.querySelectorAll('.v5-button.m-fixed-openapp.v5-button--large.v5-button--primary.v5-button--block');
            btns.forEach(function(btn){
                btn.remove();
            });
        """.trimIndent()
    view?.evaluateJavascript(js, null)
}

private fun handleRouterChange(view: WebView?) {
    val routeJs = """
            (function() {
                function notifyJava(route){
                    if(window.AndroidBridge && window.AndroidBridge.onRouteChange){
                        window.AndroidBridge.onRouteChange(route);
                    }
                }
                window.addEventListener('popstate', function() {
                    notifyJava(window.location.href);
                });
                window.addEventListener('hashchange', function() {
                    notifyJava(window.location.href);
                });
                var pushState = history.pushState;
                var replaceState = history.replaceState;
                history.pushState = function(){
                    pushState.apply(history, arguments);
                    notifyJava(window.location.href);
                }
                history.replaceState = function(){
                    replaceState.apply(history, arguments);
                    notifyJava(window.location.href);
                }
            })();
    """
    view?.evaluateJavascript(routeJs, null)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WebParserScaffold(
    onToBack: () -> Unit,
    onToAs: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Column {
                ASTopAppBar(
                    style = BILIBILIASTopAppBarStyle.Small,
                    title = {
                        Text(text = "网页解析")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    navigationIcon = {
                        AsBackIconButton(onClick = {
                            onToBack.invoke()
                        })
                    },
                    actions = {
                        ASIconButton(onClick = {
                            onToAs()
                        }) {
                            Icon(Icons.Outlined.Check, contentDescription = "解析当前页面")
                        }
                    }
                )
            }
        },
    ) {
        content.invoke(it)
    }

}
