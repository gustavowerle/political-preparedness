package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.election.ElectionRepository
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RepresentativeViewModel(
    private val dataSource: ElectionRepository
) : ViewModel() {

    val address: Address = Address("", "", "", "", "")

    private val _representatives = MutableLiveData<List<Representative>>()
    val representative: LiveData<List<Representative>> get() = _representatives

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    fun searchRepresentatives() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (isValidAddress()) {
                    try {
                        val (offices, officials) = dataSource.getRepresentatives(address)
                        _representatives.postValue(offices.flatMap { office ->
                            office.getRepresentatives(officials)
                        })
                    } catch (e: HttpException) {
                        _message.postValue("No representative found")
                    }
                }
            }
        }
    }

    private fun isValidAddress(): Boolean {
        return when {
            address.line1.trim().isEmpty() -> {
                _message.postValue("Fill the address line 1")
                false
            }
            address.city.trim().isEmpty() -> {
                _message.postValue("Fill the city")
                false
            }
            address.zip.trim().isEmpty() -> {
                _message.postValue("Fill the zip")
                false
            }
            else -> true
        }
    }

    //TODO: Create function get address from geo location

}
