package cc.cryptopunks.astral.agent.node.handler.content

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import cc.cryptopunks.astral.agent.node.handler.Methods
import cc.cryptopunks.astral.apphost.byte
import cc.cryptopunks.astral.apphost.long
import cc.cryptopunks.astral.apphost.string8
import cc.cryptopunks.astral.apphost.write

fun Context.contentResolverMethods(
    resolver: ContentResolver = contentResolver
): Methods = mapOf(
    Content.info to {
        val uriString = string8
        val info = resolver.info(uriString)
//            gsonSerializer().encodeLine(info) TODO verify
        write(cc.cryptopunks.astral.apphost.gsonSerializer.encode(info).encodeToByteArray())
        byte
    },
    Content.read to {
        val uriString = string8
        val offset = long
        val uri = Uri.parse(uriString)
        val input = resolver.openInputStream(uri)!!
        input.skip(offset)
        byte = 0
        input.use { write(it) }
        byte
    }
)
