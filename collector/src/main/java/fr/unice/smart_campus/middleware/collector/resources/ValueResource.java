package fr.unice.smart_campus.middleware.collector.resources;

/*
 * #%L
 * collector
 * %%
 * Copyright (C) 2015 - 2016 Université de Nice Sophia-Antipolis (UNS) - Centre National de la Recherche Scientifique (CNRS)
 * %%
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Université de Nice Sophia-Antipolis (UNS) -
 * Centre National de la Recherche Scientifique (CNRS)
 * Copyright © 2015 UNS, CNRS
 * 
 * 
 *   Authors: SmartCampus Team - http://smartcampus.github.io/sc-contacts/
 * 
 *   Architects: Sébastien Mosser – Laboratoire I3S – mosser@i3s.unice.fr
 *               Michel Riveill - Laboratoire I3S - riveill@i3s.unice.fr
 * #L%
 */

import fr.unice.smart_campus.middleware.collector.DataAccess;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Date;

import static javax.ws.rs.core.Response.Status;


/**
 * Value REST resource
 * Used to post a value into the collector
 */
@Path("value")
public class ValueResource {

    @POST
    @Produces("text/plain")
    @Consumes("application/json")
    public Response postValue(String jsonString) {

        String userErrorMessage = null;
        int messagesCount = 0;

        try {

            JSONArray messages;

            // Parse a JSON object or a JSON array
            try {
                JSONObject jsonObject = new JSONObject(new JSONTokener(jsonString));
                messages = new JSONArray();
                messages.put(jsonObject);
            } catch (JSONException exc) {
                messages = new JSONArray(new JSONTokener(jsonString));
            }

            messagesCount = messages.length();

            // Process messages
            for (int i = 0; i < messagesCount; i++) {
                JSONObject message = (JSONObject) messages.get(i);

                // Check JSON object size
                if (message.length() != 3) {
                    throw new Exception("Invalid JSON object size");
                }

                String name = message.getString("n");
                String value = message.getString("v");
                String time = message.getString("t");

                // TODO: Temporarily display values
                Date date = new Date(Long.parseLong(time) * 1000);
                System.out.println(
                        "Received: name = " + name + ", value = " + value + ", time = " + time + " (" + date + ");");

                // Store the message into the message queue
                try {
                    DataAccess.getInstance().postMessage(message.toString());
                } catch (Exception exc) {

                    exc.printStackTrace();

                    // HTTP internal server error
                    return Response
                            .status(Status.INTERNAL_SERVER_ERROR)
                            .entity("Internal server error: " + exc.getMessage())
                            .build();
                }
            }

        } catch (NumberFormatException exc) {
            userErrorMessage = "Wrong timestamp format";
        } catch (Exception exc) {
            userErrorMessage = exc.getMessage();
        }

        // HTTP user error response
        if (userErrorMessage != null) {
            return Response
                    .status(Status.BAD_REQUEST)
                    .entity("Error: " + userErrorMessage)
                    .build();
        }

        return Response
                .status(Status.CREATED)
                .entity(messagesCount + " value(s) posted with success!")
                .build();
    }


    @GET
    @Produces("text/plain")
    public Response getValue() {
        return getWrongMethodErrorResponse();
    }


    @PUT
    @Produces("text/plain")
    public Response putValue() {
        return getWrongMethodErrorResponse();
    }


    private static Response getWrongMethodErrorResponse() {
        return Response
                .status(Status.METHOD_NOT_ALLOWED)
                .entity("Error: Use the POST HTTP command to post a value into the collector")
                .build();
    }
}
