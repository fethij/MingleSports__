package com.example.minglesports.domain.use_case.get_airports

import com.example.minglesports.R
import com.example.minglesports.common.Resource
import com.example.minglesports.domain.model.Airport
import com.example.minglesports.domain.repository.AirportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAirportUseCase @Inject constructor(
    private val repository: AirportRepository
) {
    operator fun invoke(): Flow<Resource<List<Airport>>> = flow {
        try {
            emit(Resource.Loading<List<Airport>>())
//            val airports1 = repository.getAirports().map { it. }
        //    val airports = repository.getAirports().map { it.toAirport() }
           val airports = repository.getAirports()
            emit(Resource.Success<List<Airport>>(airports))
        } catch (e: HttpException){
            emit(Resource.Error<List<Airport>>((e.localizedMessage ?: R.string.httpException).toString()))
        }catch (e: IOException){
            emit(Resource.Error<List<Airport>>(R.string.ioException.toString()))
        }
    }
}