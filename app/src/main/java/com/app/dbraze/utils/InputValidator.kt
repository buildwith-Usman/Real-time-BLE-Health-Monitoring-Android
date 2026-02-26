package com.app.dbraze.utils

object InputValidator {

    // Validate if the input is not empty or null (mandatory check)
    fun isMandatory(input: String?): Boolean {
        return !input.isNullOrEmpty()
    }

    // Validate if the input is a valid email
    fun isValidEmail(email: String?, mandatory: Boolean = true): Boolean {
        if (mandatory && !isMandatory(email)) return false
        if (email.isNullOrEmpty()) return false
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS
        return emailPattern.matcher(email).matches()
    }

    // Validate if the input is a valid phone number
    fun isValidPhoneNumber(phone: String?, mandatory: Boolean = true): Boolean {
        if (mandatory && !isMandatory(phone)) return false
        if (phone.isNullOrEmpty()) return false
        val phonePattern = android.util.Patterns.PHONE
        return phonePattern.matcher(phone).matches()
    }

    // Validate if the input meets password requirements (e.g., at least 8 characters)
    fun isValidPassword(password: String?, mandatory: Boolean = true): Boolean {
        if (mandatory && !isMandatory(password)) return false
        return (password?.length ?: 0) >= 8
    }

    // Validate if the input is a valid URL
    fun isValidUrl(url: String?, mandatory: Boolean = true): Boolean {
        if (mandatory && !isMandatory(url)) return false
        if (url.isNullOrEmpty()) return false
        val urlPattern = android.util.Patterns.WEB_URL
        return urlPattern.matcher(url).matches()
    }

    // Validate if the input is numeric
    fun isNumeric(input: String?, mandatory: Boolean = true): Boolean {
        if (mandatory && !isMandatory(input)) return false
        return input?.toDoubleOrNull() != null
    }
}
