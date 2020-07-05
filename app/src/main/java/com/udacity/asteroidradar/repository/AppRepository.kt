package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.constant.ApiStatus
import com.udacity.asteroidradar.constant.Constants
import com.udacity.asteroidradar.db.AppDatabase
import com.udacity.asteroidradar.db.toListDomainModel
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AppDatabase) {

    val _api_status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus>
        get() = _api_status

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids(Calendar.getInstance().time.formattedDate())) {
            it.toListDomainModel()
        }

    suspend fun refreshAsteroidDataList() {
        withContext(Dispatchers.IO) {
            try {
                _api_status.postValue(ApiStatus.LOADING)
                val asteroids = NasaApi.endpointScalar.getAsteroidsList(
                        toDateString(Date(), Constants.DEFAULT_START_DATE_DAYS),
                        toDateString(Date(), Constants.DEFAULT_END_DATE_DAYS),
                        Constants.API_KEY
                    ).await()
                _api_status.postValue(ApiStatus.COMPLETED)
                database.asteroidDao.insertAll(parseAsteroidsJsonResult(JSONObject(asteroids)))
            } catch (e: Exception) {
                _api_status.postValue(ApiStatus.ERROR)
            }
        }
    }

    suspend fun getImageOfTheDay(): PictureOfDay? {
        var imageOfDay : PictureOfDay? = null
        withContext(Dispatchers.IO) {

            try {
                _api_status.postValue(ApiStatus.LOADING)
                imageOfDay = NasaApi.endpointMoshi.getPictureOfDay(Constants.API_KEY)
                    .await()
                _api_status.postValue(ApiStatus.COMPLETED)
            } catch (e: Exception) {
                _api_status.postValue(ApiStatus.ERROR)
            }
        }
        return imageOfDay
    }

    private fun Date.formattedDate(): String {
        val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return formatter.format(this)
    }

    private fun toDateString(date: Date, plusDays: Int): String {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, plusDays)

        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

        return dateFormat.format(cal.time)
    }
}