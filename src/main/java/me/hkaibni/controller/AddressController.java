package me.hkaibni.controller;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.search.AddressSearchDTO;
import me.hkaibni.dto.response.ApiResponse;
import me.hkaibni.dto.entity_dto.AddressDTO;
import me.hkaibni.model.Address;
import me.hkaibni.service.AddressService;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;
import java.util.List;

@Path("/address")


public class AddressController {
    @Inject
    AddressService addressServ;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/test")
    @RolesAllowed({"USER"})
    public String test() {
        return jwt.getSubject() + " " + jwt.getGroups();
    }


    @GET
    @RolesAllowed("USER")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getAddresses(){
        return Response.ok(
                new ApiResponse(
                        200,
                        "Users retrieved successfully",
                        addressServ.getAllAddresses(), LocalDateTime.now()
                )
        ).build();
    }

    @Path("/{id}")
    @GET
    @RolesAllowed("USER")
    @Produces (MediaType.APPLICATION_JSON)
    public Response getAddress(@PathParam("id") String id){

        Address address = addressServ.getAddress(id);

        return Response.ok(
                new ApiResponse(
                        200,
                        "Address retrieved successfully",
                        address,LocalDateTime.now()
                )
        ).build();
    }

    @Path("/{id}")
    @DELETE
    @RolesAllowed("USER")
    @Produces (MediaType.APPLICATION_JSON)
    public Response deleteAddress(@PathParam("id") String id){

        if (!addressServ.deleteAddress(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(
                            new ApiResponse(
                                    404,
                                    "Address not found",
                                    null,LocalDateTime.now()
                            )
                    )
                    .build();        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "Address deleted successfully",
                        null,LocalDateTime.now()
                )
        ).build();    }
    @Path("/{id}")
    @PUT
    @RolesAllowed("USER")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response updateAddress(AddressDTO dto, @PathParam("id") String id) throws Exception {
        int operation = addressServ.updateAddress(id,dto);
        if (operation==0)
            return Response.ok(
                    new ApiResponse(
                            200,
                            "Address updated successfully",
                            dto,LocalDateTime.now()
                    )
            ).build();
        else if (operation==1){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            404,
                            "Address not found",
                            null,LocalDateTime.now()
                    ))
                    .build();

        }
        else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            409,
                            "Name Already exists",
                            null,LocalDateTime.now()
                    ))
                    .build();
        }
    }


    @GET
    @Path("/search")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUsers(AddressSearchDTO dto) {
        List<Address> results;
        if (dto.hasNoCriteria()) {
            results = addressServ.searchAddresses(dto.getValue());
        }
        else {
            results = addressServ.searchAddresses(dto);
        }
        return Response.ok(
                new ApiResponse(
                        200,
                        results.isEmpty()
                                ? "No Addresses found"
                                : "User search completed successfully",
                        results,
                        LocalDateTime.now()
                )
        ).build();
    }





}
