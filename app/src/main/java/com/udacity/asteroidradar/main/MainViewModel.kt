package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.db.getDatabase
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(app: Application) : AndroidViewModel(app) {

    private var jobViewModel = Job()
    private val coroutineScope = CoroutineScope(jobViewModel + Dispatchers.Main)
    private val dbAsteroids = getDatabase(app)
    private val asteroidRepository = AsteroidRepository(dbAsteroids)
    val asteroidsLiveData = asteroidRepository.asteroids
    val status = asteroidRepository.status

    private val _picOfTheDay = MutableLiveData<PictureOfDay>()
    val picOfTheDayLiveData: LiveData<PictureOfDay>
        get() = _picOfTheDay

    init {
        coroutineScope.launch {
            _picOfTheDay.value = asteroidRepository.getImageOfTheDay()
            asteroidRepository.refreshAsteroidDataList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }
}