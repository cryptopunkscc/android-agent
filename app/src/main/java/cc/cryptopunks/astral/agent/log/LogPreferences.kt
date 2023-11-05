package cc.cryptopunks.astral.agent.log

import android.content.Context
import cc.cryptopunks.astral.agent.compose.mutableStateOf

class LogPreferences(
    context: Context,
) {
    private val sharedPreferences = context.getSharedPreferences("log", Context.MODE_PRIVATE)

    var softWrap = sharedPreferences.mutableStateOf("soft_wrap", true)
}
