package dk.chen.garbagev1.data.injection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dk.chen.garbagev1.data.database.ItemDao
import dk.chen.garbagev1.data.database.BinDao
import dk.chen.garbagev1.data.database.GarbageDatabase
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGarbageDatabase(
        @ApplicationContext context: Context,
        itemDaoProvider: Provider<ItemDao>,
        binDaoProvider: Provider<BinDao>
    ): GarbageDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = GarbageDatabase::class.java,
            name = "garbage_database"
        )
            .addCallback(callback = GarbageDatabase.Callback(itemDaoProvider, binDaoProvider))
            .build()
    }

    @Provides
    @Singleton
    fun provideItemDao(database: GarbageDatabase): ItemDao {
        return database.itemDao()
    }

    @Provides
    @Singleton
    fun provideBinDao(database: GarbageDatabase): BinDao {
        return database.binDao()
    }
}

