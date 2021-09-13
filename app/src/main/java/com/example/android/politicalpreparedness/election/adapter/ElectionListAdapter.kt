package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ItemElectionBinding
import com.example.android.politicalpreparedness.network.models.Election
import java.text.DateFormat

class ElectionListAdapter(
    private val clickListener: ElectionListener
) : ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ElectionViewHolder private constructor(
        private val binding: ItemElectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(election: Election, clickListener: ElectionListener) {
            binding.textElectionName.text = election.name
            binding.textElectionDay.text = DateFormat.getDateInstance().format(election.electionDay)

            itemView.setOnClickListener { clickListener.onClick(election) }
        }

        companion object {
            fun from(parent: ViewGroup): ElectionViewHolder {
                val binding =
                    ItemElectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ElectionViewHolder(binding)
            }
        }
    }

    companion object ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {

        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem == newItem
        }
    }

    class ElectionListener(val clickListener: (election: Election) -> Unit) {
        fun onClick(election: Election) = clickListener(election)
    }
}
