import java.lang.RuntimeException

fun main(args: Array<String>) {
    println("Hello Kotlin!")
    println("Ex5. addMessage")
    ChatService.addMessage(1, Message(1, "Hello 1-0"))
    ChatService.addMessage(1, Message(2, "I am 1-1"))
    ChatService.addMessage(2, Message(3, "Hello 2-1"))
    ChatService.addMessage(2, Message(4, "2-2"))
    ChatService.print()

    println("Ex2. getChats")
    println(ChatService.getChats())
    println(ChatService.getChatsId())

    println("Ex1. getUnreadChatsCount. Количество непрочитанных чатов:")
    println(ChatService.getUnreadChatsCount())

    println("Ex6. deletedMessage")
    ChatService.deletedMessage(1, 1)
    ChatService.deletedMessage(1, 2)
    println(ChatService.getChats())

    println("Ex1. getUnreadChatsCount. Количество непрочитанных чатов:")
    println(ChatService.getUnreadChatsCount())

    println("Ex3. lastMessages")
    println(ChatService.lastMessages())

    println("Ex.4 getMessagesId")
    println(ChatService.getMessagesId(2))

    println("Ex.4 getMessagesFollow")
    println(ChatService.getMessagesFollow(1, 1))

    println("Ex.4 getMessagesCount")
    println(ChatService.getMessagesCount(1, 2))

    println("Ex.8 deletedChat")
    ChatService.print()
    println(ChatService.deletedChat(1))
    println(ChatService.deletedChat(2))
    println(ChatService.deletedChat(10))
    ChatService.print()
    println(ChatService.getChats())
    println(ChatService.getChatsId())

}

data class Message(val idMess: Int, val text: String, var deleted: Boolean = false, var read: Boolean = false)
data class Chat(val messages: MutableList<Message> = mutableListOf())
class NoChatException : RuntimeException()

object ChatService {
    private val chats = mutableMapOf<Int, Chat>()
    private var lastIdMess: Int = 0

    init {
        chats[10] = Chat()
        chats[10]?.messages?.add(Message(5, "deleted", true, true))
    }
    fun clear(){
        val chats= emptyMap<Int, Chat>()
        var lastIdMess: Int = 0
    }

    //1 Количество непрочитанных чатов
    fun getUnreadChatsCount() =
        chats.values.map { chat -> chat.messages.filter { it.idMess != 0 && !it.deleted && !it.read } }
            .count { it.isNotEmpty() }

    //2-Получить список чатов (в виде неудалённых сообщений)
    fun getChats() = chats.values.map { chat -> chat.messages.filter { !it.deleted } }

    //2-Получить список чатов (списком их id)
    fun getChatsId() = chats.keys.map { it }

    //3 Разбор задач
    fun lastMessages() = chats.values.map { chat ->
        chat.messages.lastOrNull { !it.deleted }?.text ?: "No message"
    }

    //4-Получить список сообщений из чата, указав ID чата
    fun getMessagesId(userId: Int): List<Message> {
        val chat = chats[userId] ?: throw NoChatException()
        return chat.messages.filter { !it.deleted }.onEach { it.read = true }
    }

    //4-Получить список сообщений из чата, ID последнего сообщения, начиная с которого нужно подгрузить более новые
    fun getMessagesFollow(userId: Int, idMes: Int): List<Message> {
        val chat = chats[userId] ?: throw NoChatException()
        return chat.messages.filter { !it.deleted && it.idMess >= idMes }.onEach { it.read = true }
    }

    //4-Получить список сообщений из чата, указав количество сообщений. Разбор задач
    fun getMessagesCount(userId: Int, count: Int): List<Message> {
        val chat = chats[userId] ?: throw NoChatException()
        return chat.messages.filter { !it.deleted }.takeLast(count).onEach { it.read = true }
    }

    //5 Из разбора задач
    fun addMessage(userId: Int, message: Message) {
        chats.getOrPut(userId) { Chat() }.messages += message.copy(idMess = ++lastIdMess)
    }

    //6-Удалить сообщение.
    fun deletedMessage(userId: Int, messId: Int) {
        val chat = chats[userId] ?: throw NoChatException()
        chat.messages.filter { messId == it.idMess }.onEach { it.deleted = true }
    }

    //8-Удалить чат.
    fun deletedChat(userId: Int) {
        val chat = chats[userId] ?: throw NoChatException()
        chats.remove(userId)
    }

    fun print() {
        for (item in chats) {
            println(item)
        }
    }

}

//fun addMessage(userId: Int, message: Message) {
//        if (chats.containsKey(userId)) {
//            chats[userId]?.messages?.add(message)
//        } else {
//            val chat = Chat()
//            chat.messages += message
//            chats[userId] = chat
//        }
//}