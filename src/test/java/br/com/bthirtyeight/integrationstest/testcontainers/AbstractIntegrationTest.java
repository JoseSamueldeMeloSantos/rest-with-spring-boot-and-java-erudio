package br.com.bthirtyeight.integrationstest.testcontainers;


import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;


@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.1.0");

        private void startContainers() {
            Startables.deepStart(Stream.of(mysql)).join();//inicializa o mysqlcontainer
        }

        private Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", mysql.getJdbcUrl(),
                    "spring.datasource.username", mysql.getUsername(),
                    "spring.datasource.password", mysql.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment();//pegou contexto do spring(tudo que e config(xml,yml,@config,...)
            MapPropertySource testcontainers = new MapPropertySource(
                    "testcontainers",
                     createConnectionConfiguration()
            );//cria propriedades para o yml
            environment.getPropertySources().addFirst(testcontainers);//adiciona as propriedades yml criadas antes de todas as outras
        }
    }
}
