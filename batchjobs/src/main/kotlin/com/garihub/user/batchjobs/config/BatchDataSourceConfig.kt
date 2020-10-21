package com.garihub.user.batchjobs.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class BatchDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "datasource.jobs")
    fun jobsDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    // Configure secondary data source for batch
    // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-two-datasources
    @Bean
    fun jobsDataSource(): DataSource {
        return jobsDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource::class.java)
            .build()
    }
}
