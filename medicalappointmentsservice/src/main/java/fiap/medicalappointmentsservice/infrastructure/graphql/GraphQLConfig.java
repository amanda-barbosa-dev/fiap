//package fiap.medicalappointmentsservice.infrastructure.graphql;
//
//import fiap.medicalappointmentsservice.application.service.MedicalAppointmentResolver;
//import graphql.schema.GraphQLSchema;
//import graphql.schema.idl.RuntimeWiring;
//import graphql.schema.idl.SchemaGenerator;
//import graphql.schema.idl.SchemaParser;
//import graphql.schema.idl.TypeDefinitionRegistry;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//@Configuration
//public class GraphQLConfig {
//
//    @Bean
//    public GraphQLSchema graphQLSchema() throws IOException {
//        ClassPathResource schemaResource = new ClassPathResource("schema.graphqls");
//        try (InputStream inputStream = schemaResource.getInputStream()) {
//            TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(inputStream);
//            RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
//                    .type("Query", builder -> builder.dataFetcher("appointments", new MedicalAppointmentResolver()))
//                    .build();
//            return new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
//        }
//    }
//}