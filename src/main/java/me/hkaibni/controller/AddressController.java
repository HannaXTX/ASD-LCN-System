package me.hkaibni.controller;


import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.AddressSearchDTO;
import me.hkaibni.dto.ApiResponse;
import me.hkaibni.dto.CreateAddressDTO;
import me.hkaibni.dto.UserSearchDTO;
import me.hkaibni.model.Address;
import me.hkaibni.model.User;
import me.hkaibni.service.AddressService;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Date;
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
                        addressServ.getAllAddresses(),new Date(System.currentTimeMillis())
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
                        address,new Date(System.currentTimeMillis())
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
                                    null,new Date(System.currentTimeMillis())
                            )
                    )
                    .build();        }

        return Response.ok(
                new ApiResponse(
                        200,
                        "Address deleted successfully",
                        null,new Date(System.currentTimeMillis())
                )
        ).build();    }
    @Path("/{id}")
    @PUT
    @RolesAllowed("USER")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Response updateAddress(CreateAddressDTO dto, @PathParam("id") String id) throws Exception {
        int operation = addressServ.updateAddress(id,dto);
        if (operation==0)
            return Response.ok(
                    new ApiResponse(
                            200,
                            "Address updated successfully",
                            dto,new Date(System.currentTimeMillis())
                    )
            ).build();
        else if (operation==1){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            404,
                            "Address not found",
                            null,new Date(System.currentTimeMillis())
                    ))
                    .build();

        }
        else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ApiResponse(
                            409,
                            "Name Already exists",
                            null,new Date(System.currentTimeMillis())
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
                        new Date()
                )
        ).build();
    }





}
