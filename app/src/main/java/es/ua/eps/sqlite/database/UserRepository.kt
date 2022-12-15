package es.ua.eps.sqlite.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()

    fun logIn(userName: String, password:String):List<UserEntity>{
       return userDao.logIn(userName, password)
    }

    fun getUser(id: Int):UserEntity?{
        val users = userDao.getUser(id)
        if(users.size == 1){
            return users[0]
        }
        return null
    }

    @WorkerThread
    suspend fun insertUser(userEntity: UserEntity){
        userDao.insertUser(userEntity)
    }

    @WorkerThread
    suspend fun updateUser(userEntity: UserEntity){
        userDao.updateUser(userEntity)
    }

    @WorkerThread
    suspend fun deleteUser(userEntity: UserEntity){
        userDao.deleteUser(userEntity)
    }
}