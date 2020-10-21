package com.garihub.user.batchjobs.config

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.batch.BatchDataSourceInitializer
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import javax.sql.DataSource

@Configuration
@EnableBatchProcessing
class BatchConfiguration(var properties: BatchProperties) {

    @Bean
    fun batchDataSourceInitializer(
        @Qualifier("jobsDataSource")
        dataSource: DataSource,
        resourceLoader: ResourceLoader
    ): BatchDataSourceInitializer {
        return BatchDataSourceInitializer(
            dataSource, resourceLoader,
            this.properties
        )
    }
}
