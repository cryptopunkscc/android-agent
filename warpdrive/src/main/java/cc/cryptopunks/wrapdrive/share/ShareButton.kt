package cc.cryptopunks.wrapdrive.share

import android.content.Context
import android.content.Intent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import cc.cryptopunks.wrapdrive.R

fun Context.startShareActivity() = startActivity(Intent(this, ShareActivity::class.java))

@Composable
fun ShareButton() {
    val context = LocalContext.current
    ShareButton { context.startShareActivity() }
}

@Composable
fun ShareButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_share_white_24dp),
            contentDescription = "share"
        )
    }
}
