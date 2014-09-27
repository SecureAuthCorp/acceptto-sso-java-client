## What is this project? ##
This is a Play 2.3 Java project to demo [Acceptto](http://acceptto.com/) CAS service.
 
## Quick start ##

1. [Install Play 2.3 (Activator)](https://playframework.com/)
1. `git clone https://github.com/acceptto-corp/acceptto-sso-java-client`
1. `cd acceptto-sso-java-client`
1. `activator run`
1. Open a browser and navigate to [localhost:9000](http://localhost:9000)

Activator will automatically download all dependencies which may take some time depending on your internet connection.

## Create your own client ##
You can use this sample and change it to meet your needs or you can create your own client from scratch. We use [pac4j](https://github.com/leleuj/pac4j) as CAS client and [play-pac4j](https://github.com/leleuj/play-pac4j) for Play integration.

### Create a new Play project ###

Create a new `play-java` project:

`activator new`

### Add dependencies ###

Add the following dependencies:

```sbt
libraryDependencies ++= Seq(
  "org.pac4j" % "play-pac4j_java" % "1.3.0-SNAPSHOT",
  "org.pac4j" % "pac4j-cas" % "1.6.0-SNAPSHOT"
)
```

Add the following resolver:

```sbt
resolvers ++= Seq(
  "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/"
)
```

### Configuration ###

Config CAS client in `onStart` method of `Global` settings class.

```java
import org.pac4j.cas.client.CasClient;
import org.pac4j.core.client.Clients;
import org.pac4j.play.Config;
import play.Application;
import play.GlobalSettings;
import play.Play;

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
```

### Controller and Action ###

Authentication controller should inherit from `JavaController` then you can use: 

- `getRedirectAction` to obtain the redirection action
- `getUserProfile` to obtain user profile if it was available

```java
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
```

### View ###

If the user is not authorized by Acceptto he or she will see the `Authenticate with Acceptto CAS` button, otherwise he or she will see the authorized profile information.

```html
@(profile : org.pac4j.core.profile.CommonProfile, urlCas: String)

<div>
@profile match {
    case _: org.pac4j.cas.profile.CasProfile => {
        <p>
            <a href="logout">Logout</a>
        </p>
        @profile
    }
    case _ => {
        <a href="@urlCas">Authenticate with Acceptto CAS</a><br />
    }
}
</div>

```

### Routes ###

The final step is to set your routes.

```
GET   /                       controllers.Application.index()
GET   /protected/index.html   controllers.Application.protectedIndex()
GET   /callback               org.pac4j.play.CallbackController.callback()
POST  /callback               org.pac4j.play.CallbackController.callback()
GET   /logout                 org.pac4j.play.CallbackController.logoutAndRedirect()
```
