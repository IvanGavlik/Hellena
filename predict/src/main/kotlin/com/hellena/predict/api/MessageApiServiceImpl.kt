package com.hellena.predict.api

import com.hellena.predict.api.model.MsgDto
import com.hellena.predict.msg.service.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class MessageApiServiceImpl(val msgService: MessageService): MsgApiDelegate {

    override fun createMsg(msgDto: MsgDto): ResponseEntity<Unit> {
        msgService.createMsg(msgDto);
        return ResponseEntity(HttpStatus.OK);
    }
}
