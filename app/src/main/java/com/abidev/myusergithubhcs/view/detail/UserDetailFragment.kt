package com.abidev.myusergithubhcs.view.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.abidev.core.Resource
import com.abidev.domain.home.usecase.UserDomainModel
import com.abidev.myusergithubhcs.databinding.FragmentUserDetailBinding
import com.abidev.myusergithubhcs.presenter.detail.UserDetailViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserDetailViewModel by viewModels()
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString("username") ?: return

        viewModel.uiState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> showSuccess(resource.data)
                is Resource.Error -> showError(resource.message)
            }
        }

        viewModel.getUserDetail(username)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showSuccess(user: UserDomainModel) {
        binding.progressBar.visibility = View.GONE

        binding.textUsername.text = user.username
        binding.textName.text = user.name ?: "Tidak ada nama"
        binding.textBio.text = user.bio ?: "Tidak ada bio"
        binding.textPublicRepos.text = user.publicRepos.toString()
        binding.textFollowers.text = user.followers.toString()
        binding.textFollowing.text = user.following.toString()

        Glide.with(binding.imageAvatar)
            .load(user.avatarUrl)
            .circleCrop()
            .into(binding.imageAvatar)
    }

    private fun showError(message: String) {
        Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}