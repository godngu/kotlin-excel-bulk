package com.godngu.excel.bulk.order.temp

open class BusinessException : RuntimeException {
    val errorCode: ErrorCode

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }

    constructor(errorCode: ErrorCode, message: String) : super(message) {
        this.errorCode = errorCode
    }
}

/**
 * Feign Client Fallback 예외.
 * */
class FeignClientFallbackException : BusinessException {
    constructor() : super(ErrorCode.FEIGN_CLIENT_REQUEST_FAIL)
    constructor(message: String) : super(ErrorCode.FEIGN_CLIENT_REQUEST_FAIL, message)
    constructor(errorCode: ErrorCode) : super(errorCode, errorCode.message)
    constructor(errorCode: ErrorCode, message: String) : super(errorCode, message)
}
