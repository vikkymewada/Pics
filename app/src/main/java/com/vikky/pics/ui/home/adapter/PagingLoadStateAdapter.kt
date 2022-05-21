package com.vikky.pics.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.vikky.pics.R

class PagingLoadStateAdapter<T : Any, VH : RecyclerView.ViewHolder>(private val adapter: PagingDataAdapter<T, VH>) :
    LoadStateAdapter<PagingLoadStateAdapter.NetworkStateItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        NetworkStateItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_network_state, parent, false)
        ) {
            adapter.retry()
        }

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.retryBtn.isVisible = loadState is LoadState.Error
    }

    class NetworkStateItemViewHolder(v: View, private val retry: () -> Unit) :
        RecyclerView.ViewHolder(v) {
        val retryBtn: MaterialButton

        init {
            retryBtn = v.findViewById<View>(R.id.retry_button) as MaterialButton
            retryBtn.setOnClickListener { retry() }
        }
    }
}