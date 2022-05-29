package com.kep.schoolmeal.model.bot.skill

data class SkillResponse(
    val version: String = "2.0",
    val context: Contexts? = null,
    val template: Template
)

data class Contexts(
    val values: List<Context>
)

data class Template(
    val outputs: List<Output>,
    val quickReplies: List<QuickReply> = listOf()
)

fun toTemplate(vararg output: Output): Template {
    return Template(output.toList())
}

fun toTemplate(output: Output, quickReplies: List<QuickReply>): Template {
    return Template(listOf(output), quickReplies)
}

open class Output

open class Button (
    label: String
): Output()

data class MessageButton(
    val label: String,
    val messageText: String
): Button(label) {
    val action = "message"
}

data class WebLinkButton(
    val label: String,
    val webLinkUrl: String
): Button(label) {
    val action = "webLink"
}

data class BlockButton(
    val label: String,
    val blockId: String
): Button(label) {
    val action = "block"
}

data class PhoneNumberButton (
    val label: String,
    val phoneNumber: String
): Button(label) {
    val action = "phone"
}

class SimpleText(text: String): Output() {
    inner class _SimpleText(val text: String)
    val simpleText = _SimpleText(text)
}

data class ListCardItem(
    val title: String,
    val description: String,
    val imageUrl: String,
    private val linkUrl: String
): Output() {
    inner class Web (
        val web: String
    )
    val link = Web(linkUrl)
}


class ListCard(title: String, imageUrl: String, listItems: List<ListCardItem>, listButtons: List<Button>?): Output() {
    inner class Header(
        val title: String,
        val imageUrl: String
    )

    inner class _ListCard(
        title: String, imageUrl: String, listItems: List<ListCardItem>, listButtons: List<Button>?
    ) {
        val header = Header(title, imageUrl)
        val items = listItems
        val buttons = listButtons
    }

    val listCard = _ListCard(title, imageUrl, listItems, listButtons)
}

class BasicCard (
    title: String,
    description: String,
    imageUrl: String?,
    buttons: List<Button>
): Output() {
    inner class Thumbnail(
        val imageUrl: String
    )

    inner class _BasicCard( val title: String,
                            val description: String,
                            val thumbnail: Thumbnail?,
                            val buttons: List<Button>)

    val basicCard = _BasicCard(title, description, imageUrl?.let {Thumbnail(imageUrl)}, buttons)
}

data class ContextParams(
    val key1: String?
)

data class Context (
    val name: String,
    val lifeSpan: Int,
    val params: ContextParams?
)

open class QuickReply (
    val action: String
)

data class ReplyExtra (
    val type: String,
    val value: String
)

data class MessageQuickReply (
    val label: String,
    val messageText: String
): QuickReply("message")

data class BlockQuickReply (
    val label: String,
    val blockId: String,
    val extra: ReplyExtra? = null
): QuickReply("block")
