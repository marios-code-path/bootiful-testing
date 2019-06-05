package com.demo.producer

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

// what is the configuration narrative?
@Configuration
@EnableReactiveCassandraRepositories(basePackages = ["com.demo.producer"])
class CassandraConfiguration : AbstractReactiveCassandraConfiguration() {
    override fun getKeyspaceName() = "space"

    override fun getContactPoints() = "127.0.0.1"

    override fun getPort() = 9142

    override fun getSchemaAction(): SchemaAction = SchemaAction.NONE

    override fun getEntityBasePackages() = arrayOf("com.demo.producer")


    override fun cluster(): CassandraClusterFactoryBean {
        val cluster = super.cluster()
        cluster.setJmxReportingEnabled(false)
        return cluster
    }
}