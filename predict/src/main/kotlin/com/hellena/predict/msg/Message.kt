package com.hellena.predict.msg

import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity
@Table(name = "msg")
data class Message(
    @Column(name = "msg_from", nullable = false)
    val from: String,

    @Column(name = "msg_to", nullable = false)
    val to: String,

    @Column(name = "header")
    var header: String?,

    @Column(name = "body", nullable = false)
    val body: String,

    ) {

    @Id
    @GeneratedValue
    @Column(name = "id")
    var id: Long? = null
}

interface MessageRepository: CrudRepository<Message, Long> {
}
