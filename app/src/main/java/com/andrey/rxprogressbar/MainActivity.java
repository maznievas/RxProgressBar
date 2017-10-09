package com.andrey.rxprogressbar;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    final String TAG = "mLog";

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Observable<Object> firstObs = Observable.just(new Object());
        final Observable<Object> progressObs = Observable.just(new Object());

        compositeDisposable = new CompositeDisposable();

        progressBar.setVisibility(View.GONE);

        Disposable d = RxView.clicks(button)
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        button.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        Log.d(TAG, Thread.currentThread().getName() + "accept: PB is visible");
                    }
                })
                .flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Object o) throws Exception {
                        return Observable.just(new Object())
                                .doOnNext(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object o) throws Exception {
                                        doSmth();
                                    }
                                })
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.d(TAG, "OK");
                        button.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "Error");
                    }
                });

    compositeDisposable.add(d);
//        Observable.just(new Object())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        Log.d(TAG, "Visible");
//                        progressBar.setVisibility(View.VISIBLE);
//                    }
//                })
//                .doOnTerminate(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        Log.d(TAG, "Invisible");
//                        progressBar.setVisibility(View.VISIBLE);
//                    }
//                })
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        Log.d(TAG, "OK");
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Log.d(TAG, "Empty Error");
//                        throwable.printStackTrace();
//                    }
//                });

//        progressObs
//                .map(new Function<Object, Object>() {
//                    @Override
//                    public Object apply(@NonNull Object o) throws Exception {
//                        Log.d(TAG, Thread.currentThread().getName() + " prBar is visible");
//                        return null;
//                    }
//                });
//
//        firstObs
//                .map(new Function<Object, Boolean>() {
//                    @Override
//                    public Boolean apply(@NonNull Object o) throws Exception {
//                        return doSmth();
//                    }
//                })
//                .subscribeOn(Schedulers.computation());
//
//        Observable.merge(progressObs, firstObs)
//                .doOnComplete(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                         Log.d(TAG, Thread.currentThread().getName() + " prBar invisible");
//                    }
//                })
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        throwable.printStackTrace();
//                    }
//                });
//        progressObs
//                .flatMap(new Function<Object, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(@NonNull Object o) throws Exception {
//                        return Observable.just(new Object())
//                                .map(new Function<Object, Object>() {
//                                    @Override
//                                    public Object apply(@NonNull Object o) throws Exception {
//                                        doSmth();
//                                        return null;
//                                    }
//                                })
//                                .subscribeOn(Schedulers.computation());
//                    }
//                })
//                .unsubscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread()   )
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        Log.d(TAG, Thread.currentThread().getName() + " prBar is invisible");
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                });

//        progressObs.flatMap(new Function<Object, ObservableSource<?>>() {
//            @Override
//            public ObservableSource<?> apply(@NonNull Object o) throws Exception {
//                Log.d(TAG, Thread.currentThread().getName() + " progressBar is visible");
//                return Observable.empty()
//                        .doOnNext(new Consumer<Object>() {
//                            @Override
//                            public void accept(Object o) throws Exception {
//                                doSmth();
//                            }
//                        })
//                        .subscribeOn(Schedulers.computation());
//            }
//        })
//                .doOnComplete(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                       // Log.d(TAG, Thread.currentThread().getName() + " progressBar is invisible");
//                    }
//                })
////                .subscribeOn(AndroidSchedulers.mainThread())
//                //.observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        Log.d(TAG, Thread.currentThread().getName() + " progressBar is invisible");
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        throwable.printStackTrace();
//                    }
//                });

//        firstObs.map(new Function<Integer, String>() {
//            @Override
//            public String apply(@NonNull Integer integer) throws Exception {
//                Log.d(TAG, Thread.currentThread().getName() + " map.apply()");
//                return integer.toString();
//            }
//
//        })
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnComplete(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        Log.d(TAG,  Thread.currentThread().getName() + " onComplete()");
//                    }
//                })
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        Log.d(TAG, s);
//                    }
//                });
    }

    public Boolean doSmth() throws InterruptedException {
        Log.d(TAG, Thread.currentThread().getName() + " sleeping thread");
        Thread.sleep(3666);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }
}
