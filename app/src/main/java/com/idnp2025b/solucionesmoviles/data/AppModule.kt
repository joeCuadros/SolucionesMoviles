package com.idnp2025b.solucionesmoviles.data.di

import android.content.Context
import androidx.room.Room
import com.idnp2025b.solucionesmoviles.data.AppDatabase
import com.idnp2025b.solucionesmoviles.data.dao.DepartamentoDao
import com.idnp2025b.solucionesmoviles.data.dao.PlantaDao
import com.idnp2025b.solucionesmoviles.data.dao.TallerDao
import com.idnp2025b.solucionesmoviles.data.dao.TipoTallerDao
import com.idnp2025b.solucionesmoviles.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bodega.db"
        ).build()

    @Provides
    fun providePlantaDao(db: AppDatabase): PlantaDao =
        db.plantaDao()

    @Provides
    fun provideDepartamentoDao(db: AppDatabase): DepartamentoDao =
        db.departamentoDao()

    @Provides
    fun provideTipoTallerDao(db: AppDatabase): TipoTallerDao =
        db.tipoTallerDao()

    @Provides
    fun provideTallerDao(db: AppDatabase): TallerDao =
        db.tallerDao()
    @Provides
    @Singleton
    fun provideRepository(
        db: AppDatabase,
        plantaDao: PlantaDao,
        departamentoDao: DepartamentoDao,
        tipoTallerDao: TipoTallerDao,
        tallerDao: TallerDao
    ): Repository = Repository(
        db,
        plantaDao,
        departamentoDao,
        tipoTallerDao,
        tallerDao
    )
}
