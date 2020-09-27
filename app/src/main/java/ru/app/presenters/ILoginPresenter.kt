package ru.app.presenters

interface ILoginPresenter {
    fun doLogin(phone: String, password: String)
    fun setProgressBarVisibility(visibility: Int)
}