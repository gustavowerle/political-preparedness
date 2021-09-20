package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.android.ext.android.inject
import java.text.DateFormat

class VoterInfoFragment : Fragment() {

    private val args: VoterInfoFragmentArgs by navArgs()
    private lateinit var binding: FragmentVoterInfoBinding
    private val viewModel: VoterInfoViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVoterInfoBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        viewModel.voterInfo.observe(viewLifecycleOwner, Observer {
            binding.textToolbarTitle.text = it.election.name
            binding.electionDate.text = DateFormat.getDateInstance().format(it.election.electionDay)

        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.buttonFollow.isEnabled = false
                binding.buttonFollow.alpha = 0.7f
            } else {
                binding.progressBar.visibility = View.GONE
                binding.buttonFollow.isEnabled = true
                binding.buttonFollow.alpha = 1f
            }
        })

        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */
        viewModel.getVoterInfo(args.argElectionId, args.argDivision.country)

        //TODO: Handle loading of URLs

        viewModel.isSavedElection.observe(viewLifecycleOwner, Observer { isSavedElection ->
            if (isSavedElection) {
                binding.buttonFollow.text = getString(R.string.unfollow_election)
            } else {
                binding.buttonFollow.text = getString(R.string.follow_election)
            }
        })

        return binding.root
    }

    //TODO: Create method to load URL intents

}