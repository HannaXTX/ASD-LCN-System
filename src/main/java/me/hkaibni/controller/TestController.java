package me.hkaibni.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/test")
public class TestController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response test1() {
        return Response.ok(
                Map.of("message", "CONNECTED")
        ).build();
    }
}