package fiap.medicalappointmentsservice.infrastructure.graphql;

import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfig {

    @Bean
    public ServletRegistrationBean graphQLServlet(GraphQLConfiguration graphQLConfiguration) {
        return new ServletRegistrationBean<>(GraphQLHttpServlet.with(graphQLConfiguration), "/graphql");
    }
}