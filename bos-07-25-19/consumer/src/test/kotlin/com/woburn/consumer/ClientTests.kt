package com.woburn.consumer

import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties

@AutoConfigureStubRunner(ids = ["com.woburn:producer:+:8091"], stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class ClientTests {

}