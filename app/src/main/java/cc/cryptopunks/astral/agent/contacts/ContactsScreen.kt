package cc.cryptopunks.astral.agent.contacts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun ContactsScreen(
    model: ContactsViewModel = koinViewModel(),
    select: ((String) -> Unit)? = null,
) {
    val contacts by model.state.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(contacts) { contact ->
            ContactItem(
                contact = contact,
                select = select,
            )
        }
    }
}

@Composable
fun ContactItem(
    contact: Contact,
    select: ((String) -> Unit)? = null,
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .clickable(
            enabled = select != null
        ) {
            select?.invoke(contact.id)
        }
        .padding(16.dp)
) {
    Text(
        text = contact.alias,
        style = MaterialTheme.typography.subtitle1
    )
    Spacer(modifier = Modifier.size(4.dp))
    Text(
        text = contact.id.takeLast(10),
        style = MaterialTheme.typography.caption,
        fontFamily = FontFamily.Monospace
    )
}
