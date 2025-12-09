package com.actionwatch;

import com.actionwatch.client.GitHubClient;
import com.actionwatch.model.Job;
import com.actionwatch.state.MonitorState;
import com.actionwatch.state.RepoState;
import com.actionwatch.model.WorkflowRun;

import java.util.Comparator;
import java.util.List;

public class ActionWatch {

    private static final int POLL_INTERVAL_SEC = 10;

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: java -jar ActionWatch.jar <owner/repo> <token>");
            return;
        }

        String repo = args[0];
        String token = args[1];

        GitHubClient github = new GitHubClient(token);
        MonitorState state = MonitorState.load();
        RepoState repoState = state.getRepo(repo);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping, saving state...");
            state.save();
        }));

        System.out.println("Monitoring GitHub repository: " + repo);

        while (true) {
            try {
                List<WorkflowRun> runs = github.fetchWorkflowRuns(repo);
                processWorkflowRuns(repoState, runs);
                processJobs(github, repo, repoState, runs);

                Thread.sleep(POLL_INTERVAL_SEC * 1000);
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void processWorkflowRuns(RepoState repoState, List<WorkflowRun> runs) {
        runs.sort(Comparator.comparingLong(r -> r.id));

        for (WorkflowRun run : runs) {
            if (run.id <= repoState.lastWorkflowRunId) continue;

            printWorkflowEvent(run);
            repoState.lastWorkflowRunId = run.id;
        }
    }

    private static void processJobs(GitHubClient github, String repo, RepoState repoState, List<WorkflowRun> workflowRuns) {
        for (WorkflowRun run : workflowRuns) {
            try {
                List<Job> jobs = github.fetchJobs(repo, run.id);
                if (jobs == null) continue;

                jobs.sort(Comparator.comparingLong(j -> j.id));

                for (Job job : jobs) {
                    if (job.id <= repoState.lastJobId) continue;

                    printJobEvent(run, job);
                    repoState.lastJobId = job.id;
                }

            } catch (Exception ex) {
                System.out.println("Error fetching jobs for run " + run.id + ": " + ex.getMessage());
            }
        }
    }

    private static void printJobEvent(WorkflowRun run, Job job) {
        System.out.printf(
                "[JOB] workflow=%s | jobId=%d | name=%s | status=%s | conclusion=%s | start=%s | end=%s%n",
                run.workflowName,
                job.id,
                job.name,
                job.status,
                job.conclusion,
                job.startedAt,
                job.completedAt
        );
    }

    private static void printWorkflowEvent(WorkflowRun run) {
        System.out.printf(
                "[WORKFLOW] id=%d | name=%s | branch=%s | sha=%s | status=%s | conclusion=%s | created=%s%n",
                run.id,
                run.workflowName,
                run.branch,
                run.commitSha,
                run.status,
                run.conclusion,
                run.createdAt
        );
    }
}
