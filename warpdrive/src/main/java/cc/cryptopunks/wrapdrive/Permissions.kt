package cc.cryptopunks.wrapdrive

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.Settings
import cc.cryptopunks.astral.agent.api.Permissions
import cc.cryptopunks.astral.agent.api.hasPermissions


internal fun Context.requireWritePermissions(block: () -> Unit) {
    if (hasWritePermissions()) block()
    else requestWritePermission()
}

private fun Context.requestWritePermission() =
    startActivity(
        Permissions.request(
            "Warp Drive needs a permission to save downloaded files on your phone.",
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
            else
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

private fun Context.hasWritePermissions() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        Environment.isExternalStorageManager()
    else
        hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
