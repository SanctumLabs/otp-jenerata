package com.garihub.user.batchjobs.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class JobsBatchConfigurer(
    properties: BatchProperties?,
    @Qualifier("jobsDataSource")
    dataSource: DataSource?,
    transactionManagerCustomizers: TransactionManagerCustomizers?
) : BasicBatchConfigurer(properties, dataSource, transactionManagerCustomizers)
