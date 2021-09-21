package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by viewModel()
    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)
        binding.viewModel = viewModel

        viewModel.message.observe(viewLifecycleOwner, Observer {
            context?.apply {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.navigateToElectionVoterInfo.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        viewModel.selectedElection.id,
                        viewModel.selectedElection.division
                    )
                )
                viewModel.setNavigateToElectionVoterInfoComplete()
            }
        })

        val clickListener = ElectionListAdapter.ElectionListener {
            viewModel.navigateToElectionVoterInfo(it)
        }

        val electionsAdapter = ElectionListAdapter(clickListener)
        binding.recyclerUpcomingElections.adapter = electionsAdapter
        binding.recyclerUpcomingElections.layoutManager = LinearLayoutManager(context)
        viewModel.elections.observe(viewLifecycleOwner, Observer {
            electionsAdapter.submitList(it)
        })

        val savedElectionsAdapter = ElectionListAdapter(clickListener)
        binding.recyclerSavedElections.adapter = savedElectionsAdapter
        binding.recyclerSavedElections.layoutManager = LinearLayoutManager(context)
        viewModel.savedElections.observe(viewLifecycleOwner, Observer {
            savedElectionsAdapter.submitList(it)
        })

        return binding.root
    }
}
