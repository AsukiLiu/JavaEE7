package org.asuki.rx;

import static javax.json.Json.createObjectBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.slf4j.Logger;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Func2;

@Singleton
@Path("facade")
public class FacadeService {

    private static final String BASE_URL = "http://localhost:8080/sample-web";

    @Inject
    private Logger log;

    @Resource
    private ManagedExecutorService executorService;

    private WebTarget subAServiceTarget;

    private WebTarget subBServiceTarget;

    @PostConstruct
    private void initializeRest() {
        subAServiceTarget = ClientBuilder.newClient().target(
                BASE_URL + "/rs/sub-a");
        subBServiceTarget = ClientBuilder.newClient().target(
                BASE_URL + "/rs/sub-b");
    }

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public void findById(@Suspended AsyncResponse asyncResponse,
            @PathParam("id") String id) {

        Observable<JsonObject> develperInfo = getDevelperInfo(id);
        Observable<JsonArray> languagesInfo = getLanguagesInfo(id);

        // @formatter:off
        Func2<JsonObject, JsonArray, JsonObject> merge = 
                (JsonObject develper, JsonArray languages) -> 
                    createObjectBuilder()
                    .add("develper", develper)
                    .add("languages", languages)
                    .build();
         // @formatter:on

        Observable.zip(develperInfo, languagesInfo, merge).subscribe(
                new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        log.info("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        log.error("onError");
                        asyncResponse.resume(e);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        log.info("onNext");
                        asyncResponse.resume(jsonObject);
                    }
                });
    }

    private Observable<JsonObject> getDevelperInfo(String id) {
        subAServiceTarget.path(id).request().get(JsonObject.class);

        return Observable.create((OnSubscribe<JsonObject>) subscriber -> {

            Runnable r = () -> {
                subscriber.onNext(subAServiceTarget.path(id).request()
                        .get(JsonObject.class));
                subscriber.onCompleted();
            };

            executorService.execute(r);
        });
    }

    private Observable<JsonArray> getLanguagesInfo(String id) {
        return Observable.create((OnSubscribe<JsonArray>) subscriber -> {

            Runnable r = () -> {
                subscriber.onNext(subBServiceTarget.path(id).request()
                        .get(JsonArray.class));
                subscriber.onCompleted();
            };

            executorService.execute(r);
        });
    }

}
