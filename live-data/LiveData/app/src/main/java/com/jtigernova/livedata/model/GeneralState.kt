package com.jtigernova.livedata.model

data class User(val name: String)

data class GeneralState(val user: User) {

    fun setUserName(name: String): GeneralState {
        return copy(user = user.copy(name = name))
    }
}