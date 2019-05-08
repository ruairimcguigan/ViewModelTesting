package com.aquidigital.viewmodeltesting.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aquidigital.App
import com.aquidigital.viewmodeltesting.R
import com.aquidigital.viewmodeltesting.api.ApiResult
import com.aquidigital.viewmodeltesting.api.ApiResult.*
import com.aquidigital.viewmodeltesting.api.Repo
import com.aquidigital.viewmodeltesting.api.UserService
import com.aquidigital.viewmodeltesting.viewmodel.ViewModelFactory

class MainView: Fragment() {

    companion object {
        fun newInstance() = MainView()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // ViewModel factory
        val factory = ViewModelFactory((activity?.application as App).retrofit.create(UserService::class.java))

        // Create ViewModel and bind observer
        observeViewModel(factory)
    }

    private fun observeViewModel(factory: ViewModelFactory) {
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
        viewModel.reposLiveData.observe(this, this.dataObserver)
        viewModel.loadingLiveData.observe(this, this.loadingObserver)
    }

    private val dataObserver = Observer<ApiResult<List<Repo>>> { result ->
        when (result?.status) {
            Status.LOADING -> {
                // Loading data
            }

            Status.ERROR -> {
                // Error for http request
            }

            Status.SUCCESS -> {
                // Data from API
            }
        }
    }

    private val loadingObserver = Observer<Boolean> { visible ->
        // Show hide a progress
    }

}