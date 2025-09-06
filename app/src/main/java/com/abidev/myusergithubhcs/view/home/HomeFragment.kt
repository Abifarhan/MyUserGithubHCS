package com.abidev.myusergithubhcs.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.abidev.myusergithubhcs.databinding.FragmentHomeBinding
import com.abidev.myusergithubhcs.presenter.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: UserAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        setupRecyclerView()
        setupObserver()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter { user ->
            val action = HomeFragmentDirections.actionHomeFragmentToUserDetailFragment(user.username)
            navController.navigate(action)
        }

        binding.recyclerViewUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(View.getContext)
            adapter = this@HomeFragment.adapter
            addItemDecoration(
                DividerItemDecoration(
                    View.getContext,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> showSuccess(resource.data)
                is Resource.Error -> showError(resource.message)
                else -> showLoading()
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewUsers.visibility = View.GONE
    }

    private fun showSuccess(users: List<UserDomainModel>) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewUsers.visibility = View.VISIBLE
        adapter.submitList(users)
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewUsers.visibility = View.VISIBLE
        Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
        adapter.submitList(emptyList())
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.searchUsers(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}