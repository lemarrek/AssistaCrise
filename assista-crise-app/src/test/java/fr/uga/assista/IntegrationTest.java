package fr.uga.assista;

import fr.uga.assista.config.AsyncSyncConfiguration;
import fr.uga.assista.config.DatabaseTestcontainer;
import fr.uga.assista.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        AssistaCriseApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        fr.uga.assista.config.JacksonHibernateConfiguration.class,
    }
)
@ImportTestcontainers(DatabaseTestcontainer.class)
public @interface IntegrationTest {}
