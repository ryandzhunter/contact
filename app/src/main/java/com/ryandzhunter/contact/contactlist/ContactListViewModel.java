package com.ryandzhunter.contact.contactlist;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.ryandzhunter.contact.ILifecycleViewModel;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.addcontact.AddContactActivity;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by aryandi on 7/1/17.
 */

public class ContactListViewModel extends BaseObservable implements ILifecycleViewModel {

    private Context context;
    private GetContactListUseCase useCase;
    ObservableField<List<Contact>> obsRequestResult = new ObservableField<>();
    ObservableField<Throwable> obsError = new ObservableField<>();
    ObservableBoolean isLoading = new ObservableBoolean();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean isContactEmpty;
    private String title;

    public ContactListViewModel(Context context, GetContactListUseCase useCase) {
        this.context = context;
        this.useCase = useCase;
    }

    public void getCachedContactList() {
        isLoading.set(true);
        compositeDisposable.add(useCase.getCachedContactList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    isLoading.set(false);
                    if (contacts.size() > 0) {
                        onSuccess(contacts);
                    } else {
                        fetchContactList();
                    }
                }, throwable -> obsError.set(throwable)));
    }

    public void deleteAllCachedContact(){
        useCase.deleteAllCachedContact()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete - successfully deleted all contact");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.d("onError - failed:", e);
                    }
                });
    }

    public void fetchContactList() {
        compositeDisposable.add(useCase.getAPIContactList()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contacts -> {
                        saveCachedContact(contacts);
                }, throwable -> obsError.set(throwable)));
    }

    private void onSuccess(List<Contact> contacts) {
        obsRequestResult.set(contacts);
        isContactEmpty = contacts.size() == 0;
    }

    public void saveCachedContact(List<Contact> contact) {
        useCase.saveMultipleListCachedContact(contact)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete - successfully added contact");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("onError - add:", e);
                    }
                });
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
    }

    @Bindable
    public boolean isContactEmpty() {
        return isContactEmpty;
    }

    public String titleBar() {
        return title;
    }

    void setTitleBar(String title) {
        this.title = title;
    }

    public Drawable iconLeft() {
        return ContextCompat.getDrawable(context, R.drawable.ic_menu);
    }

    public Drawable iconRight() {
        return ContextCompat.getDrawable(context, R.drawable.ic_search);
    }

    public void onFabAddClick(){
        AddContactActivity.openAddContactActivity(context);
    }

}
