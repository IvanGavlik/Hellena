package com.hellena.predict.item.image

import org.hibernate.annotations.Type
import org.springframework.data.repository.CrudRepository
import javax.persistence.*

@Entity
@Table(name = "item_image")
data class Image(
    @Column(name = "name", nullable = false)
    val name: String,

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "content", columnDefinition="BLOB")
    val content: ByteArray,
) {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name = "id")
    var id: Long? = null;
}

interface ImageRepository: CrudRepository<Image, Long> {}
