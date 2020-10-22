package com.garihub.otp

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.util.TimeZone
import javax.annotation.PostConstruct

@SpringBootApplication
@ComponentScan(basePackages = ["com.garihub.otp"])
@EntityScan("com.garihub.otp.database")
@EnableJpaRepositories("com.garihub.otp.database")
class OtpApp {
    @PostConstruct
    fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Nairobi"))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(OtpApp::class.java)
}
