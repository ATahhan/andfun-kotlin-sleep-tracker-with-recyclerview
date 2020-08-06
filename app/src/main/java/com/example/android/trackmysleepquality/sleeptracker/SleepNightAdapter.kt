/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.GridItemSleepNightBinding
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

enum class SleepNightAdapterType {
    LIST, GRID;

    val inversed: SleepNightAdapterType
        get() {
            return when(this) {
                GRID -> LIST
                LIST -> GRID
            }
        }

    fun recyclerLayoutManager(context: Context?, spanCount: Int?): RecyclerView.LayoutManager {
        return when(this) {
            GRID -> GridLayoutManager(context, spanCount ?: 3)
            LIST -> LinearLayoutManager(context)
        }
    }
}

class SleepNightAdapter(var type: SleepNightAdapterType, val clickListener: SleepNightListener):
        ListAdapter<SleepNight, SleepNightAdapter.GenericViewHolder>(SleepNightDiffCallback()) {

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        when(type) {
            SleepNightAdapterType.LIST -> return ListViewHolder.from(parent)
            SleepNightAdapterType.GRID -> return GridViewHolder.from(parent)
        }
    }

    abstract class GenericViewHolder(view: View): RecyclerView.ViewHolder(view) {
        abstract fun bind(clickListener: SleepNightListener, item: SleepNight)
    }

    class ListViewHolder private constructor(val binding: ListItemSleepNightBinding): GenericViewHolder(binding.root) {

        override fun bind(clickListener: SleepNightListener, item: SleepNight) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(binding)
            }
        }
    }

    class GridViewHolder private constructor(val binding: GridItemSleepNightBinding): GenericViewHolder(binding.root) {

        override fun bind(clickListener: SleepNightListener, item: SleepNight) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): GridViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return GridViewHolder(binding)
            }
        }
    }

    class SleepNightDiffCallback: DiffUtil.ItemCallback<SleepNight>() {
        override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem.nightId == newItem.nightId
        }

        override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem == newItem
        }
    }

}

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}