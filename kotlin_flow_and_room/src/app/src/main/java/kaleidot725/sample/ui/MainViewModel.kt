package kaleidot725.sample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kaleidot725.sample.data.User
import kaleidot725.sample.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repo: UserRepository): ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.tryUpdateRecentUsersCache()
        }
    }

    val users: LiveData<List<User>> = repo.getUsers().asLiveData()
    val usersSortedByFirstName: LiveData<List<User>> = repo.getUserSortedByFirstName().asLiveData()
    val usersSortedByLastName: LiveData<List<User>> = repo.getUserSortedByLastName().asLiveData()
    val usersSortedByAge: LiveData<List<User>> = repo.getUserSortedByAge().asLiveData()
}