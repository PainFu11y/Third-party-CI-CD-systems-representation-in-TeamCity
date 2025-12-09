package com.actionwatch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowRun {

    public long id;

    @JsonProperty("name")
    public String workflowName;

    @JsonProperty("head_branch")
    public String branch;

    @JsonProperty("head_sha")
    public String commitSha;

    @JsonProperty("status")
    public String status;     // queued, in_progress, completed

    @JsonProperty("conclusion")
    public String conclusion; // success, failure, cancelled...

    @JsonProperty("created_at")
    public OffsetDateTime createdAt;

    @JsonProperty("run_started_at")
    public OffsetDateTime startedAt;

    @JsonProperty("updated_at")
    public OffsetDateTime updatedAt;

    public WorkflowRun(long id, String workflowName,
                       String branch, String commitSha,
                       String status, String conclusion,
                       OffsetDateTime createdAt, OffsetDateTime startedAt,
                       OffsetDateTime updatedAt) {
        this.id = id;
        this.workflowName = workflowName;
        this.branch = branch;
        this.commitSha = commitSha;
        this.status = status;
        this.conclusion = conclusion;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.updatedAt = updatedAt;
    }
}
