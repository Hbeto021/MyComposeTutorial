package br.com.htolintino.chat.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.htolintino.chat.domain.Message

internal class ChatViewModel : ViewModel() {

    private val mutableMessages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = mutableMessages

    private val messagesList = mutableListOf<Message>()

    fun onSendMessageClick(text: String) {
        if (text.isNotEmpty()) {
            val message = Message(author = "Me", body = text)
            messagesList.add(message)
            showMessages()
        }
    }

    private fun showMessages() {
        mutableMessages.value = messagesList
    }
}
