package com.ryandzhunter.contact.contactlist;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.ryandzhunter.contact.ILifecycleViewModel;
import com.ryandzhunter.contact.R;
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

    void fetchContactList() {
        compositeDisposable.add(useCase.getContactList()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contacts -> {
                    obsRequestResult.set(contacts);
                    isContactEmpty = contacts.size() == 0;
                }, throwable -> obsError.set(throwable)));
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

    public String titleBar(){
        return title;
    }

    void setTitleBar(String title){
        this.title = title;
    }

    public Drawable iconLeft(){
        return ContextCompat.getDrawable(context, R.drawable.ic_menu);
    }

    public Drawable iconRight(){
        return ContextCompat.getDrawable(context, R.drawable.ic_search);
    }
}
