package com.example.task81

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MyViewModel : ViewModel() {

    val heroesLiveData = MutableLiveData<ArrayList<Hero>>()
    private lateinit var job: Job

    fun onCreate(context: Context) {
        job = viewModelScope.launch(Dispatchers.IO) {
            heroesLiveData.postValue(Repository(context).getAllHeroes())
        }
    }

    fun onUpdate(context: Context) {
        job = viewModelScope.launch(Dispatchers.IO) {
            heroesLiveData.postValue(Repository(context).requestToApi())
        }
    }
}