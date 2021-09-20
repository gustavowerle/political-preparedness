package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(
    private val repository: ElectionRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse> get() = _voterInfo

    private val _isSavedElection = MutableLiveData<Boolean>(false)
    val isSavedElection: LiveData<Boolean> get() = _isSavedElection

    fun getVoterInfo(id: Int, address: String) {
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response = repository.getVoterInfo(id, address)
                _voterInfo.postValue(response)
                checkElectionIsSaved(response.election.id)
                _isLoading.postValue(false)
            }
        }
    }

    private suspend fun checkElectionIsSaved(id: Int) {
        _isSavedElection.postValue(repository.isSavedElection(id))
    }

    fun handleSaveButtonClick() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                voterInfo.value?.let {
                    if (isSavedElection.value!!) {
                        repository.removeSavedElection(it.election)
                        Log.i(TAG, "Election unfollowed")
                    } else {
                        repository.saveElection(it.election)
                        Log.i(TAG, "Election followed")
                    }
                    checkElectionIsSaved(it.election.id)
                }
            }
        }
    }

    companion object {
        const val TAG = "VoterInfoViewModel"
    }

    //TODO: Add var and methods to support loading URLs

}
