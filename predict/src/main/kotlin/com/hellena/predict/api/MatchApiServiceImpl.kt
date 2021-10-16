package com.hellena.predict.api

import com.hellena.predict.api.model.MatchDto
import com.hellena.predict.api.model.OodDto
import com.hellena.predict.match.Match
import com.hellena.predict.match.MatchRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.stream.Collectors

@Service
class MatchApiServiceImpl(val matchRepository: MatchRepository): MatchApiService {

    override fun getUsers(): List<MatchDto> {
        return this.matchRepository.findAll().stream()
            .map { toMatchDTO(it) }
            .collect(Collectors.toList())
    }

    fun toMatchDTO(match: Match): MatchDto {
         val oddDto = OodDto(
             tip1 = BigDecimal(match.ood.tip_1),
             tip2 = BigDecimal(match.ood.tip_2),
             tipX = BigDecimal(match.ood.tip_X),
             tip1X = BigDecimal(match.ood.tip_1X),
             tipX2 = BigDecimal(match.ood.tip_X2),
             tip12 = BigDecimal(match.ood.tip_12),
         )

        val dto = MatchDto(
            home = match.home,
            guest = match.guest,
            oodDto = oddDto,
            matchDay = match.matchDay.toString(),
            created = match.created.toString(),
        )

        return dto
    }
}
