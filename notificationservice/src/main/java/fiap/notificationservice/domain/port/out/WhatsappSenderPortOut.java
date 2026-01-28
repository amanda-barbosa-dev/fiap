package fiap.notificationservice.domain.port.out;

public interface WhatsappSenderPortOut {

    void sendNotification(String phoneNumber, String message);
}
