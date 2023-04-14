package com.sanctumlabs.otp.datastore.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OtpEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OtpEntity>(OtpTable)

    var code by OtpTable.code
    var userId by OtpTable.userId
    var expiryTime by OtpTable.expiryTime
    var createdOn by OtpTable.createdOn
    var updatedOn by OtpTable.updatedOn
    var used by OtpTable.used
}
