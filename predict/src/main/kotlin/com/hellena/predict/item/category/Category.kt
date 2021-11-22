package com.hellena.predict.item.category

import org.springframework.data.repository.CrudRepository
import javax.persistence.*


@Entity
@Table(name = "category")
data class Category (
    @Id
    @GeneratedValue
    @Column(name = "id")
    val id: Long,

    @Column(name = "name")
    val name: String,

    @Column(name = "description")
    val description: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="sub_category")
    val subCategory: Category? = null,
    )

interface CategoryRepository: CrudRepository<Category, Long> {
    override fun findAll(): List<Category>
}
