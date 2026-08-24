package me.hkaibni.utils;

import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.response.ApiResponse;

import java.time.LocalDateTime;

public class ResponseUtil {

    public static Response ok(String message, Object data) {
        return Response.ok(
                new ApiResponse(
                        200,
                        message,
                        data,
                        LocalDateTime.now()
                )
        ).build();
    }

    public static Response created(String message, Object data) {
        return Response.status(Response.Status.CREATED)
                .entity(
                        new ApiResponse(
                                201,
                                message,
                                data,
                                LocalDateTime.now()
                        )
                )
                .build();
    }

    public static Response badRequest(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(
                        new ApiResponse(
                                400,
                                message,
                                null,
                                LocalDateTime.now()
                        )
                )
                .build();
    }

    public static Response notFound(String message) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(
                        new ApiResponse(
                                404,
                                message,
                                null,
                                LocalDateTime.now()
                        )
                )
                .build();
    }

    public static Response conflict(String message) {
        return Response.status(Response.Status.CONFLICT)
                .entity(
                        new ApiResponse(
                                409,
                                message,
                                null,
                                LocalDateTime.now()
                        )
                )
                .build();
    }

    public static Response unauthorized(String message) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(
                        new ApiResponse(
                                401,
                                message,
                                null,
                                LocalDateTime.now()
                        )
                )
                .build();
    }

    public static Response forbidden(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(
                        new ApiResponse(
                                403,
                                message,
                                null,
                                LocalDateTime.now()
                        )
                )
                .build();
    }
}