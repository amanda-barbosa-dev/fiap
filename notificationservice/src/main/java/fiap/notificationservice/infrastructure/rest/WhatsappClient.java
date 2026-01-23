package fiap.notificationservice.infrastructure.rest;

import fiap.notificationservice.domain.port.out.WathsappSenderPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WhatsappClient implements WathsappSenderPortOut {
    @Override
    public void sendNotification(String phoneNumber, String message) {
        System.out.println("=====================================");
        System.out.println("SENDING REMINDER TO: " + phoneNumber);
        System.out.println(message);
        System.out.println("=====================================");
    }

}
