import org.pac4j.cas.client.CasClient;
import org.pac4j.core.client.Clients;
import org.pac4j.play.Config;
import play.Application;
import play.GlobalSettings;
import play.Play;

//import play.mvc.Http.RequestHeader;
public class Global extends GlobalSettings {
    @Override
    public void onStart(final Application app) {
        final String baseUrl = Play.application().configuration().getString("baseUrl");
        final String casUrl = Play.application().configuration().getString("casUrl");
        // CAS
        final CasClient casClient = new CasClient();
        casClient.setCasLoginUrl(casUrl);
        final Clients clients = new Clients(baseUrl + "/callback", casClient);
        
        Config.setClients(clients);
    }
}
