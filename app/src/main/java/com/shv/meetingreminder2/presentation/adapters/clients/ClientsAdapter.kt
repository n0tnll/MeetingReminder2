package com.shv.meetingreminder2.presentation.adapters.clients

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shv.meetingreminder2.domain.entity.Client

class ClientsAdapter : RecyclerView.Adapter<ClientsViewHolder>() {

    private val clientsList = mutableListOf<Client>()

    var onClientClickListener: ((Client) -> Unit)? = null

    fun addClients(newClients: List<Client>) {
        val startPosition = clientsList.size
        clientsList.addAll(newClients)
        notifyItemRangeInserted(startPosition, clientsList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientsViewHolder {
        return ClientsViewHolder.from(parent)
    }

    override fun getItemCount() = clientsList.size

    override fun onBindViewHolder(holder: ClientsViewHolder, position: Int) {
        val client = clientsList[position]
        holder.bind(client, onClientClickListener)
    }
}