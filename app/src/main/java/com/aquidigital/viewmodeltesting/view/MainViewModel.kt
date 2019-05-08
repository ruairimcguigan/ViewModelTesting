package com.aquidigital.viewmodeltesting.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aquidigital.viewmodeltesting.api.ApiResult
import com.aquidigital.viewmodeltesting.api.Repo
import com.aquidigital.viewmodeltesting.api.UserService
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable

class MainViewModel(private val userService: UserService): ViewModel() {

    val reposLiveData = MutableLiveData<ApiResult<List<Repo>>>()
    val loadingLiveData = MutableLiveData<Boolean>()

    fun fetchRepos(userName:String) {
        toggleLoadingView(true)
        userService.getRepos(userName).subscribe(RepositoryConsumer())
    }

    fun toggleLoadingView(isVisible: Boolean) {
        loadingLiveData.value = isVisible
    }


    inner class RepositoryConsumer : MaybeObserver<List<Repo>> {

        override fun onSubscribe(d: Disposable) {
            reposLiveData.postValue(ApiResult.loading())
        }

        override fun onSuccess(t: List<Repo>) {
            reposLiveData.postValue(ApiResult.success(t))
            toggleLoadingView(false)
        }

        override fun onError(e: Throwable) {
            toggleLoadingView(false)
            reposLiveData.postValue(ApiResult.error(e))
        }

        override fun onComplete() {
            toggleLoadingView(false)
        }
    }
}