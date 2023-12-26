package com.shv.meetingreminder2.domain.usecases.validation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
