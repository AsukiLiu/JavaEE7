package org.asuki.webservice.rs;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.client.ClientBuilder;

import org.asuki.common.Resources;
import org.asuki.rx.FacadeService;
import org.asuki.rx.SubAService;
import org.asuki.rx.SubBService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FacadeServiceIT {

    @ArquillianResource
    private URL baseUrl;

    @Deployment(testable = false, name = "sub-a")
    public static WebArchive createDeploymentSubAService() {
        return ShrinkWrap.create(WebArchive.class, "sub-a.war").addClasses(
                SubAService.class, RestApplication.class);
    }

    @Deployment(testable = false, name = "sub-b")
    public static WebArchive createDeploymentSubBService() {
        return ShrinkWrap.create(WebArchive.class, "sub-b.war").addClasses(
                SubBService.class, RestApplication.class);
    }

    @Deployment(testable = false, name = "test")
    public static WebArchive createDeploymentFacadeService() {
        return ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addClasses(FacadeService.class, RestApplication.class,
                        Resources.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE,
                        ArchivePaths.create("beans.xml"))
                .addAsLibraries(
                        Maven.resolver()
                                .loadPomFromFile("pom.xml")
                                .resolve(
                                        "com.netflix.rxjava:rxjava-core:0.19.0")
                                .withTransitivity().as(JavaArchive.class));
    }

    @Test
    @OperateOnDeployment("test")
    public void shouldFindById() throws MalformedURLException {

        String result = ClientBuilder.newClient()
                .target(URI.create(new URL(baseUrl, "rs/").toExternalForm()))
                .path("facade/1").request(APPLICATION_JSON).get(String.class);

        assertThat(
                result,
                is("{\"develper\":{\"author\":\"Andy\",\"city\":\"Tokyo\"},\"languages\":[\"Chinese\",\"English\",\"Japanese\"]}"));
    }
}