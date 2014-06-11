package org.asuki.webservice.rs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.asuki.webservice.rs.annotation.Tracked;

// http://localhost:8080/sample-web/rs
@Tracked
@ApplicationPath("rs")
public class RestApplication extends Application {

}
