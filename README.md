# ActionWatch

## Project Description

**ActionWatch** is a lightweight CLI tool for monitoring GitHub Actions workflows in real time.  
It periodically polls the GitHub Actions API for a given repository and reports **new workflow run, job, and step events**, tracking their lifecycle from start to completion.

The tool is stateful: it persists information about previously processed events and guarantees that **only new events are reported** between runs. When restarted for the same repository, ActionWatch resumes monitoring from the last processed state.

---

## Technology Stack

- Java 11+
- OkHttp (HTTP client)
- GitHub REST API (Actions API)
- org.json (JSON parsing)
- Jackson (ObjectMapper for state persistence)
- Gradle
- Command Line Interface (CLI)

---

## What the Project Covers

- Integration with an external REST API (GitHub)
- Periodic polling and event monitoring
- Persistent state management between application runs
- CLI-based application design
- Separation of concerns (client, domain, state, core logic)
- Basic CI/CD observability concepts

---

## Key Features

- Monitor GitHub Actions workflows for a specific repository
- Track workflow runs, jobs, and job steps
- Report workflow/job lifecycle events:
    - `queued`
    - `in_progress`
    - `completed`
- Persist state between runs to avoid duplicate event reporting
- Resume monitoring from the last processed event
- Graceful shutdown with automatic state saving
- Minimal dependencies and simple setup

---


## Setup and Run

### Prerequisites

- Java 11 or higher
- GitHub Personal Access Token with permissions:
  - `repo`
  - `actions:read`

### Build

```bash
./gradlew clean build
```

### Run
```bash
java -jar build/libs/ActionWatch.jar <owner/repo> <github_token>
```



