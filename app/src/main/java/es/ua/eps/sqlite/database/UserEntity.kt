package es.ua.eps.sqlite.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity (
    @ColumnInfo(name = "u") val userName: String,
    @ColumnInfo(name = "fn") val fullName: String,
    @ColumnInfo(name = "p") val password: String,
    @ColumnInfo(name = "e") val email: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    override fun toString(): String {
        return userName
    }
}