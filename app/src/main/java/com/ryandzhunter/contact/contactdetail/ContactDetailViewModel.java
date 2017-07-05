package com.ryandzhunter.contact.contactdetail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.view.Menu;
import android.widget.Toast;

import com.ryandzhunter.contact.ILifecycleViewModel;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by aryandi on 7/4/17.
 */

public class ContactDetailViewModel extends BaseObservable implements ILifecycleViewModel {

    private final Context context;
    private final GetContactListUseCase useCase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    ObservableField<Throwable> obsError = new ObservableField<>();
    ObservableBoolean isLoading = new ObservableBoolean();
    private Contact contact;
    public ObservableField<String> mFullName = new ObservableField<>();
    public ObservableField<String> mPhoneNumber = new ObservableField<>();
    public ObservableField<String> mEmail = new ObservableField<>();
    private ClipData myClip;
    private ClipboardManager myClipboard;

    public ContactDetailViewModel(Context context, GetContactListUseCase useCase) {
        this.context = context;
        this.useCase = useCase;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
    }

    public void fetchContactDetail(int id) {
        compositeDisposable.add(useCase.getContactDetail(id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contact -> {
                    this.contact = contact;
                    mFullName.set(contact.firstName + " " + contact.lastName);
                    mEmail.set(contact.email);
                    mPhoneNumber.set(contact.phoneNumber);
                }, throwable -> obsError.set(throwable)));
    }

    public String getTitle() {

        return contact != null ? contact.firstName + " " + contact.lastName : "";
    }

    public void onPhoneClick(){
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.phoneNumber));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void onPhoneNumberClick(){
        myClip = ClipData.newPlainText("text", contact.phoneNumber);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(context, contact.phoneNumber, Toast.LENGTH_SHORT).show();
    }

    public void onEmailClick(){
        myClip = ClipData.newPlainText("text", contact.email);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(context, contact.email, Toast.LENGTH_SHORT).show();
    }

    public void onMessageClick(){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contact.phoneNumber));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void onIconEmailClick(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"+contact.email));
        context.startActivity(Intent.createChooser(emailIntent, "Send email"));
    }

    public void onShareMenuClick(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                contact.phoneNumber);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public void deleteContact() {

    }

    public void clickEdit() {

    }

    public void updateContact(Menu menu) {

    }

    private void setFavouriteIcon(Menu menu) {
        if (contact.favorite) {
            menu.getItem(0).setIcon(context.getResources().getDrawable(R.drawable.ic_favourite_filled));
        } else {
            menu.getItem(0).setIcon(context.getResources().getDrawable(R.drawable.ic_favourite));
        }
    }
}
