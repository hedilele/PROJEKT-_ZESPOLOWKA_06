package com.example.planer

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.planer.entities.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.Charset

class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {

    private val databaseScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // Załadowanie danych z JSON
        val defaultTypesJson = loadJsonFromAsset("defaultTypes.json")
        val types = parseJsonToTypes(defaultTypesJson)

        databaseScope.launch {
            val typesDAO = AppDatabase.getDatabase(context).typesDAO()
            typesDAO.insertTypes(types)
        }
    }


    // Funkcja ładująca JSON (plik tekstowy) z folderu Assets
    private fun loadJsonFromAsset(filename: String): String {
        val inputStream = context.assets.open(filename)
        return try {
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            String(buffer, Charset.forName("UTF-8"))
        } finally {
            inputStream.close()
        }
    }

    // Parse z json na Type
    private fun parseJsonToTypes(json: String): List<Types> {
        val typesList = mutableListOf<Types>()
        val jsonArray = JSONObject(json).getJSONArray("types")
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val typeName = jsonObject.getString("name")
            val typeColor = jsonObject.getString("color")
            typesList.add(Types(name = typeName, colour = typeColor))
        }
        return typesList
    }
}
