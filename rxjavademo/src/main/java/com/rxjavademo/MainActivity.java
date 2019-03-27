package com.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "testaa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void clickMe(View v){
        Log.i(TAG,"clickMe() ...");
        Observable.just("one", "two", "three", "four", "five")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG,"onSubscribe() ...");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG,"onNext() ..."+s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG,"onError() ...");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG,"onComplete() ...");
                    }
                });
    }

}
