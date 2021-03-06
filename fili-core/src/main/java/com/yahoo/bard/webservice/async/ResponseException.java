// Copyright 2016 Yahoo Inc.
// Licensed under the terms of the Apache license. Please see LICENSE.md file distributed with this work for terms.
package com.yahoo.bard.webservice.async;

import com.yahoo.bard.webservice.druid.model.query.DruidAggregationQuery;
import com.yahoo.bard.webservice.druid.model.query.DruidQuery;

import javax.ws.rs.core.Response;

/**
 * Custom helper class to wrap Error Response object attributes in an Exception type. We need this class when argument
 * type is enforced as Exception.
 */
public class ResponseException extends Exception {

    private final int statusCode;
    private final String reason;
    private final String description;
    private final DruidQuery<?> druidQuery;
    private final Throwable cause;

    /**
     * Class constructor with throwable and other parameters.
     *
     * @param statusType  Status type of the response
     * @param druidQuery  The druid query being processed
     * @param error  Exception object with error details
     */
    public ResponseException(Response.StatusType statusType, DruidAggregationQuery<?> druidQuery, Throwable error) {
        this(
                statusType.getStatusCode(),
                ErrorUtils.getReason(error),
                ErrorUtils.getDescription(error),
                druidQuery,
                error
        );
    }

    /**
     * Class constructor with all the parameters to prepare the error response by considering error object as null.
     *
     * @param statusCode  Http status code for the error
     * @param reason  Reason for the error
     * @param description  Description for the error
     * @param druidQuery  The druid query being processed
     */
    public ResponseException(int statusCode, String reason, String description, DruidQuery<?> druidQuery) {
        this(statusCode, reason, description, druidQuery, null);
    }

    /**
     * Class constructor with all the parameters to prepare the error response.
     *
     * @param statusCode  Http status code for the error
     * @param reason  Reason for the error
     * @param description  Description for the error
     * @param druidQuery  The druid query being processed
     * @param cause  Exception object with error details
     */
    public ResponseException(
            int statusCode,
            String reason,
            String description,
            DruidQuery<?> druidQuery,
            Throwable cause
    ) {
        super(buildMessage(reason, description, statusCode, druidQuery, cause));
        this.statusCode = statusCode;
        this.reason = reason;
        this.description = description;
        this.druidQuery = druidQuery;
        this.cause = cause;
    }

    /**
     * Builds a message for this exception that is basically a straightforward serialization of its interesting state.
     *
     * @param reason  The reason for the error
     * @param description  A description of the error
     * @param statusCode  The status code received from Druid
     * @param druidQuery  The druid query that triggered the invalid response
     * @param cause  The cause of this exception, if any
     *
     * @return A Stringification of the parameters to serve as this exception's message
     */
    private static String buildMessage(
            String reason,
            String description,
            int statusCode,
            DruidQuery<?> druidQuery,
            Throwable cause
    ) {
        return String.format(
                "Reason: %s, Description: %s, statusCode: %d, druid query: %s, cause: %s",
                reason,
                description,
                statusCode,
                druidQuery,
                cause
        );
    }

    public String getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public DruidQuery<?> getDruidQuery() {
        return druidQuery;
    }

    public Throwable getCause() {
        return cause;
    }
}
