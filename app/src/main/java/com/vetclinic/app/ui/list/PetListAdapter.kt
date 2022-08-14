package com.vetclinic.app.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vetclinic.app.R
import com.vetclinic.app.common.fetchimage.FetchImage
import com.vetclinic.app.databinding.RecyclerviewPetItemBinding
import com.vetclinic.app.domain.PetDomain

class PetListAdapter(
    private val fetchImage: FetchImage,
    private val onItemClicked: (pet: PetDomain) -> Unit
) : ListAdapter<PetDomain, PetListAdapter.PetViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        return PetViewHolder(
            RecyclerviewPetItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: PetViewHolder) {
        fetchImage.cancel(holder.binding.petImage)
    }

    inner class PetViewHolder(
        val binding: RecyclerviewPetItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PetDomain) {
            binding.petImage.setImageResource(R.drawable.ic_launcher_foreground)
            fetchImage.into(binding.petImage, item.imageUrl)
            binding.petInfo.text = item.title
            binding.root.setOnClickListener { onItemClicked.invoke(item) }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PetDomain>() {
            override fun areItemsTheSame(oldItem: PetDomain, newItem: PetDomain): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: PetDomain, newItem: PetDomain): Boolean {
                return oldItem == newItem
            }
        }
    }
}