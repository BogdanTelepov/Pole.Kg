package ru.app.presenters

interface IRegistrationPresenter {
    fun doRegistration(
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String
    )

    fun setProgressBarVisibility(visibility: Int)

}