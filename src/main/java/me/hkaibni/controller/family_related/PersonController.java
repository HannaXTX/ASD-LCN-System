package me.hkaibni.controller.family_related;

import io.smallrye.common.constraint.NotNull;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.hkaibni.dto.request.PersonDTO;
import me.hkaibni.dto.response.IdResponse;
import me.hkaibni.model.family.Person;
import me.hkaibni.service.family.PersonService;
import me.hkaibni.utils.ResponseUtil;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonController {

    @Inject
    PersonService personService;

    @GET
    @RolesAllowed({"USER","ADMIN"})
    public Response getAllPersons() {
        return ResponseUtil.ok(
                "Persons retrieved successfully",
                personService.getAllPersons()
        );
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"USER","ADMIN"})
    public Response getPersonById(@PathParam("id") String id) {

        Person person = personService.getPerson(id);

        if (person == null) {
            return ResponseUtil.notFound("Person not found");
        }

        return ResponseUtil.ok(
                "Person retrieved successfully",
                person
        );
    }

    @POST
    @Path("/create")
    @RolesAllowed({"USER","ADMIN"})
    public Response createPerson(PersonDTO dto) {
        return ResponseUtil.ok(
                "Person created successfully",
                new IdResponse(personService.createPerson(dto).getId())
        );
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"USER","ADMIN"})
    public Response updatePerson(
            @PathParam("id") String id,
            PersonDTO dto
    ) {

        Person person = personService.getPerson(id);

        if (person == null) {
            return ResponseUtil.notFound("Person not found");
        }

        return ResponseUtil.ok(
                "Person updated successfully",
                personService.updatePerson(person, dto)
        );
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"USER","ADMIN"})
    public Response deletePerson(@PathParam("id") String id) {

        Person person = personService.getPerson(id);

        if (person == null) {
            return ResponseUtil.notFound("Person not found");
        }

        personService.deletePerson(id);

        return ResponseUtil.ok(
                "Person deleted successfully",
                null
        );
    }
}