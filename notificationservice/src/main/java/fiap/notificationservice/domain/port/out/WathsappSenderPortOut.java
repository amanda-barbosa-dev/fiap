package fiap.notificationservice.domain.port.out;

public interface WathsappSenderPortOut {

    void sendNotification(String phoneNumber, String message);
}
