package com.actionwatch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobStep {

    public String name;

    @JsonProperty("number")
    public int number;

    @JsonProperty("status")
    public String status; // queued, in_progress, completed

    @JsonProperty("conclusion")
    public String conclusion; // success, failure

    @JsonProperty("started_at")
    public OffsetDateTime startedAt;

    @JsonProperty("completed_at")
    public OffsetDateTime completedAt;
}
