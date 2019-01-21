package com.muhammadwahyudin.notesmvvm.data.local

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.muhammadwahyudin.notesmvvm.data.model.Note
import org.jetbrains.anko.doAsync

@Database(entities = [Note::class], version = 2)
abstract class AppDatabase: RoomDatabase() {

    abstract fun noteDao() : NoteDao

    companion object {
        var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if(instance == null){
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "AppDB")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        fun destroyDatabase(){
            instance = null
        }

        // Prepopulate db
        private var roomCallback: RoomDatabase.Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                doAsync {
                    val noteDao = instance!!.noteDao()
                    for(i in 1..3) {
                        noteDao.insert(Note("Title$ $i", "Description $i", i))
                    }
                    Log.d("DB", "Prepopulated db")
                }
            }
        }

    }

}