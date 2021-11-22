package com.hellena.predict.match

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table
data class Match(@Id val id: Long,
                 val home: String,
                 val guest: String,
                 @Embedded
                 val ood: Odd,
                 @Column(name = "match_day")
                 val matchDay: LocalDate,
                 val created: LocalDate) {
}

@Embeddable
data class Odd(@Column(name = "tip_1")
               val tip_1: Double,

               @Column(name = "tip_x")
               val tip_X: Double,

               @Column(name = "tip_2")
               val tip_2: Double,

               @Column(name = "tip_1x")
               val tip_1X: Double,

               @Column(name = "tip_x2")
               val tip_X2: Double,

               @Column(name = "tip_12")
               val tip_12: Double) {
}

@Repository
interface MatchRepository: CrudRepository<Match, Long> {
    override fun findAll(): List<Match>
}
