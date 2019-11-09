package com.example.streamy

class MessageClientTests {

    private lateinit var client: MessageClient

    private lateinit var webClient: WebClient

    @BeforeEach
    fun setUp() {
        client = MessageClient(webClient)
    }

    @Test
    fun `should get all the messages`() {
        StepVerifier
                .create(client.getMessages())
                .expectSubscription()
                .assertNext {
                    // assert we got the message
                }
                .expectComplete()
                .verify()
    }

}

