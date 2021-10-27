package com.bsq.aee;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.bsq.aee.di.component.AppComponent;
import com.bsq.aee.di.component.DaggerAppComponent;
import com.bsq.aee.others.MyTimberDebugTree;
import com.bsq.aee.utils.DialogUtils;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.Getter;
import lombok.Setter;
import timber.log.Timber;

public class MVVMApplication extends Application{
    @Setter
    private AppCompatActivity currentActivity;

    @Getter
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (com.bsq.aee.BuildConfig.DEBUG) {
            Timber.plant(new MyTimberDebugTree());
        }

        appComponent = DaggerAppComponent.builder()
                .application(this)
                .build();
        appComponent.inject(this);

        // Init Toasty
        Toasty.Config.getInstance()
                .allowQueue(false)
                .apply();
    }


    public PublishSubject<Integer> showDialogNoInternetAccess(){
        final PublishSubject<Integer> subject = PublishSubject.create();
        currentActivity.runOnUiThread(() ->
                DialogUtils.dialogConfirm(currentActivity, currentActivity.getResources().getString(R.string.newtwork_error),
                        currentActivity.getResources().getString(R.string.newtwork_error_button_retry),
                        (dialogInterface, i) -> subject.onNext(1), currentActivity.getResources().getString(R.string.newtwork_error_button_exit),
                        (dialogInterface, i) -> System.exit(0))
        );
        return subject;
    }
}
