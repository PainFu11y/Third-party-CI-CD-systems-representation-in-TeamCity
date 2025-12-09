package com.actionwatch.client;

import com.actionwatch.model.WorkflowRun;
import com.actionwatch.model.Job;
import com.actionwatch.model.JobStep;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class GitHubClient {

    private final OkHttpClient client = new OkHttpClient();
    private final String token;

    public GitHubClient(String token) {
        this.token = token;
    }

    private String get(String url) throws IOException {
        Request req = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/vnd.github+json")
                .build();

        try (Response resp = client.newCall(req).execute()) {
            if (!resp.isSuccessful()) {
                throw new IOException("GitHub API error: " + resp.code());
            }
            return resp.body().string();
        }
    }


    public List<WorkflowRun> fetchWorkflowRuns(String repo) throws IOException {
        String url = "https://api.github.com/repos/" + repo + "/actions/runs?per_page=50";
        JSONObject json = new JSONObject(get(url));
        JSONArray runsArray = json.getJSONArray("workflow_runs");

        List<WorkflowRun> runs = new ArrayList<>();
        for (int i = 0; i < runsArray.length(); i++) {
            JSONObject r = runsArray.getJSONObject(i);
            WorkflowRun run = new WorkflowRun(
                    r.getLong("id"),
                    r.getString("name"),
                    r.getString("head_branch"),
                    r.getString("head_sha"),
                    r.getString("status"),
                    r.optString("conclusion", null),
                    OffsetDateTime.parse(r.getString("created_at")),
                    r.has("run_started_at") && !r.isNull("run_started_at") ? OffsetDateTime.parse(r.getString("run_started_at")) : null,
                    OffsetDateTime.parse(r.getString("updated_at"))
            );
            runs.add(run);
        }
        return runs;
    }


    public List<Job> fetchJobs(String repo, long runId) throws IOException {
        String url = "https://api.github.com/repos/" + repo + "/actions/runs/" + runId + "/jobs?per_page=50";
        JSONObject json = new JSONObject(get(url));
        JSONArray jobsArray = json.getJSONArray("jobs");

        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < jobsArray.length(); i++) {
            JSONObject j = jobsArray.getJSONObject(i);
            Job job = new Job();
            job.id = j.getLong("id");
            job.name = j.getString("name");
            job.status = j.getString("status");
            job.conclusion = j.optString("conclusion", null);
            job.startedAt = j.has("started_at") && !j.isNull("started_at") ? OffsetDateTime.parse(j.getString("started_at")) : null;
            job.completedAt = j.has("completed_at") && !j.isNull("completed_at") ? OffsetDateTime.parse(j.getString("completed_at")) : null;

            JSONArray stepsArray = j.getJSONArray("steps");
            List<JobStep> steps = new ArrayList<>();
            for (int s = 0; s < stepsArray.length(); s++) {
                JSONObject st = stepsArray.getJSONObject(s);
                JobStep step = new JobStep();
                step.name = st.getString("name");
                step.number = st.getInt("number");
                step.status = st.getString("status");
                step.conclusion = st.optString("conclusion", null);
                step.startedAt = st.has("started_at") && !st.isNull("started_at") ? OffsetDateTime.parse(st.getString("started_at")) : null;
                step.completedAt = st.has("completed_at") && !st.isNull("completed_at") ? OffsetDateTime.parse(st.getString("completed_at")) : null;
                steps.add(step);
            }
            job.steps = steps;

            jobs.add(job);
        }
        return jobs;
    }
}
