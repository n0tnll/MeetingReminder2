package com.shv.meetingreminder2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shv.meetingreminder2.databinding.FragmentClientsListBinding
import com.shv.meetingreminder2.presentation.adapters.clients.ClientsAdapter

class ClientsListFragment : Fragment() {

    private var _binding: FragmentClientsListBinding? = null
    private val binding: FragmentClientsListBinding
        get() = _binding ?: throw RuntimeException("FragmentClientsListBinding is null")

    private val adapter by lazy {
        ClientsAdapter()
    }

//    private val viewModel: LoadClientsViewModel by lazy {
//        ViewModelProvider(this)[LoadClientsViewModel::class.java]
//    }

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
        super.onViewCreated(view, savedInstanceState)
        binding.rvClientsList.adapter = adapter
//
//        observeViewModel()
    }

//    private fun observeViewModel() {
//        viewModel.clients.observe(viewLifecycleOwner) {
//            adapter.submitList(it)
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        fun newInstance(): ClientsListFragment {
            return ClientsListFragment()
        }
    }
}