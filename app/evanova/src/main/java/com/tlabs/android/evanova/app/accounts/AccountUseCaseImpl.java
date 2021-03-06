package com.tlabs.android.evanova.app.accounts;

import com.tlabs.android.evanova.app.accounts.AccountUseCase;
import com.tlabs.android.evanova.content.ContentFacade;
import com.tlabs.android.evanova.content.ContentPublisher;
import com.tlabs.android.jeeves.model.EveAccount;
import com.tlabs.android.jeeves.service.events.EveAccountUpdateEndEvent;
import com.tlabs.android.jeeves.service.events.EveAccountUpdateStartEvent;
import com.tlabs.android.jeeves.service.events.EveApiEvent;
import com.tlabs.android.jeeves.service.events.EveApiKeyImportEndEvent;
import com.tlabs.android.jeeves.service.events.EveApiKeyImportStartEvent;
import com.tlabs.android.jeeves.service.events.EveAuthCodeImportEndEvent;
import com.tlabs.android.jeeves.service.events.EveAuthCodeImportStartEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class AccountUseCaseImpl implements AccountUseCase {

    private final ContentFacade content;
    private final ContentPublisher receiver;

    @Inject
    public AccountUseCaseImpl(
            ContentFacade content,
            ContentPublisher receiver) {
        this.content = content;
        this.receiver = receiver;
    }

    @Override
    public List<Long> listAccounts() {
        return this.content.listAccounts();
    }

    public List<EveAccount> loadAccounts() {
        final List<Long> ids = content.listAccounts();
        final List<EveAccount> accounts = new ArrayList<>(ids.size());
        for (Long id: ids) {
            accounts.add(content.getAccount(id));
        }
        return accounts;
    }

    public void importAuthCode(String authCode, Observer<EveAccount> observer) {
        if (null != observer) {
            this.receiver.getObservable().subscribe(new Subscriber<EveApiEvent>() {
                @Override
                public void onCompleted() {
                    observer.onCompleted();
                    unsubscribe();
                }

                @Override
                public void onError(Throwable e) {
                    observer.onError(e);
                }

                @Override
                public void onNext(EveApiEvent event) {
                    if (!(event instanceof EveAuthCodeImportEndEvent)) {
                        return;
                    }

                    final EveAuthCodeImportEndEvent endEvent = (EveAuthCodeImportEndEvent)event;
                    if (endEvent.getAuthenticated()) {
                        observer.onNext(content.getAccount(endEvent.getAccountId()));
                    }
                    else {
                        observer.onNext(null);
                    }
                    onCompleted();
                }
            });
        }
        this.receiver.publish(new EveAuthCodeImportStartEvent(authCode));
    }

    public void importApiKey(long apiId, String apiKey, String name, Observer<EveAccount> observer) {
        if (null != observer) {
            this.receiver.getObservable().subscribe(new Subscriber<EveApiEvent>() {
                @Override
                public void onCompleted() {
                    observer.onCompleted();
                    unsubscribe();
                }

                @Override
                public void onError(Throwable e) {
                    observer.onError(e);
                }

                @Override
                public void onNext(EveApiEvent event) {
                    if (!(event instanceof EveApiKeyImportEndEvent)) {
                        return;
                    }

                    final EveApiKeyImportEndEvent endEvent = (EveApiKeyImportEndEvent)event;
                    if (endEvent.getAccounts().isEmpty()) {
                        observer.onNext(null);
                    }
                    else {
                        for (Long id : endEvent.getAccounts()) {
                            observer.onNext(content.getAccount(id));
                        }
                    }
                    onCompleted();
                }
            });
        }
        this.receiver.publish(new EveApiKeyImportStartEvent(apiId, apiKey, name));
    }

    @Override
    public void reloadAccount(long id, Observer<EveAccount> observer) {
        if (null != observer) {
            this.receiver.getObservable().subscribe(new Subscriber<EveApiEvent>() {
                @Override
                public void onCompleted() {
                    observer.onCompleted();
                    unsubscribe();
                }

                @Override
                public void onError(Throwable e) {
                    observer.onError(e);
                }

                @Override
                public void onNext(EveApiEvent event) {
                    if (!(event instanceof EveAccountUpdateEndEvent)) {
                        return;
                    }
                    observer.onNext(content.getAccount(id));
                    onCompleted();
                }
            });
        }
        this.receiver.publish(new EveAccountUpdateStartEvent(id, true));
    }

    @Override
    public void deleteAccount(long id, Observer<EveAccount> observer) {
        subscribe(() -> {
            final EveAccount account = content.getAccount(id);
            if (null != account) {
                content.deleteAccount(id);
            }
            return account;
        },
        account -> {
            if (null == observer) {
                return;
            }
            observer.onNext(account);
            observer.onCompleted();
        });

    }

    private static <T> Subscription subscribe(Func0<T> f, Action1<T> action1) {
        return defer(f).subscribe(action1::call);
    }

    private static <T> Observable<T> defer(Func0<T> f) {
        final Observable<T> observable =
                Observable.defer(() -> Observable.fromCallable(f));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

}
