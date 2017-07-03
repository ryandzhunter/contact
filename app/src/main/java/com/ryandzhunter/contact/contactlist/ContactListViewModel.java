package com.ryandzhunter.contact.contactlist;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;

import com.ryandzhunter.contact.ILifecycleViewModel;
import com.ryandzhunter.contact.model.Contact;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;

import java.util.List;
import java.util.PrimitiveIterator;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aryandi on 7/1/17.
 */

public class ContactListViewModel extends BaseObservable implements ILifecycleViewModel {

    private GetContactListUseCase useCase;
    public ObservableField<List<Contact>> obsRequestResult = new ObservableField<>();
    public ObservableField<Throwable> obsError = new ObservableField<>();
    public ObservableBoolean isLoading = new ObservableBoolean();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean isContactEmpty;

    public ContactListViewModel(GetContactListUseCase useCase) {
        this.useCase = useCase;
    }

    public void fetchContactList() {
        compositeDisposable.add(useCase.getContactList()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contacts -> {
                    obsRequestResult.set(contacts);
                    if (contacts.size() > 0 ){
                        isContactEmpty = true;
                    } else {
                        isContactEmpty = false;
                    }
                }, throwable -> {
                    {
                        obsError.set(throwable);
                    }
                }));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
    }

    @Bindable
    public boolean isContactEmpty() {
        return isContactEmpty;
    }

    public void setIsContactEmpty(boolean isContactEmpty) {
        this.isContactEmpty = isContactEmpty;
    }
}
