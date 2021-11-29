package com.example.minglesports.domain.use_case.get_flights

import com.example.minglesports.R
import com.example.minglesports.common.Resource
import com.example.minglesports.domain.model.Flight
import com.example.minglesports.domain.repository.FlightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetFlightUseCase @Inject constructor(
    private val repository: FlightRepository
) {
    operator fun invoke(): Flow<Resource<List<Flight>>> = flow {
        try {
            emit(Resource.Loading<List<Flight>>())
            val flights = repository.
            getFlights()
            emit(Resource.Success<List<Flight>>(flights))
        } catch (e: HttpException){
            emit(Resource.Error<List<Flight>>((e.localizedMessage ?: R.string.httpException).toString()))
        }catch (e: IOException){
            emit(Resource.Error<List<Flight>>(R.string.ioException.toString()))
        }
    }
}