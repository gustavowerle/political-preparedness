package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ElectionsViewModel(
    private val datasource: ElectionRepository
) : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _elections = MutableLiveData<List<Election>>()
    val elections: LiveData<List<Election>> get() = _elections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>> get() = _savedElections

    lateinit var selectedElection: Election

    private val _navigateToElectionVoterInfo = MutableLiveData(false)
    val navigateToElectionVoterInfo: LiveData<Boolean> get() = _navigateToElectionVoterInfo

    init {
        getUpcomingElections()
        getSavedElections()
    }

    private fun getUpcomingElections() {
        viewModelScope.launch {
            with(Dispatchers.IO) {
                val result: List<Election> = try {
                    datasource.getElections()
                } catch (e: HttpException) {
                    _message.postValue("Sorry, something went wrong, please try again")
                    emptyList()
                }
                _elections.postValue(result)
            }
        }
    }

    private fun getSavedElections() {
        TODO("Not yet implemented")
    }

    fun navigateToElectionVoterInfo(election: Election) {
        selectedElection = election
        _navigateToElectionVoterInfo.value = true
    }

    fun setNavigateToElectionVoterInfoComplete() {
        _navigateToElectionVoterInfo.value = false
    }
}
