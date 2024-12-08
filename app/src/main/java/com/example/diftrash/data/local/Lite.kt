package com.example.diftrash.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.diftrash.data.local.Constant.Companion.DB_NAME
import com.example.diftrash.data.local.Constant.Companion.DB_VERSION

class Lite(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    data class Pengguna(val fullname: String, val email: String, val password: String)

    override fun onCreate(db: SQLiteDatabase?) {
        // Create table query
        val table = """
            CREATE TABLE pengguna(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fullname VARCHAR(100),
                email VARCHAR(100),
                password VARCHAR(100)
            )
        """.trimIndent()
        // Execute query to create table
        db?.execSQL(table)
        // Insert initial data (with corrected values)
        val insert = "INSERT INTO pengguna(fullname, email, password) VALUES('ADMIN', 'admin@example.com', 'root')"
        db?.execSQL(insert)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade as needed
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS pengguna")
            onCreate(db)
        }
    }

    fun login(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM pengguna WHERE email=? AND password=?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val count = cursor.count
        cursor.close()
        db.close()
        return count > 0
    }

    fun insertPengguna(pengguna: Pengguna): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("fullname", pengguna.fullname)
            put("email", pengguna.email)
            put("password", pengguna.password)
        }
        val result = db.insert("pengguna", null, values)
        db.close()
        return result
    }
    fun getNamaLengkap(username: String?): String? {
        val db = readableDatabase
        val query = "SELECT fullname FROM pengguna WHERE email = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        var namaLengkap: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            namaLengkap = cursor.getString(cursor.getColumnIndexOrThrow("fullname"))
            cursor.close()
        }
        db.close()
        return namaLengkap
    }
    // Fungsi untuk mengecek apakah username sudah ada di tabel pengguna
    fun isUsernameExists(username: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM pengguna WHERE fullname = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }
    fun isEmailExists(email: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM pengguna WHERE email = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }


    fun getEmail(username: String?): String? {
        val db = readableDatabase
        val query = "SELECT email FROM pengguna WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        var email: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            cursor.close()
        }
        db.close()
        return email
    }
    fun updateUser(username: String?, newFullName: String?, newEmail: String?) {
        val db = readableDatabase
        val values = ContentValues().apply {
            put("nama_lengkap", newFullName)
            put("email", newEmail)
        }
        db.update("pengguna", values, "username = ?", arrayOf(username))
        db.close()
    }




}