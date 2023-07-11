package cc.cryptopunks.astral.agent

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAstralService()
        setContent {
            AstralTheme {
                val status by astralStatus.collectAsState()
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = status.name
                    )
                }
            }
        }
        GlobalScope.launch {
            astralStatus.filter { it == AstralStatus.Started }.collect {
                startActivity(Intent(this@MainActivity, AstralWebView::class.java))
            }
        }
    }
}


@Composable
fun AstralTheme(
    content: @Composable () -> Unit,
) = MaterialTheme(
    colors = lightColors(
        primary = Color(0xDE000000),
        primaryVariant = Color(0xFF000000),
    ),
    content = content
)
