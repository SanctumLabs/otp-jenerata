package com.garihub.otp.database

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@Suppress("UnnecessaryAbstractClass")
@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    @CreationTimestamp
    @Column(name = "created_on")
    var createdOn: Instant = Instant.now()

    @UpdateTimestamp
    @Column(name = "updated_on")
    var updatedOn: Instant = Instant.now()
}
