package es.ua.eps.sqlite.database

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository): ViewModel() {
    val users: LiveData<List<UserEntity>> = repository.allUsers.asLiveData()

    fun logIn(userName: String, password: String): UserEntity? {
      val user = repository.logIn(userName, password)
      if(user.size == 1) return user[0]
      return null
    }

    fun getUser(id: Int): UserEntity? {
        return repository.getUser(id)
    }


    fun insertUser(userEntity: UserEntity) = viewModelScope.launch {
        repository.insertUser(userEntity)
    }

    fun updateUser(userEntity: UserEntity) = viewModelScope.launch {
        repository.updateUser(userEntity)
    }

    fun deleteUser(userEntity: UserEntity) = viewModelScope.launch {
        repository.deleteUser(userEntity)
    }

}

class UserModelFactory(private val repository: UserRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java))
            return UserViewModel(repository) as T

        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}