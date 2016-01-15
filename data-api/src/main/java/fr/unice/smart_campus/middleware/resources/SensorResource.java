package fr.unice.smart_campus.middleware.resources;

/*
 * #%L
 * data-api
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

import fr.unice.smart_campus.middleware.accessor.DataAccessor;
import fr.unice.smart_campus.middleware.Helper;
import fr.unice.smart_campus.middleware.TimeRange;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.ws.rs.core.Response.Status;


/**
 * Sensor REST resource.
 * To get details and values of sensors.
 */
@Path("sensors")
public class SensorResource {

	@GET
	@Produces("application/json")
	public Response getSensors () {
		DataAccessor access = null;
		String data = "";

		try {
			// Try to get access to the database
			access = new DataAccessor();
			data = access.getSensors(null);
			access.close();
		} catch (Exception e) {
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Error: " + e.getMessage())
					.build();
		}

		return Response
				.status(Status.OK)
				.entity(data)
				.build();
	}

    /**
     * GET requiest to retrieve the last value of one sensor (id)
     * @param idSensor Identifier of the sensor
     * @param convert Convert timestamp to human-readable date
     * @return a JSON Object with the last value of the sensor
     *          (or 500 HTTP error if the connection to the database does not work
     *          or 400 HTTP error if the url is not correct)
     */
    @GET
    @Path("{idSensor}/data/last")
    @Produces("application/json")

    public Response getLastDataFromSensor(@PathParam("idSensor") String idSensor,
                                          @QueryParam("convert") boolean convert,
                                          @QueryParam("format") int format)
    {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);

        return getDataFromSensorJSON(idSensor, strDate, convert);
    }

	/**
	 * GET request to retrieve values of one sensor (id)
	 *      depending on dates :
	 *      - no dates : all values of the sensor are returned
	 *      - one date : the last value of the sensor before this date
	 *      - two dates : al values before these two dates (included)
	 *
	 * @param idSensor Identifier of the sensor
	 * @param date Dates of recorded values that you want. Optional.
	 *             Format : yyyy-MM-dd HH:mm:ss or yyyy-MM-dd HH:mm:ss/yyyy-MM-dd HH:mm:ss
     * @param convert Convert timestamp to human-readable date
	 * @return a JSON Object with the values of the sensor, depending on time.
	 *          (or 500 HTTP error if the connection to the database does not work
	 *          or 400 HTTP error if the url is not correct)
	 */
	@GET
    @Produces("application/json")
    @Path("{idSensor}/data")
	public Response getDataFromSensorJSON (@PathParam("idSensor") String idSensor,
	                                   @QueryParam("date") String date,
                                       @QueryParam("convert") boolean convert) {

		// URL Dates parsing
		TimeRange time = new TimeRange(0L, 0L);
		if (date != null) {
			try {
				time = Helper.getTimestamps(date);
			} catch (ParseException exc) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(exc.getMessage())
						.build();
			}
		}

		DataAccessor access = null;
		String data = "";
		String error = null;
		try {
			// Try to get access to the database
			access = new DataAccessor();
			data = access.getDataFromSensor(idSensor, time.getFirst(), time.getSecond(), convert, "json");
			access.close();
		} catch (Exception e) {
			access.close();
			error = e.getMessage();
		}

		if (error != null)
			return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity("Error: " + error)
				.build();

		return Response
				.status(Status.OK)
				.entity(data)
				.build();
	}

    @GET
    @Produces("application/rss+xml")
    @Path("{idSensor}/dataxml.xml")
    public Response getDataFromSensorRSS (@PathParam("idSensor") String idSensor) {

        // URL Dates parsing
        TimeRange time = new TimeRange(0L, 0L);


        DataAccessor access = null;
        String data = "";
        String error = null;
        try {
            // Try to get access to the database
            access = new DataAccessor();
            data = access.getDataFromSensor(idSensor, time.getFirst(), time.getSecond(), true, "rss");
            access.close();
        } catch (Exception e) {
            access.close();
            error = e.getMessage();
        }

        if (error != null)
            return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + error)
                    .build();

        return Response
                .status(Status.OK)
                .entity(data)
                .build();
    }

	@POST
	@Produces("text/plain")
	@Consumes("application/json")
	public Response postValue (String jsonString) {
		return null;
	}

	@PUT
	@Produces("text/plain")
	public Response putValue (String jsonString) {
		return null;
	}
}
