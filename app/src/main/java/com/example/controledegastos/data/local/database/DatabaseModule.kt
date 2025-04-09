package com.example.controledegastos.data.local.database

import android.content.Context
import androidx.room.Room
import com.example.controledegastos.data.local.dao.ItemsDao
import com.example.controledegastos.data.local.dao.ItemsDaoSum
import com.example.controledegastos.data.repository.ItemsSumRepository
import com.example.controledegastos.data.usecase.MonthTotalValue
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database").fallbackToDestructiveMigration()
            .build()

    }

    @Provides
    fun provideItemsDao(database: AppDatabase): ItemsDao {
        return database.getItemsDao()
    }

    @Provides
    fun provideItemsDaoSum(database: AppDatabase): ItemsDaoSum {
        return database.getItemsDaoSum()
    }

    @Provides
    @Singleton
    fun provideMonthTotalValue(@ApplicationContext context: Context, itemsSumRepository: ItemsSumRepository): MonthTotalValue {
        return MonthTotalValue(context, itemsSumRepository)
    }
}

