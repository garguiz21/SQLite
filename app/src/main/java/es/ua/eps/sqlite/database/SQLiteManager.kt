package es.ua.eps.sqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel


class SQLiteManager(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // WITH execSQL
    // DONE
    fun insertUserExec(userName: String, fullName: String, password: String, email: String): Boolean{
        val existUser = userExist(userName)
        if(existUser == null){
            val db = writableDatabase
            val ADD_USER = StringBuilder().append("INSERT INTO ").append(TABLE_NAME).append(" (")
                .append(USER_NAME_FIELD).append(", ")
                .append(FULL_NAME_FIELD).append(", ")
                .append(PASSWORD_FIELD).append(", ")
                .append(EMAIL_FIELD)
                .append(") ")
                .append("VALUES (")
                .append("'$userName'").append(", ")
                .append("'$fullName'").append(", ")
                .append("'$password'").append(", ")
                .append("'$email'").append(")")

            return try {
                db.execSQL(ADD_USER.toString())
                true
            } catch (error: SQLException) {
                false
            }
        } else {
            return false
        }
    }

    // DONE
    fun updateUserExec(id: Int, userName: String, full_name: String, password: String, email: String): Boolean{
        val db = writableDatabase
        return try {
            db.execSQL("UPDATE $TABLE_NAME " +
                    "SET $USER_NAME_FIELD='$userName', " +
                    "$FULL_NAME_FIELD='$full_name', " +
                    "$PASSWORD_FIELD='$password', " +
                    "$EMAIL_FIELD='$email' " +
                    "WHERE $ID_FIELD='$id'")
            true
        }catch (error: SQLException){
            false
        }
    }

    // DONE
    fun deleteUserExec(id: Int): Boolean{
        val db = writableDatabase
        return try {
            db.execSQL("DELETE FROM $TABLE_NAME WHERE $ID_FIELD='$id'")
            true
        }catch(error: SQLException){
            false
        }
    }

//  Método insert
//  DONE
    fun insertUser(userName: String, fullName: String, password: String, email: String): Boolean{
        val existUser = userExist(userName)
        return if(existUser == null){
            val db = writableDatabase

            val contentValues = ContentValues()

            contentValues.put(USER_NAME_FIELD, userName)
            contentValues.put(FULL_NAME_FIELD, fullName)
            contentValues.put(PASSWORD_FIELD, password)
            contentValues.put(EMAIL_FIELD, email)

            db.insert(TABLE_NAME, null, contentValues) != -1L

        } else {
            false
        }

    }

//  Método update
//  DONE
    fun updateUser(id: Int, userName: String, full_name: String, password: String, email: String): Boolean{
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_NAME_FIELD, userName)
        contentValues.put(FULL_NAME_FIELD, full_name)
        contentValues.put(PASSWORD_FIELD, password)
        contentValues.put(EMAIL_FIELD, email)

        return db.update(TABLE_NAME, contentValues, "$ID_FIELD=$id", null) != 0

    }

//  Método delete
    fun deleteUser(id: Int): Boolean{
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$ID_FIELD=$id", null) != 0
    }

//  WITH rawQuery
//  DONE
    fun getUserRawQuery(id: Int): UserDTO?{
        val db = writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT $ID_FIELD, $USER_NAME_FIELD, $FULL_NAME_FIELD, $PASSWORD_FIELD, $EMAIL_FIELD  FROM $TABLE_NAME WHERE $ID_FIELD='$id'", null)
        var user: UserDTO? = null
        if(cursor.moveToFirst()){
            user = UserDTO(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4))
        }
        cursor.close()
        return user
    }

//  DONE
    fun logIn(userName: String, password: String): UserDTO?{
        val db = writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT $ID_FIELD, $USER_NAME_FIELD, $FULL_NAME_FIELD, $PASSWORD_FIELD, $EMAIL_FIELD FROM $TABLE_NAME WHERE $USER_NAME_FIELD='$userName' AND $PASSWORD_FIELD='$password'", null)
        var user: UserDTO? = null
        if(cursor.moveToFirst()){
            user = UserDTO(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4))
        }
        cursor.close()
        return user
    }

//  DONE
    fun getUsersRawQuery(): ArrayList<UserDTO>{
        val db = writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT $ID_FIELD, $USER_NAME_FIELD, $FULL_NAME_FIELD, $PASSWORD_FIELD, $EMAIL_FIELD  FROM $TABLE_NAME", null)
        val users: ArrayList<UserDTO> = ArrayList()
        if(cursor.moveToFirst()){
            do {
                users.add(UserDTO(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }

//  WITH query
//  DONE
    fun getUserQuery(id: Int): UserDTO?{
        val db = writableDatabase
        val rows = arrayOf<String>(ID_FIELD, USER_NAME_FIELD, FULL_NAME_FIELD, PASSWORD_FIELD, EMAIL_FIELD)
        val cursor: Cursor = db.query(TABLE_NAME, rows, "$ID_FIELD=$id", null, null, null, null)
        var user: UserDTO? = null
        if(cursor.moveToFirst()){
            do {
                user = UserDTO(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return user
    }

//  DONE
    fun getUsersQuery(): ArrayList<UserDTO>{
        val db = writableDatabase
        val rows = arrayOf(ID_FIELD, USER_NAME_FIELD, FULL_NAME_FIELD, PASSWORD_FIELD, EMAIL_FIELD)
        val cursor: Cursor = db.query(TABLE_NAME, rows, null, null, null, null, null )
        var users: ArrayList<UserDTO> = ArrayList()
        if(cursor.moveToFirst()){
            do {
                users.add(UserDTO(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }

//  DONE
    private fun userExist(userName: String): UserDTO?{
        val db = writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT $ID_FIELD, $USER_NAME_FIELD, $FULL_NAME_FIELD, $PASSWORD_FIELD, $EMAIL_FIELD FROM $TABLE_NAME WHERE $USER_NAME_FIELD='$userName'", null)
        var user: UserDTO? = null
        if(cursor.moveToFirst()){
            user = UserDTO(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4))
        }
        cursor.close()
        return user
    }

    fun backUp():Boolean {
        try {
            val sd: File = Environment.getExternalStorageDirectory()
            if (sd.canWrite()) {
                val currentDBPath = writableDatabase.path
                val backupDBPath = DATABASE_NAME

                val currentDB = File(currentDBPath)
                val backupDB = File(sd, backupDBPath)
                if (currentDB.exists()) {
                    val src: FileChannel = FileInputStream(currentDB).channel
                    val dst: FileChannel = FileOutputStream(backupDB).channel
                    dst.transferFrom(src, 0, src.size())
                    src.close()
                    dst.close()
                    return true
                }
                return false
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    fun restore():Boolean {
        try {
            val sd = Environment.getExternalStorageDirectory()
            if (sd.canWrite()) {
                val currentDBPath =  writableDatabase.path
                val backupDBPath = DATABASE_NAME
                val currentDB = File(currentDBPath)
                val backupDB = File(sd, backupDBPath)
                if (currentDB.exists()) {
                    val src = FileInputStream(backupDB).channel
                    val dst = FileOutputStream(currentDB).channel
                    dst.transferFrom(src, 0, src.size())
                    src.close()
                    dst.close()
                    return true
                }
            }
            return false
        } catch (e: java.lang.Exception) {
            return false
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USERS_TABLE = StringBuilder().append("CREATE TABLE  ").append(TABLE_NAME).append(" (")
                                .append(ID_FIELD).append("  INTEGER PRIMARY KEY AUTOINCREMENT, ")
                                .append(USER_NAME_FIELD).append("  TEXT, ")
                                .append(FULL_NAME_FIELD).append("  TEXT, ")
                                .append(PASSWORD_FIELD).append("  TEXT, ")
                                .append(EMAIL_FIELD).append("  TEXT")
                                .append(")")

        db?.execSQL(CREATE_USERS_TABLE.toString())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    companion object {

        private var sqLiteManager: SQLiteManager? = null

        @Synchronized
        fun getInstance(context: Context): SQLiteManager {
            if(sqLiteManager == null) {
                sqLiteManager = SQLiteManager(context)
            }
            return sqLiteManager as SQLiteManager
        }

        private val DATABASE_NAME = "userdb"
        private val DATABASE_VERSION = 1
        private val TABLE_NAME = "users"
        private val COUNTER = "counter"

        private val ID_FIELD = "id"
        private val USER_NAME_FIELD = "un"
        private val FULL_NAME_FIELD = "fn"
        private val PASSWORD_FIELD = "p"
        private val EMAIL_FIELD = "e"
    }
}