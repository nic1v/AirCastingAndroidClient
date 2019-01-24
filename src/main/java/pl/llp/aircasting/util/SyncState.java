package pl.llp.aircasting.util;

import com.google.inject.Singleton;

@Singleton
public class SyncState {
    private States state = States.NOT_IN_PROGRESS;

    public void markSyncComplete() {
        state = States.NOT_IN_PROGRESS;
    }

    public void startSync() {
        state = States.IN_PROGRESS;
    }

    public synchronized boolean isInProgress() {
        return state == States.IN_PROGRESS;
    }

    public enum States {
        IN_PROGRESS,
        SYNC_POSSIBLE,
        NOT_IN_PROGRESS
    }
}
