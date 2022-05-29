package com.example.sample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val repo: UserRepository) : ViewModel() {
    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(listOf())
    val users: StateFlow<List<User>> = _users

    private val _usersSortedByFirstName: MutableStateFlow<List<User>> = MutableStateFlow(listOf())
    val usersSortedByFirstName: StateFlow<List<User>> = _usersSortedByFirstName

    private val _usersSortedByLastName: MutableStateFlow<List<User>> = MutableStateFlow(listOf())
    val usersSortedByLastName: StateFlow<List<User>> = _usersSortedByLastName

    private val _usersSortedByAge: MutableStateFlow<List<User>> = MutableStateFlow(listOf())
    val usersSortedByAge: StateFlow<List<User>> = _usersSortedByAge

    init {
        viewModelScope.launch {
            repo.fetchData()
            repo.users.collect { data ->
                _users.value = data
                _usersSortedByFirstName.value = data.sortedBy { it.firstName }
                _usersSortedByLastName.value = data.sortedBy { it.lastName }
                _usersSortedByAge.value = data.sortedBy { it.age }
            }
        }
    }
}