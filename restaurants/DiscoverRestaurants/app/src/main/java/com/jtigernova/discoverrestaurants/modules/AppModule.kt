package com.jtigernova.discoverrestaurants.modules

import android.content.Context
import androidx.room.Room
import com.jtigernova.discoverrestaurants.repository.IRestaurantRepository
import com.jtigernova.discoverrestaurants.repository.RestaurantRepository
import com.jtigernova.discoverrestaurants.repository.api.DoorDash
import com.jtigernova.discoverrestaurants.repository.api.IRestaurantApi
import com.jtigernova.discoverrestaurants.repository.database.Db
import com.jtigernova.discoverrestaurants.repository.database.DbRestaurantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class, FragmentComponent::class)
object AppModule {
    @Provides
    fun provideRestaurantApi(): IRestaurantApi {
        return DoorDash()
    }

    //@Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): Db {
        return Room
            .databaseBuilder(appContext, Db::class.java, Db.NAME)
            .build()
    }

    @Provides
    fun provideDbRestaurantDao(db: Db): DbRestaurantDao {
        return db.restaurantDao()
    }

    @Provides
    fun provideRestaurantRepository(
        api: IRestaurantApi,
        dao: DbRestaurantDao
    ): IRestaurantRepository {
        return RestaurantRepository(
            api = api,
            restaurantDao = dao
        )
    }
}