package si.fri.paw.api.v1.health;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class NotificationHealthCheck implements HealthCheck {

    Logger log = Logger.getLogger(NotificationHealthCheck.class.getName());

    @Override
    public HealthCheckResponse call() {

        String url = ConfigurationUtil.getInstance().get("kumuluzee.external.notification-url").get();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200){
                return HealthCheckResponse.up(NotificationHealthCheck.class.getSimpleName());
            }
        }
        catch (Exception e){
            log.info("Error connecting to Notification server.");
        }
        return HealthCheckResponse.down(NotificationHealthCheck.class.getSimpleName());
    }
}
