package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений
 * ограниченного размера)
 */
public class LogWindowSource {
    private final int iQueueLength;

    private final LinkedBlockingQueue<LogEntry> messages;
    private final ArrayList<LogChangeListener> listeners;
    private volatile LogChangeListener[] activeListeners;

    public LogWindowSource(int iQueueLength) {
        this.iQueueLength = iQueueLength;
        listeners = new ArrayList<>();
        messages = new LinkedBlockingQueue<>(iQueueLength);
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
            activeListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        if (!messages.offer(entry)) {
            messages.poll();
            messages.offer(entry);
        }
        LogChangeListener[] activeListeners = this.activeListeners;
        if (activeListeners == null) {
            synchronized (listeners) {
                if (this.activeListeners == null) {
                    activeListeners = listeners.toArray(new LogChangeListener[0]);
                    this.activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }

    public int size() {
        synchronized (messages) {
            return messages.size();
        }
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        var size = messages.size();
        if (startFrom < 0 || startFrom >= size) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, size);
        return messages.stream().toList().subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all() {
        return messages;
    }
}
