package com.top.planer

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.top.planer.entities.Settings
import com.top.planer.entities.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.nio.charset.Charset

class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {

    private val databaseScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // Załadowanie danych z JSON
        val defaultTypesJson = loadJsonFromAsset("defaultTypes.json")
        val defaultSettingsJson = loadJsonFromAsset("defaultSettings.json")
        val types = parseJsonToTypes(defaultTypesJson)
        val settings = parseJsonToSettings(defaultSettingsJson)

        databaseScope.launch {
            val typesDAO = AppDatabase.getDatabase(context).typesDAO()
            val settingsDAO = AppDatabase.getDatabase(context).settingsDAO()
            typesDAO.insertTypes(types)
            settingsDAO.insert(settings)
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
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val typeName = jsonObject.getString("name")
            val typeColor = jsonObject.getString("color")
            typesList.add(Types(name = typeName, colour = typeColor))
        }
        return typesList
    }

    private fun parseJsonToSettings(json: String): Settings {
        val jsonArray = JSONArray(json)
        val jsonObject = jsonArray.getJSONObject(0)

        val id = jsonObject.getInt("id")
        val dailyAvailableHours = jsonObject.getInt("dailyAvailableHours")
        return Settings(id = id, dailyAvailableHours = dailyAvailableHours)
    }
}
