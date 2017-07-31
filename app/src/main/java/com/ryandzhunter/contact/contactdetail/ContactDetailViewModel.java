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

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by aryandi on 7/4/17.
 */

public class ContactDetailViewModel extends BaseObservable implements ILifecycleViewModel {

    private final Context context;
    private final GetContactListUseCase useCase;
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

    public ContactDetailViewModel(Context context, GetContactListUseCase useCase, ContactDetailView view) {
        this.context = context;
        this.useCase = useCase;
        this.view = view;
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
        compositeDisposable.add(useCase.getContactDetail(id)
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
        deleteContact(id);
    }

    public void deleteContact(int id) {
        useCase.deleteContact(id)
                .doOnSubscribe(disposable ->  isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                Timber.d("on Deleted Contact Success");
                view.closeActivity();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Timber.d("on Deleted Contact Error");
            }
        });
    }

    void onEditClick() {
        AddContactActivity.openEditContactActivity(context, contact);
    }

    void onFavouriteClick() {
        if (contact.favorite != null) {
            contact.favorite = !contact.favorite;
            updateContact(id, contact);
        }
    }

    public void updateContact(int id, Contact contact) {
        isLoading.set(true);
        compositeDisposable.add(useCase.updateContact(id, contact)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe(contactResult -> {
                    onSuccessUpdateContact(contactResult);
                }, throwable -> obsError.set(throwable)));
    }

    private void onSuccessUpdateContact(Contact contact) {
        this.contact = contact;
        isFavourite.set(contact.favorite);
        updateCachedContact(contact);
    }

    public void updateCachedContact(Contact contact) {
        useCase.updateCachedContact(contact);
    }

}
