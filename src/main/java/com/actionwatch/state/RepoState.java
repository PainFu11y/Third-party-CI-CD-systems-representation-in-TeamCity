package com.actionwatch.state;

/**
 * Stores last processed entity IDs/timestamps for a specific repository.
 * Helps the monitor avoid reporting old events across restarts.
 */
public class RepoState {

    /**
     * ID of the last workflow run that was already processed.
     * Prevents re-reporting workflow run creations and state changes.
     */
    public long lastWorkflowRunId = 0;

    /**
     * ID of the last job event processed (jobs have unique IDs).
     * Ensures only new job start/finish events are reported.
     */
    public long lastJobId = 0;

    /**
     * Unix timestamp of the last processed step event.
     * Steps inside jobs do not have stable IDs, only timestamps.
     */
    public long lastStepEventTime = 0;
}
