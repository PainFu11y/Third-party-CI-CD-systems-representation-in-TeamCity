package com.actionwatch.state;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.actionwatch.util.JsonUtil;

public class MonitorState {

    private static final String STATE_FILE = ".actionwatch-state.json";

    public Map<String, RepoState> repos = new HashMap<>();

    public static MonitorState load() {
        try {
            File f = new File(STATE_FILE);
            if (!f.exists()) return new MonitorState();
            return JsonUtil.MAPPER.readValue(f, MonitorState.class);
        } catch (IOException e) {
            return new MonitorState();
        }
    }

    public void save() {
        try {
            JsonUtil.MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(STATE_FILE), this);
        } catch (IOException ignored) {}
    }

    public RepoState getRepo(String repo) {
        return repos.computeIfAbsent(repo, r -> new RepoState());
    }
}
