package com.godngu.excel.bulk.order.temp

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

class SearchPeriod {
    val startDate: LocalDateTime
    val endDate: LocalDateTime

    /*
    * 검색 시간 시점을 정할 수 있는 Enum
    * 자정, 저녁 11시 설정 가능
    * 이 후 추가 가능
     */
    enum class TimeType(val endDateTime: LocalDateTime) {
        MIDNIGHT(LocalDate.now().atTime(23, 59, 59)),
        ELEVEN(LocalDate.now().atTime(22, 59, 59))
    }

    /*
    * 기본 값 설정 및 최대 범위 설정 시 사용
    * 범위 설정 단위 (년, 월, 주, 일)
     */
    enum class PeriodType {
        YEAR,
        MONTH,
        WEEK,
        DAY,
    }

    /*
     * 기본 생성자
     * defaultDateRange, defaultPeriodUnit 조합 : 기본값 범위 설정
     * defaultTime : 검색 시간 설정
     * maxRange, maxPeriodUnit
      */
    constructor(
        searchStartDate: LocalDateTime? = null,
        searchEndDate: LocalDateTime? = null,
        defaultDateRange: Int = 1,
        defaultPeriodUnit: PeriodType = PeriodType.YEAR,
        defaultTime: TimeType = TimeType.MIDNIGHT,
        maxRange: Int = defaultDateRange,
        maxPeriodUnit: PeriodType = defaultPeriodUnit,
    ) {

        when {
            searchStartDate == null && searchEndDate == null -> {
                this.endDate = defaultTime.endDateTime
                this.startDate = this.endDate.minus(getPeriod(defaultDateRange, defaultPeriodUnit)).plusSeconds(1)
            }
            searchStartDate == null -> {
                throw BusinessException(ErrorCode.INVALID_INPUT_VALUE)
            }
            searchEndDate == null -> {
                throw BusinessException(ErrorCode.INVALID_INPUT_VALUE)
            }
            else -> {
                checkIfPeriodInputIsValid(searchStartDate, searchEndDate, getPeriod(maxRange, maxPeriodUnit))
                this.startDate = searchStartDate
                this.endDate = searchEndDate
            }
        }
    }

    private fun getPeriod(dateRange: Int, periodUnit: PeriodType): Period = when (periodUnit) {
        PeriodType.YEAR -> Period.ofYears(dateRange)
        PeriodType.MONTH -> Period.ofMonths(dateRange)
        PeriodType.WEEK -> Period.ofWeeks(dateRange)
        PeriodType.DAY -> Period.ofDays(dateRange)
    }

    /*
    * 날짜 검색조건이 유효한지 체크.
    * 최대 범위 내 검색을 요청하는 지 확인
     */
    private fun checkIfPeriodInputIsValid(searchStartDate: LocalDateTime, searchEndDate: LocalDateTime, maxPeriod: Period) {
        if (searchEndDate.isBefore(searchStartDate)) throw BusinessException(ErrorCode.INVALID_INPUT_VALUE)
        if (Duration.between(searchStartDate, searchEndDate) >= Duration.between(searchEndDate.minus(maxPeriod), searchEndDate)) {
            throw BusinessException(ErrorCode.INVALID_INPUT_VALUE)
        }
    }
}
