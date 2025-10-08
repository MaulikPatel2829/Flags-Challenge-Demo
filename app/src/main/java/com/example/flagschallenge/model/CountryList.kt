package com.example.flagschallenge.model

data class CountryList(
    val questions: MutableList<Question> = arrayListOf()
) {
    data class Question(
        val answer_id: Int = 0,
        val countries: MutableList<Country> = arrayListOf(),
        val country_code: String = ""
    )

    data class Country(
        val country_name: String = "",
        val id: Int = 0,
        var userSelect: Boolean = false,
        var correctAnswer: Boolean = false
    )

}