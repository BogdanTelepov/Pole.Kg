package ru.app.responseModels

data class LoginResponseModel(
    var id: Int = 0,
    var firstName: String,
    var lastName: String,
    var sessionId: String,
    var type: String,
    var validated: Boolean,
    var phoneNumber: String,
    var email: String,
    var identificationToken: String
)