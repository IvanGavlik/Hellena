package com.hellena.predict.match

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

data class Match(@Id val id: Long,
                 val home: String,
                 val guest: String,
                 @Embedded.Empty
                 val ood: Odd,
                 @Column("match_day")
                 val matchDay: LocalDate,
                 val created: LocalDateTime) {
}

data class Odd(val tip_1: Double,
               val tip_X: Double,
               val tip_2: Double,
               val tip_1X: Double,
               val tip_X2: Double,
               val tip_12: Double) {}

@Repository
interface MatchRepository: CrudRepository<Match, Long> {

}
