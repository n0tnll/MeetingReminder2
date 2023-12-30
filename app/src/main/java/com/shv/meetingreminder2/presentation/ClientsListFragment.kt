package com.shv.meetingreminder2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shv.meetingreminder2.MeetingReminderApplication
import com.shv.meetingreminder2.databinding.FragmentClientsListBinding
import com.shv.meetingreminder2.domain.entity.Client
import com.shv.meetingreminder2.presentation.adapters.clients.ClientsAdapter
import com.shv.meetingreminder2.presentation.viewmodels.ViewModelFactory
import com.shv.meetingreminder2.presentation.viewmodels.clients.ClientsList
import com.shv.meetingreminder2.presentation.viewmodels.clients.ClientsViewModel
import com.shv.meetingreminder2.presentation.viewmodels.clients.Loading
import com.shv.meetingreminder2.presentation.viewmodels.clients.LoadingError
import javax.inject.Inject

class ClientsListFragment : Fragment() {

    private var _binding: FragmentClientsListBinding? = null
    private val binding: FragmentClientsListBinding
        get() = _binding ?: throw RuntimeException("FragmentClientsListBinding is null")

    private val adapter by lazy {
        ClientsAdapter()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as MeetingReminderApplication).component
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ClientsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientsListBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        component.inject(this)
        super.onViewCreated(view, savedInstanceState)

        binding.rvClientsList.adapter = adapter
        adapter.onClientClickListener = {
            launchAddReminderFragment(it)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) {
            with(binding) {
                pbClientLoading.isVisible = false
                buttonRetryLoading.visibility = View.GONE
                when (it) {
                    is ClientsList -> {
                        adapter.addClients(it.clientsList)
                    }

                    Loading -> {
                        pbClientLoading.isVisible = true
                    }

                    is LoadingError -> {
                        pbClientLoading.isVisible = false
                        buttonRetryLoading.visibility = View.VISIBLE

                        Toast.makeText(
                            context,
                            it.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()

                        buttonRetryLoading.setOnClickListener {
                            viewModel.loadClients()
                        }
                    }
                }
            }
        }
    }

    private fun launchAddReminderFragment(client: Client) {
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(AddReminderFragment.RESULT_CLIENT, client)
        findNavController().navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}