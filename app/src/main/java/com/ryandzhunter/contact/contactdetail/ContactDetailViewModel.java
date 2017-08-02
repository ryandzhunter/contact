package com.ryandzhunter.contact.contactdetail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import android.net.Uri;

import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ryandzhunter.contact.ILifecycleViewModel;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.addcontact.AddContactActivity;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;
import com.ryandzhunter.contact.util.Preferences;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by aryandi on 7/4/17.
 */

public class ContactDetailViewModel extends BaseObservable implements ILifecycleViewModel {

    private final Context context;
    private final GetContactListUseCase useCase;
    private final Preferences pref;
    private ContactDetailView view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    ObservableField<Throwable> obsError = new ObservableField<>();
    ObservableBoolean isLoading = new ObservableBoolean();
    ObservableBoolean isFavourite = new ObservableBoolean();
    private Contact contact;
    public ObservableField<String> mFullName = new ObservableField<>();
    public ObservableField<String> mPhoneNumber = new ObservableField<>();
    public ObservableField<String> mEmail = new ObservableField<>();
    public ObservableField<String> mProfilePic = new ObservableField<>();

    private ClipData myClip;
    private ClipboardManager myClipboard;
    private int id;

    public ContactDetailViewModel(Context context, GetContactListUseCase useCase, ContactDetailView view, Preferences pref) {
        this.context = context;
        this.useCase = useCase;
        this.view = view;
        this.pref = pref;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
    }

    public Contact getContact() {
        return contact;
    }

    public void fetchContactDetail(int id) {
        this.id = id;
        compositeDisposable.add(useCase.getAPIContactDetail(id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contact -> {
                    onSuccessFetchContactDetail(contact);
                }, throwable -> obsError.set(throwable)));
    }

    private void onSuccessFetchContactDetail(Contact contact) {
        this.contact = contact;
        mFullName.set(contact.firstName + " " + contact.lastName);
        mEmail.set(contact.email);
        mPhoneNumber.set(contact.phoneNumber);
        mProfilePic.set(contact.profilePic);
        isFavourite.set(contact.favorite);
    }

    @BindingAdapter(value = {"bind:imageUrl"}, requireAll = false)
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_betty_allen)
                .error(R.drawable.ic_betty_allen)
                .centerCrop()
                .into(imageView);
    }

    public void onPhoneClick() {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.phoneNumber));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void onPhoneNumberClick() {
        myClip = ClipData.newPlainText("text", contact.phoneNumber);
        if (myClipboard == null)
            myClipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(context, contact.phoneNumber, Toast.LENGTH_SHORT).show();
    }

    public void onEmailClick() {
        myClip = ClipData.newPlainText("text", contact.email);
        if (myClipboard == null)
            myClipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(context, contact.email, Toast.LENGTH_SHORT).show();
    }

    public void onMessageClick() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contact.phoneNumber));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void onIconEmailClick() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + contact.email));
        context.startActivity(Intent.createChooser(emailIntent, "Send email"));
    }

    public void onDeleteClick() {
        deleteContactToAPI(id);
    }

    void onEditClick() {
        AddContactActivity.openEditContactActivity(context, contact);
    }

    void onFavouriteClick() {
        if (contact.favorite != null) {
            contact.favorite = !contact.favorite;
            updateContactToAPI(id, contact);
        }
    }

    public void deleteContactToAPI(int id) {
        useCase.deleteContactToAPI(id)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable ->  isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(() -> {
                    Timber.d("on Deleted Contact Success");
                    deleteCachedContact(contact);
                }, throwable -> obsError.set(throwable));
    }

    public void deleteCachedContact(Contact contact){
        useCase.deleteCachedContact(contact)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable ->  isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(() -> {
                    Timber.d("on Deleted Cached Contact Success");
                    view.closeActivity();
                    pref.setIsShouldReloadList(true);
                }, throwable -> obsError.set(throwable));
    }

    // tp update favorite contact
    public void updateContactToAPI(int id, Contact contact) {
        compositeDisposable.add(useCase.updateContactToAPI(id, contact)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable ->  isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contactResult -> {
                    onSuccessUpdateContactToAPI(contactResult);
                }, throwable -> obsError.set(throwable)));
    }

    private void onSuccessUpdateContactToAPI(Contact contact) {
        this.contact = contact;
        isFavourite.set(contact.favorite);
        updateCachedContact(contact);
    }

    public void updateCachedContact(Contact contact) {
        useCase.updateCachedContact(contact)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable ->  isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(() -> {
                    Timber.d("onComplete - successfully update cache contact");
                    pref.setIsShouldReloadList(true);
                }, throwable -> obsError.set(throwable));
    }

}
