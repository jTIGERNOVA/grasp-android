package com.jtigernova.designpatterns.mydog.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.jtigernova.designpatterns.Dep
import com.jtigernova.designpatterns.data.model.Dog
import com.jtigernova.designpatterns.data.repo.IAppRepo

class MyDogViewModel(
    savedStateHandle: SavedStateHandle
) : BaseViewModel(savedStateHandle) {
    private val appRepo: IAppRepo = Dep.appRepo(myScope)

    val id: Int = getOrError("id")

    val dog: LiveData<Dog> = appRepo.getDog()
}