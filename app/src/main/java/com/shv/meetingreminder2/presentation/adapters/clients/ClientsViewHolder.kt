package com.shv.meetingreminder2.presentation.adapters.clients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.shv.meetingreminder2.data.extensions.getFullName
import com.shv.meetingreminder2.databinding.ClientItemBinding
import com.shv.meetingreminder2.domain.entity.Client

class ClientsViewHolder(
    private val binding: ClientItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(client: Client, onClientClick: ((Client) -> Unit)?) {
        with(binding) {
            with(client) {
                ivClientPhoto.load(imgUrl)
                tvClientFullName.text = getFullName()
                tvEmail.text = email
            }
            root.setOnClickListener {
                onClientClick?.invoke(client)
            }
        }
    }

    companion object {

        fun from(parent: ViewGroup): ClientsViewHolder {
            val binding = ClientItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return ClientsViewHolder(binding)
        }
    }
}