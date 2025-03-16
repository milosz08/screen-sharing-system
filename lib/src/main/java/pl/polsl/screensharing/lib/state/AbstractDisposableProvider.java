package pl.polsl.screensharing.lib.state;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

@Slf4j
public abstract class AbstractDisposableProvider {
    private final ConcurrentMap<String, Disposable> subscriptionsPool;

    public AbstractDisposableProvider() {
        subscriptionsPool = new ConcurrentHashMap<>();
    }

    public <T> void wrapAsDisposable(Observable<T> subject, Consumer<T> consumer) {
        final Disposable disposable = subject.subscribe(consumer::accept);
        subscriptionsPool.put(UUID.randomUUID().toString(), disposable);
    }

    public void disposeAllSubscriptions() {
        for (final Map.Entry<String, Disposable> entry : subscriptionsPool.entrySet()) {
            entry.getValue().dispose();
            log.debug("Dispose subscription: {}", entry.getKey());
        }
        log.info("Disposed all subscriptions: {}", subscriptionsPool.size());
    }
}
