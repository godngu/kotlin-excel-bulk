package com.godngu.excel.bulk.order.temp

class SearchCategoryKeywordInput {
    val category: SearchCategoryType?
    val keywordInput: String?

    constructor() {
        this.category = null
        this.keywordInput = null
    }

    constructor(category: SearchCategoryType?, input: String?) {
        if (category == null) {
            this.category = null
            this.keywordInput = null
            return
        }
        checkIfInputIsValid(category, input)
        isNullCheckByKeyword(category, input)
        this.category = category
        this.keywordInput = input
    }

    companion object {
        fun checkIfInputIsValid(category: SearchCategoryType, input: String?) {
            if (category.isDigit) {
                input?.takeIf { it.all { char -> char.isDigit() } } ?: throw BusinessException(ErrorCode.INVALID_INPUT_VALUE)
            }
        }

        fun isNullCheckByKeyword(category: SearchCategoryType, keyword: String?) {
            if (keyword.isNullOrEmpty() && category.toString() != "ALL") throw BusinessException(ErrorCode.SEARCH_INPUT_KEYWORD_ERROR)
        }
    }
}
