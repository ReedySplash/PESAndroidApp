package edu.pes.laresistencia.storage

import android.content.Context
import edu.pes.laresistencia.register.UserRequest
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


class InternalStorage {

    private val filename = "tokenfile"
    private val filename_user = "userfile"

    fun storeToken(token: String, context: Context) {
        try {
            File(context.filesDir, filename)
            val file = context.openFileOutput(filename, Context.MODE_PRIVATE)
            file.write(token.toByteArray())
            file.close()
        }
        catch (e: Exception) {

        }
    }

    fun getToken(context: Context): String? {
        try {
            var file = context.openFileInput(filename)
            var isr = InputStreamReader(file)
            var br = BufferedReader(isr)
            var sb = StringBuilder()
            var line = br.readLine()
            while (line != null) {
                sb.append(line)
                line = br.readLine()
            }
            file.close()
            isr.close()
            br.close()
            return sb.toString()
        }
        catch (e: Exception) {

        }
        return null
    }

    fun deleteToken(context: Context): Boolean
    {
        val dir: File = context.filesDir
        val file: File = File(dir, filename)
        return file.delete()
    }

    fun storeUser(context: Context, body: UserRequest) {
        try {
            File(context.filesDir, filename_user).bufferedWriter()
            val file = context.openFileOutput(filename_user, Context.MODE_PRIVATE).bufferedWriter()
            file.write(body.name)
            file.newLine()
            file.write(body.surname)
            file.newLine()
            file.write(body.gender.toString())
            file.newLine()
            file.write(body.birthDate.toString())
            file.newLine()
            file.write(body.email)
            file.newLine()
            file.write(body.country)
            file.newLine()
            file.close()
        }
        catch (e: Exception) {

        }
    }

    fun getUser(context: Context): ArrayList<String>? {
        try {
            var file = context.openFileInput(filename_user)
            var isr = InputStreamReader(file)
            var br = BufferedReader(isr)
            var sb = ArrayList<String>()
            var it = 0
            var line = br.readLine()
            while (line != null) {
                sb.add(line)
                line = br.readLine()
                it += 1
                if (it == 6) break
            }
            file.close()
            isr.close()
            br.close()
            return sb
        }
        catch (e: Exception) {

        }
        return null
    }

    fun deleteUser(context: Context): Boolean {
        val dir: File = context.filesDir
        val file: File = File(dir, filename_user)
        return file.delete()
    }

}