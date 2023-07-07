import org.junit.Assert.*
import org.junit.Before

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    @org.junit.Test
    fun addMessage() {
        ChatService.addMessage(1, Message(1, "Text"))
        val x = ChatService.getChatsId().isEmpty()
        assertFalse(x)
    }
}