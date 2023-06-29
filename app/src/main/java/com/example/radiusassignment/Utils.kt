package com.example.radiusassignment

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Field
import javax.inject.Singleton

@Singleton
object Utils {

    fun getResId(resName: String, c: Class<*>): Int {
        return try {
            val idField: Field = c.getDeclaredField(resName)
            idField.getInt(idField)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }
}