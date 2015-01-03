package org.asuki.servlet;

import static java.lang.System.out;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.asuki.json.JsonResource;
import org.asuki.web.servlet.JsonHttpServlet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class JsonHttpServletIT {

    // @Deployment(testable = false)
    @Deployment
    public static WebArchive createArchive() {
        final WebArchive war = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addClasses(JsonHttpServlet.class, JsonResource.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE,
                        ArchivePaths.create("beans.xml"));

        out.println(war.toString(Formatters.VERBOSE));

        return war;
    }

    @RunAsClient
    @Test
    public void shouldGetJson(@ArquillianResource URL baseURL)
            throws IOException {

        final URL url = new URL(baseURL, "json");

        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                url.openStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }

        assertThat(
                builder.toString(),
                is("{\"title\":\"Algorithm\",\"isMember\":true,\"price\":12.34,\"ids\":[\"101\",\"102\"],\"authors\":[{\"name\":\"Robby\",\"age\":30},{\"name\":\"Zak\",\"age\":20}]}"));
    }
}
