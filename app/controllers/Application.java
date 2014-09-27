package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

import org.pac4j.play.java.*;
import org.pac4j.core.profile.CommonProfile;

public class Application extends JavaController {
    public static Result index() {
        final CommonProfile profile = getUserProfile();
        final String url = getRedirectAction("CasClient", "/").getLocation();
        return ok(views.html.index.render(profile, url));
    }

    @RequiresAuthentication(clientName = "CasClient")
    public static Result protectedIndex() {
      // profile
      final CommonProfile profile = getUserProfile();
      return ok(views.html.protectedIndex.render(profile));
    }
}
