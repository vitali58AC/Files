package com.example.files.custom_content_provider

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.net.toUri
import com.example.files.data.Course
import com.example.files.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomContentRepository(private val context: Context) {

    private val uriUser = USER_URI.toUri()
    private val uriCourse = COURSE_URI.toUri()
    private val userResolver = context.contentResolver.acquireContentProviderClient(uriUser)
    private val courseResolver = context.contentResolver.acquireContentProviderClient(uriCourse)

    private fun showNullResolverToast() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                Toast.makeText(
                    context,
                    "There is no \"Content Provider\" application to find a list of users and courses.",
                    Toast.LENGTH_LONG
                ).show()
            }, 0
        )
    }

    suspend fun saveDefaultUsers() = withContext(Dispatchers.IO) {
        ContentValues().apply {
            if (userResolver == null) {
                showNullResolverToast()
            } else {
                User.defaultUsers.map {
                    put(COLUMN_USER_ID, it.id)
                    put(COLUMN_USER_NAME, it.name)
                    put(COLUMN_USER_AGE, it.age)
                    userResolver.insert(
                        uriUser,
                        this
                    )
                }
            }
        }
    }

    suspend fun saveDefaultCourses() = withContext(Dispatchers.IO) {
        ContentValues().apply {
            Course.defaultCourses.map {
                put(COLUMN_COURSE_ID, it.id)
                put(COLUMN_COURSE_TITLE, it.title)
                courseResolver?.insert(
                    uriCourse,
                    this
                )
            }
        }
    }

    suspend fun getAllUser() = withContext(Dispatchers.IO) {
        userResolver?.query(
            uriUser,
            null,
            null,
            null,
            null
        )?.use {
            getUserFromCursor(it)
        }.orEmpty()
    }

    private fun getUserFromCursor(cursor: Cursor): List<User> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<User>()
        do {
            val nameIndex = cursor.getColumnIndex(COLUMN_USER_NAME)
            val name = cursor.getString(nameIndex).orEmpty()
            val idIndex = cursor.getColumnIndex(COLUMN_USER_ID)
            val id = cursor.getLong(idIndex)
            val ageIndex = cursor.getColumnIndex(COLUMN_USER_AGE)
            val age = cursor.getInt(ageIndex)
            list.add(User(id, name, age))
        } while (cursor.moveToNext())
        return list
    }

    suspend fun getAllCourses() = withContext(Dispatchers.IO) {
        courseResolver?.query(
            uriCourse,
            null,
            null,
            null,
            null
        )?.use {
            getCoursesFromCursor(it)
        }.orEmpty()
    }

    private fun getCoursesFromCursor(cursor: Cursor): List<Course> {
        if (cursor.moveToFirst().not()) return emptyList()
        val list = mutableListOf<Course>()
        do {
            val titleIndex = cursor.getColumnIndex(COLUMN_COURSE_TITLE)
            val title = cursor.getString(titleIndex).orEmpty()
            val idIndex = cursor.getColumnIndex(COLUMN_COURSE_ID)
            val id = cursor.getLong(idIndex)
            list.add(Course(id, title))
        } while (cursor.moveToNext())
        return list
    }

    fun getUserFromId(id: Long): Long {
        return userResolver?.query(
            //ContentProvider.USER_URI_SELECT.toUri(),
            uriUser,
            null,
            //Это не работает, потому что нет реализации
            "$COLUMN_USER_ID = ?",
            arrayOf(id.toString()),
            null
        )?.use {
            getIdFromCursor(it, id)
        } ?: -1
    }

    private fun getIdFromCursor(cursor: Cursor, targetId: Long): Long {
        val error = -1L
        if (cursor.moveToFirst().not()) return error
        do {
            val idIndex = cursor.getColumnIndex(COLUMN_USER_ID)
            val userId = cursor.getString(idIndex)
            if (userId.toLong() == targetId) return userId.toLong()
        } while (cursor.moveToNext())
        return error
    }

    suspend fun saveOneUser(user: User) = withContext(Dispatchers.IO) {
        ContentValues().apply {
            if (userResolver == null) {
                showNullResolverToast()
            } else {
                put(COLUMN_USER_ID, user.id)
                put(COLUMN_USER_NAME, user.name)
                put(COLUMN_USER_AGE, user.age)
                userResolver.insert(
                    uriUser,
                    this
                )
            }
        }
    }

    suspend fun deleteUserFromId(id: Long) = withContext(Dispatchers.IO) {
        val resultUri = ContentUris.withAppendedId(USER_URI_PASS.toUri(), id)
        userResolver?.delete(
            resultUri,
            null,
            null
        )
            ?: showNullResolverToast()
    }


    suspend fun updateUserFromId(id: Long, name: String, age: Int) = withContext(Dispatchers.IO) {
        val resultUri = ContentUris.withAppendedId(USER_URI_PASS.toUri(), id)
        val contentValues = ContentValues().apply {
            put(COLUMN_USER_ID, id)
            put(COLUMN_USER_NAME, name)
            put(COLUMN_USER_AGE, age)
        }
        userResolver?.update(
            resultUri,
            contentValues,
            null,
            null
        )
            ?: showNullResolverToast()
    }

    @Suppress("DEPRECATION")
    fun closeContentProviderClient() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            userResolver?.close()
            courseResolver?.close()
        } else {
            userResolver?.release()
            courseResolver?.release()
        }
    }

    companion object {
        private const val AUTHORITIES = "com.example.contentprovider.provider"
        private const val PATH_USERS = "users"
        private const val PATH_COURSES = "courses"

        const val USER_URI = "content://$AUTHORITIES/$PATH_USERS"
        const val USER_URI_PASS = "content://$AUTHORITIES/$PATH_USERS/#"
        const val COURSE_URI = "content://$AUTHORITIES/$PATH_COURSES"

        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NAME = "name"
        const val COLUMN_USER_AGE = "age"

        const val COLUMN_COURSE_ID = "id"
        const val COLUMN_COURSE_TITLE = "title"
    }
}
