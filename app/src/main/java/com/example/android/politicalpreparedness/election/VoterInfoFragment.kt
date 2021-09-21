package com.example.android.politicalpreparedness.election

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.DateFormat

class VoterInfoFragment : Fragment() {

    private val args: VoterInfoFragmentArgs by navArgs()
    private lateinit var binding: FragmentVoterInfoBinding
    private val viewModel: VoterInfoViewModel by viewModel()

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

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            context?.apply {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.error))
                    .setMessage(it)
                    .setNeutralButton(R.string.ok) { _, _ -> }
                    .setOnDismissListener { findNavController().popBackStack() }
                    .create()
                    .show()
            }
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

        val division = args.argDivision
        if (division.state.isEmpty()) {
            viewModel.getVoterInfo(args.argElectionId, division.country)
        } else {
            viewModel.getVoterInfo(args.argElectionId, "${division.country} - ${division.state}")
        }

        viewModel.isSavedElection.observe(viewLifecycleOwner, Observer { isSavedElection ->
            if (isSavedElection) {
                binding.buttonFollow.text = getString(R.string.unfollow_election)
            } else {
                binding.buttonFollow.text = getString(R.string.follow_election)
            }
        })

        viewModel.url.observe(viewLifecycleOwner, Observer {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        })

        return binding.root
    }
}
