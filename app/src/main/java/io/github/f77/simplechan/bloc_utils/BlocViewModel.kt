package io.github.f77.simplechan.bloc_utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.f77.simplechan.bloc_utils.action.ActionsLiveData
import io.github.f77.simplechan.bloc_utils.action.interfaces.ActionInterface
import io.github.f77.simplechan.bloc_utils.event.EventInterface
import io.github.f77.simplechan.bloc_utils.state.StateInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BlocViewModel : ViewModel(), BlocViewModelInterface {
    private val _dispatcher: CoroutineDispatcher = Dispatchers.IO
    override val state: MutableLiveData<StateInterface> = MutableLiveData()
    override val actions: ActionsLiveData<ActionInterface> = ActionsLiveData()

    /**
     * Directly handling all incoming events.
     * For example, for logging purposes.
     */
    override suspend fun onEvent(event: EventInterface) {}

    /**
     * Add event to the ViewModel.
     * Each event runs in it's own coroutine in the scope of current ViewModel.
     * So, if the model suddenly dies, all running processing of the events will safely stop.
     */
    override fun addEvent(event: EventInterface) {
        viewModelScope.launch(_dispatcher) {
            onEvent(event)

            // Handle actions.
            mapEventToAction(event).collect {
                actions.postValue(it)
            }

            // Handle states.
            mapEventToState(event).collect {
                state.postValue(it)
            }
        }
    }
}
