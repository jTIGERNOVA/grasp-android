package com.jtigernova.livedata.model

data class User(val name: String?, val gender: String?)

data class GeneralState(val user: User) {

    fun setUserName(name: String): GeneralState {
        return copy(user = user.copy(name = name))
    }

    fun setGender(gender: String?): GeneralState {
        return copy(user = user.copy(gender = gender))
    }
}