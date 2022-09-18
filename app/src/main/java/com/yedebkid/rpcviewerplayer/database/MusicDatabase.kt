package com.yedebkid.rpcviewerplayer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yedebkid.rpcviewerplayer.model.domain.SongDomainData

@Database(entities = [SongDomainData::class], version = 1)
abstract class MusicDatabase: RoomDatabase() {
    abstract fun getMusicsDao(): MusicsDao

    companion object{
        @Volatile
        private var INSTANCE: MusicDatabase? = null

        fun getMusicDatabase(context: Context): MusicDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicDatabase::class.java,
                    "rpc_music_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}