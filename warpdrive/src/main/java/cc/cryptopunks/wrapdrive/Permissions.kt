package cc.cryptopunks.wrapdrive

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.content.ContextCompat
import cc.cryptopunks.astral.agent.api.Permissions


internal fun Context.requireWritePermissions(block: () -> Unit) {
    if (hasWritePermissions()) block()
    else requestWritePermission()
}

private fun Context.requestWritePermission() =
    startActivity(
        Permissions.request(
            "Warp Drive needs a permission to save downloaded files on your phone.",
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            else
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

private fun Context.hasWritePermissions() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        Environment.isExternalStorageManager()
    else
        applicationContext.hasPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)

private fun Context.hasPermissions(name: String): Boolean =
    ContextCompat.checkSelfPermission(applicationContext, name) ==
        PackageManager.PERMISSION_GRANTED
