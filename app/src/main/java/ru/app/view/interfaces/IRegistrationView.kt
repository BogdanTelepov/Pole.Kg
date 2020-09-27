package ru.app.view.interfaces

interface IRegistrationView {
    fun onRegistrationResult(result: Boolean, code: Int, identificationToken: String)
    fun onSetProgressBarVisibility(visibility: Int)
}