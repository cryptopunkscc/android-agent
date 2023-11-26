package cc.cryptopunks.astral.agent.node.handler.content

import android.content.Context
import android.net.Uri
import astral.ContentInfo
import cc.cryptopunks.astral.apphost.Conn
import cc.cryptopunks.astral.apphost.write
import cc.cryptopunks.astral.agent.node.Conn as ConnAdapter

class ContentResolver(
    context: Context,
    private val conn: Conn,
) : astral.ContentResolver {

    private val resolver = context.contentResolver

    override fun string(): String {
        return "android/content/jrpc"
    }

    override fun info(uri: String): ContentInfo {
        val info = resolver.info(uri)
        return ContentInfo().apply {
            this.uri = uri
            mime = info.mime
            name = info.name
            size = info.size
        }
    }

    override fun reader(uri: String, offset: Long) {
        val parsedUri = Uri.parse(uri)
        val input = resolver.openInputStream(parsedUri)!!
        input.skip(offset)
        input.use { conn.write(it) }
    }
}

fun ContentResolverFactory(context: Context) =
    astral.ContentResolverFactory {
        ContentResolver(context, ConnAdapter(it))
    }
