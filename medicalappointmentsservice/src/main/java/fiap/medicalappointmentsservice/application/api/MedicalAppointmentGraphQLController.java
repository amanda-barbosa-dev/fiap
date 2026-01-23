package fiap.medicalappointmentsservice.application.api;

import fiap.medicalappointmentsservice.domain.port.in.MedicalAppointmentGraphQLInterface;
import graphql.ExecutionResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import graphql.GraphQL;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MedicalAppointmentGraphQLController {

    private final GraphQL graphQl;

    private final MedicalAppointmentGraphQLInterface graphQLInterface;

    @PostMapping("/graphql")
    public ResponseEntity<Map<String, Object>> graphql(@RequestBody Map<String, Object> request) {
        String query = (String) request.get("query");
        ExecutionResult result = graphQl.execute(query);
        return ResponseEntity.ok(result.toSpecification());
    }

}
