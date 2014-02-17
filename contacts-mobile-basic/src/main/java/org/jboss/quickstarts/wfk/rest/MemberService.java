/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.quickstarts.wfk.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.data.MemberRepository;
import org.jboss.quickstarts.wfk.model.Member;
import org.jboss.quickstarts.wfk.service.MemberRegistration;
import org.jboss.quickstarts.wfk.util.ConvertDate;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the members table.
 */
@Path("/members")
@RequestScoped
@Stateful
public class MemberService {
    @Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private MemberRepository repository;

    @Inject
    MemberRegistration registration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Member> listAllMembers() {
        return repository.findAllOrderedByName();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response lookupMemberById(@PathParam("id") long id) {
        Member member = repository.findById(id);
        if (member == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        log.info("findById " + id + ": found Member = " + member.getFirstName() + " " + member.getLastName() + " " + member.getEmail() + " " + member.getPhoneNumber() + " "
                + member.getBirthDate() + " " + member.getId());
        
        return Response.ok(member).build();
    }

    /**
     * Creates a new member from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMember(Member member) {
        log.info("createMember started. Member = " + member.getFirstName() + " " + member.getLastName() + " " + member.getEmail() + " " + member.getPhoneNumber() + " "
            + member.getBirthDate() + " " + member.getId());
        Response.ResponseBuilder builder = null;

        /*
         * There is a problem with the dates as the come back from the UI. They are affected by a local vs UTC change that
         * in some cases causes the date to lose a day every time they are saved.
         * 
         * The real root of the problem appears to be when a date with a time stamp in the local(which for any timezone 
         * west of GMT will subtract at least 1 hour causing the date to be subtracted too) is saved in the H2 database,
         * the database does not convert it to UTC it saves the local time with no timezone. To work around this we obtain
         * the timezone offset and subtract that from the date with local time. This at least brings us closer to UTC. In order 
         * to deal with any slight variations in the offset calculations of java we set the time of the 'Birthdate' to 
         * 12:00.  This means that even if the time shifts one way or another it does not change the day.
         * 
         * If a more robust database were used this would not be a problem. Please keep this in mind if you use this code base. 
         */
        member.setBirthDate(ConvertDate.localToGMT(member.getBirthDate()));

        try {
            // Validates member using bean validation
            validateMember(member);

            // Go add the new Member.
            registration.create(member);

            // Create an "ok" response
            builder = Response.ok().entity(member);

            log.info("createMember completed. Member = " + member.getFirstName() + " " + member.getLastName() + " " + member.getEmail() + " " + member.getPhoneNumber() + " "
                + member.getBirthDate() + " " + member.getId());
        } catch (ConstraintViolationException ce) {
            log.info("ConstraintViolationException - " + ce.toString());
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            log.info("ValidationException - " + e.toString());
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("email", "That email is already used, please use a unique email");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            log.info("Exception - " + e.toString());
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    /**
     * Updates a member with the ID provided in the Member. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMember(Member member) {
        log.info("updateMember started. Member = " + member.getFirstName() + " " + member.getLastName() + " " + member.getEmail() + " " + member.getPhoneNumber() + " "
            + member.getBirthDate() + " " + member.getId());
        Response.ResponseBuilder builder = null;
        
        /*
         * There is a problem with the dates as the come back from the UI. They are affected by a local vs UTC change that
         * in some cases causes the date to lose a day every time they are saved.
         * 
         * The real root of the problem appears to be when a date with a time stamp in the local(which for any timezone 
         * west of GMT will subtract at least 1 hour causing the date to be subtracted too) is saved in the H2 database,
         * the database does not convert it to UTC it saves the local time with no timezone. To work around this we obtain
         * the timezone offset and subtract that from the date with local time. This at least brings us closer to UTC. In order 
         * to deal with any slight variations in the offset calculations of java we set the time of the 'Birthdate' to 
         * 12:00.  This means that even if the time shifts one way or another it does not change the day.
         * 
         * If a more robust database were used this would not be a problem. Please keep this in mind if you use this code base. 
         */
        member.setBirthDate(ConvertDate.localToGMT(member.getBirthDate()));
        
        try {
            // Validates member using bean validation.
            validateMember(member);

            // Apply the changes the Member.
            registration.update(member);

            // Create an "ok" response.
            builder = Response.ok().entity(member);

            log.info("updateMember completed. Member = " + member.getFirstName() + " " + member.getLastName() + " " + member.getEmail() + " " + member.getPhoneNumber() + " "
                + member.getBirthDate() + " " + member.getId());
        } catch (ConstraintViolationException ce) {
            log.info("ConstraintViolationException - " + ce.toString());
            // Handle bean validation issues
            builder = createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            log.info("ValidationException - " + e.toString());
            // Handle the unique constrain violation
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("email", "That email is already used, please use a unique email");
            responseObj.put("error", "This is where errors are displayed that are not related to a specific field");
            responseObj.put("anotherError", "You can find this error message in /src/main/java/org/jboss/quickstarts/wfk/rest/MemberService.java line 167.");
            builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
        } catch (Exception e) {
            log.info("Exception - " + e.toString());
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    /**
     * Deletes a member using the ID provided. If the ID is not present then nothing can be deleted, and will return a 
     * JAX-RS response with either 200 OK or with a map of fields, and related errors.
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMember(Member member) {
        log.info("deleteMember started. Member = " + member.getFirstName() + " " + member.getLastName() + " " + member.getEmail() + " " + member.getPhoneNumber() + " "
            + member.getBirthDate() + " " + member.getId());
        Response.ResponseBuilder builder = null;

        try {
            if (member.getId() != null) {
                registration.delete(member);
            } else {
                log.info("MemberService - deleteMember - No ID was found so can't Delete.");
            }

            // Create an "ok" response
            builder = Response.ok().entity(member);
            log.info("deleteMember completed. Member = " + member.getFirstName() + " " + member.getLastName() + " " + member.getEmail() + " " + member.getPhoneNumber() + " "
                + member.getBirthDate() + " " + member.getId());
        } catch (Exception e) {
            log.info("Exception - " + e.toString());
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        return builder.build();
    }

    /**
     * <p>
     * Validates the given Member variable and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.
     * </p>
     * <p>
     * If the error is caused because an existing member with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.
     * </p>
     * 
     * @param member Member to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If member with the same email already exists
     */
    private void validateMember(Member member) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(member.getEmail(), member.getId())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    /**
     * Creates a JAX-RS "Bad Request" response including a map of all violation fields, and their message. This can be used
     * by clients to show violations.
     * 
     * @param violations A set of violations that needs to be reported
     * @return JAX-RS response containing all violations
     */
    private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<String, String>();

        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }

    /**
     * Checks if a member with the same email address is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "email")" constraint from the Member class.
     * 
     * Since Update will being using an email that is already in the database we need to make sure that it is the email
     * from the record being updated.  
     * 
     * @param email The email to check
     * @param id 
     * @return True if the email already exists, and false otherwise
     */
    public boolean emailAlreadyExists(String email, Long id) {
        Member member = null;
        Member memberWithID = null;
        try {
            member = repository.findByEmail(email);
        } catch (NoResultException e) {
            // ignore
        }

        if (member != null && id != null) {
            try {
                memberWithID = repository.findById(id);
                if (memberWithID.getEmail().equals(email)) {
                    member = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return member != null;
    }
}
