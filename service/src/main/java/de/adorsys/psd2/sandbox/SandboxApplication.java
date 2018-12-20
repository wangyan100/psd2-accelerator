package de.adorsys.psd2.sandbox;

import de.adorsys.psd2.sandbox.migration.MigrationRunner;
import de.adorsys.psd2.sandbox.xs2a.Xs2aConfig;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {
    // TODO no persistence for now
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    // TODO disable security for now
    SecurityAutoConfiguration.class,
    ManagementWebSecurityAutoConfiguration.class
})
@PropertySource(
    value = {
        "classpath:sandbox-application.properties",
        "classpath:sandbox-application-${spring.profiles.active}.properties"
    },
    ignoreResourceNotFound = true
)
@EnableSwagger2
@ComponentScan(basePackages = "de.adorsys.psd2.sandbox.portal")
public class SandboxApplication {

  /**
   * Starts our spring boot app.
   *
   * @param args CLI args
   */
  public static void main(String[] args) {
    if (args.length != 0 && args[0].matches("migrate|generate-schema")) {
      new SpringApplicationBuilder()
          .parent(MigrationRunner.class).web(false)
          .run(args);
    } else {
      new SpringApplicationBuilder()
          .parent(EmptyConfiguration.class).web(false)
          .child(SandboxApplication.class).listeners(new StartFailedListener()).web(true)
          .sibling(Xs2aConfig.class).listeners(new StartFailedListener()).web(true)
          .run(args);
    }
  }

  /*
   * Looks like we can't have two unrelated contexts, so we need an empty parent
   */
  @Configuration
  static class EmptyConfiguration {

  }

  private static class StartFailedListener implements ApplicationListener<ApplicationFailedEvent> {

    @Override
    public void onApplicationEvent(ApplicationFailedEvent applicationFailedEvent) {
      SpringApplication.exit(applicationFailedEvent.getApplicationContext().getParent(),
          (ExitCodeGenerator) () -> 1);
    }
  }
}
