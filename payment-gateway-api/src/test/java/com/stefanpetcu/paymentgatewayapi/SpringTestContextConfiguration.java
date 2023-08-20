package com.stefanpetcu.paymentgatewayapi;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.stefanpetcu.paymentgatewayapi.repository.PaymentEntityRepository;
import com.stefanpetcu.paymentgatewayapi.repository.PaymentSourceCardEntityRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.profiles.active=test",
                "wiremock.server.httpsPort=-1", // fixes wiremock issues on M1
                "service.external.sim-bank.base-url=http://localhost:${wiremock.server.port}",
        })
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 12345)
@EnableAutoConfiguration
public class SpringTestContextConfiguration {
    @Autowired
    public PaymentSourceCardEntityRepository paymentSourceCardEntityRepository;

    @Autowired
    public PaymentEntityRepository paymentEntityRepository;

    @BeforeEach
    @Transactional
    public void resetWireMockAndDatabase() {
        WireMock.reset();
        WireMock.resetAllScenarios();
        WireMock.resetAllRequests();
        this.paymentEntityRepository.deleteAll();
        this.paymentSourceCardEntityRepository.deleteAll();
    }
}
