package com.demo.barcelona.producer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean
import org.springframework.data.cassandra.config.SchemaAction
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories

@SpringBootApplication
class ProducerApplication

fun main(args: Array<String>) {
    runApplication<ProducerApplication>(*args)
}

@Configuration
@EnableReactiveCassandraRepositories(basePackages = ["com.demo.barcelona.producer"])
class CassandraConfiguration : AbstractReactiveCassandraConfiguration() {
    override fun getKeyspaceName() = "space"

    override fun getContactPoints() = "127.0.0.1"

    override fun getPort() = 9142

    override fun getSchemaAction(): SchemaAction = SchemaAction.NONE

    override fun getEntityBasePackages() = arrayOf("com.demo.barcelona.producer")


    override fun cluster(): CassandraClusterFactoryBean {
        val cluster = super.cluster()
        cluster.setJmxReportingEnabled(false)
        return cluster
    }
}