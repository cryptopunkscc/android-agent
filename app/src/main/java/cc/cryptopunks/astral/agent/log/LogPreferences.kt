package cc.cryptopunks.astral.agent.log

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import cc.cryptopunks.astral.agent.compose.mutableStateOf

class LogPreferences(
    context: Context,
) {
    private val sharedPreferences = context.getSharedPreferences("log_prefs", Context.MODE_PRIVATE)

    var softWrap by sharedPreferences.mutableStateOf("soft_wrap", true)
}
