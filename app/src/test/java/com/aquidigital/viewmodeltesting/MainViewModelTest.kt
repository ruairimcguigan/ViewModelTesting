package com.aquidigital.viewmodeltesting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.aquidigital.viewmodeltesting.api.ApiResult
import com.aquidigital.viewmodeltesting.api.Repo
import com.aquidigital.viewmodeltesting.api.UserService
import com.aquidigital.viewmodeltesting.view.MainViewModel
import com.nhaarman.mockitokotlin2.any
import io.reactivex.Maybe
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.net.SocketException

class MainViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock lateinit var userService: UserService

    lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainViewModel(userService)
    }

    @Test
    fun `test fetchRepos() onSuccess`() {
        /**
         *
         * 1.   Mock api response
         * 2.   Attach fake observer
         * 3.   Observe mocked response
         * 4.   verify/assert value is present
         * 5.   verify/assert correct status
         *
         */
        // given
        `when`(userService.getRepos(anyString())).thenAnswer {
            return@thenAnswer Maybe.just(anyList<Repo>())
        }

        val observer = mock(Observer::class.java) as Observer<ApiResult<List<Repo>>>
        viewModel.reposLiveData.observeForever(observer)

        // when
        viewModel.fetchRepos(anyString())

        // then
        assertNotNull(viewModel.reposLiveData.value)
        assertEquals(ApiResult.Status.SUCCESS, viewModel.reposLiveData.value?.status)
    }

    @Test
    fun `test fetchRepos onError`() {

        // given
        `when`(userService.getRepos(anyString())).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException("No network"))
        }

        // when
        viewModel.fetchRepos(anyString())

        // then
        assertNotNull(viewModel.reposLiveData.value)
        assertEquals(ApiResult.Status.ERROR, viewModel.reposLiveData.value?.status)
        assert(viewModel.reposLiveData.value?.err is Throwable)
    }

    @Test
    fun `test toggleLoadingView onSuccess`() {
        // given
        `when`(userService.getRepos(anyString())).thenAnswer {
            return@thenAnswer Maybe.just(listOf<Repo>())
        }

        // when
        val spyViewModel = spy(viewModel)

        // invoke
        spyViewModel.fetchRepos(anyString())

        // verify
        verify(spyViewModel, times(2)).toggleLoadingView(anyBoolean())
    }

    @Test
    fun `test loadingView on no data received`() {

        // mock response
        `when`(userService.getRepos(anyString())).thenReturn(Maybe.create {
            it.onComplete()
        })

        // spy on viewmodel
        val spyViewModel = spy(viewModel)

        // invoke
        spyViewModel.fetchRepos(anyString())

        // verify
        verify(spyViewModel, times(2)).toggleLoadingView(anyBoolean())
    }

    @Test
    fun `test loadingView onError`() {
        // mock response
        `when`(userService.getRepos(anyString())).thenAnswer {
            return@thenAnswer Maybe.error<SocketException>(SocketException())
        }

        // spy on viewmodel
        val spyViewModel = spy(viewModel)

        // invoke
        spyViewModel.fetchRepos(anyString())

        // verify
        verify(spyViewModel, times(2)).toggleLoadingView(anyBoolean())
    }
}