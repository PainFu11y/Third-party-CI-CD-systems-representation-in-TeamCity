package com.actionwatch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {

    public long id;

    public String name;

    @JsonProperty("status")
    public String status; // queued, in_progress, completed

    @JsonProperty("conclusion")
    public String conclusion; // success, failure, cancelled...

    @JsonProperty("started_at")
    public OffsetDateTime startedAt;

    @JsonProperty("completed_at")
    public OffsetDateTime completedAt;

    public List<JobStep> steps;
}
