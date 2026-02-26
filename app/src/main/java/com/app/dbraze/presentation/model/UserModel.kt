package com.app.dbraze.presentation.model

data class UserModel(
    val fullName: String? = null,
    val email: String,
    val gender: String? = null,
    val height: Float? = null,
    val weight: Float? = null,
    val dateOfBirth: String? = null,
    val password: String
){
    override fun toString(): String {
        return "UserModel(fullName=$fullName, email='$email', gender=$gender, height=$height, weight=$weight, dateOfBirth=$dateOfBirth, password='********')"
    }

}

fun UserModel.toMap(includeSensitiveData: Boolean = false): Map<String, Any> {
    val userMap = mutableMapOf<String, Any>(
        "email" to email,
        "password" to password
    )

    fullName?.let { userMap["fullName"] = it }
    gender?.let { userMap["gender"] = it }
    height?.let { userMap["height"] = it }
    weight?.let { userMap["weight"] = it }
    dateOfBirth?.let { userMap["dateOfBirth"] = it }

    if (includeSensitiveData) {
        userMap["password"] = password
    }

    return userMap
}
