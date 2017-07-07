package com.ryandzhunter.contact;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.ryandzhunter.contact.dagger.component.AppComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aryandi on 7/1/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private List<ILifecycleViewModel> viewModels;
    protected ProgressDialog mProgressDialog;
    private AlertDialog.Builder mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate content view
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View contentView = getLayoutInflater().inflate(getLayoutResourceId(), root, false);
        setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        getIntentExtra();
        setupDataBinding(contentView);
    }

    protected abstract
    @LayoutRes
    int getLayoutResourceId();

    protected abstract void setupDataBinding(View contentView);

    protected abstract void getIntentExtra();

    protected AppComponent getAppComponent() {
        ContactApp application = (ContactApp) getApplication();
        return application.getAppComponent();
    }

    @Override
    protected void onDestroy() {
        destroyViewModels();
        super.onDestroy();
    }

    private void destroyViewModels() {
        if (viewModels == null) return;
        for (ILifecycleViewModel vm : viewModels) {
            vm.onDestroy();
        }
    }

    protected void addViewModel(ILifecycleViewModel vm) {
        if (viewModels == null) {
            viewModels = new ArrayList<>();
        }
        if (!viewModels.contains(vm)) {
            viewModels.add(vm);
        }
    }

    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("");
        mProgressDialog.setMessage(message);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void showAlertDialog(Context context, String title, String msg){
        if (mAlertDialog == null) mAlertDialog = new AlertDialog.Builder(context);
        mAlertDialog.setTitle(title);
        mAlertDialog.setMessage(msg);
        mAlertDialog.setPositiveButton("OK", (dialog, which) -> {
        });
    }

}
