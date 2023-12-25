package com.shv.meetingreminder2.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.shv.meetingreminder2.databinding.FragmentAddReminderBinding

class AddReminderFragment : Fragment() {

    private var _binding: FragmentAddReminderBinding? = null
    private val binding: FragmentAddReminderBinding
        get() = _binding ?: throw RuntimeException("FragmentAddReminderBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddReminderBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etClient.setOnClickListener {
            observeChosenClient()
            findNavController().navigate(
                AddReminderFragmentDirections.actionAddReminderFragmentToClientsListFragment()
            )
        }
    }

    private fun observeChosenClient() {
        val currentBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<String>(RESULT_CLIENT)
            ?.observe(currentBackStackEntry, Observer {
                Log.d("CHOSEN_CLIENT", it)
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val RESULT_CLIENT = "chosen_client"
    }
}