package com.hellena.predict.msg.service

import com.hellena.predict.api.model.MsgDto
import com.hellena.predict.msg.Message
import com.hellena.predict.msg.MessageRepository
import org.springframework.stereotype.Service

interface MessageService {
    fun createMsg(msg: MsgDto);
}

@Service
class MessageServiceImpl(val msgRepository: MessageRepository): MessageService {
    override fun createMsg(msg: MsgDto) {
        var message = Message(
            to = msg.to,
            from = msg.from,
            header = null,
            body = msg.body,
        )
        if(msg.header != null) {
            message.header = msg.header
        }
        msgRepository.save( message);
    }
}
