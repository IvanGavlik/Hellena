package com.hellena.predict.match

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

data class Match(@Id val id: Long,
                 val home: String,
                 val guest: String,
                 @Embedded.Empty
                 val ood: Odd,
                 @Column("match_day")
                 val matchDay: LocalDate,
                 val created: LocalDate) {
}

data class Odd(@Column("tip_1")
               val tip_1: Double,
               @Column("tip_x")
               val tip_X: Double,
               @Column("tip_2")
               val tip_2: Double,
               @Column("tip_1x")
               val tip_1X: Double,
               @Column("tip_x2")
               val tip_X2: Double,
               @Column("tip_12")
               val tip_12: Double) {}

@Repository
interface MatchRepository: CrudRepository<Match, Long> {
    override fun findAll(): List<Match>
}
