package com.polinc.movieappex.ui.prods;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.StorageReference;
import com.polinc.movieappex.main.MyApplication;
import com.polinc.movieappex.models.Movie;
import com.polinc.movieappex.models.MoviesWraper;
import com.polinc.movieappex.net.MoviesGetController;
import com.polinc.movieappex.ui.movies.MoviesActivity;
import com.polinc.movieappex.ui.movies.SingleLiveEvent;

import java.util.ArrayList;

import io.reactivex.Scheduler;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Cancellable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ProdViewModel extends ViewModel {

    boolean  fetched=false;


        private PublishSubject< Bitmap  > imgsData;
        private SingleLiveEvent<Integer> resultStatus;
        // ArrayList<Movie> movies;
        public int currentPage=0;

       String[] imgs={"cub_red.jpg","cubs_blue.jpg","cubs_green.jpg","cubs_red.jpg",
      "glass.jpg","cub_blue.jpg","cub_green.jpg"};



   Fragment fragment;

        public ProdViewModel(Fragment fragment )
        {
            this.fragment=fragment;
            //
             imgsData =PublishSubject.create();
            resultStatus = new SingleLiveEvent<>();
            System.out.println("ProdViewModel=");
        }

        public PublishSubject<  Bitmap  > getImageData() {
            System.out.println("getImageData="+currentPage);
            return imgsData;
        }
        public LiveData<Integer> getResultStatus() {
            return resultStatus;
        }


        public void fetchImages()
        {

            if(fetched)
               return;
             fetched=true;
             ArrayList<Observable> newObs = new ArrayList<Observable>();

             for(int a=0;a<imgs.length;a++) {
                 System.out.println("STaT="+imgs.length+"="+a);
                 int finalA = a;
                 Observable<Bitmap> observable=   Observable.create(new ObservableOnSubscribe<Bitmap>() {
                         @Override
                         public void subscribe(ObservableEmitter<Bitmap> emitter) throws Exception {
                             System.out.println("islfdfandRef="+"prods/" + imgs[finalA]);
                             StorageReference islandRef = ((ProdFragment) fragment).storageRef.child("prods/" + imgs[finalA]);
                             final long ONE_MEGABYTE = 1024 * 1024;
                              Task<byte[]> downTask= islandRef.getBytes(ONE_MEGABYTE);

                             System.out.println("sssrrt="+downTask);
                             downTask.addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                 @Override
                                 public void onSuccess(byte[] bytes) {
                                     Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    // newItems.add(bitmap);
                                     emitter.onNext(bitmap);
                                     emitter.onComplete();
                                     System.out.println("onSuccess="+bytes.length);
                                 }
                             }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {
                                     System.out.println("onFailure=" );
                                    e.printStackTrace();
                                 }
                             });
                         }
                     });
                 System.out.println(" newObs.add="+observable);
                 newObs.add(observable);

             }

            System.out.println(" newObs="+newObs.size());

             //Merge observables
             Observable.mergeArrayDelayError( newObs.toArray(new Observable[imgs.length]) )
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Observer(){
                         @Override
                         public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                         }

                         @Override
                         public void onNext(@NonNull Object o) {
                             System.out.println(" onNext="+o);
                             onImagesFetchAddSuccess(o);
                         }

                         @Override
                         public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                             onImagesFetchFailed(e);
                         }

                         @Override
                         public void onComplete() {
                             System.out.println(" onComplete=" );
                         }
                     });


        }


    void onImagesFetchAddSuccess(Object o )
        {


          //  imgsData.setValue((Bitmap)o);
            imgsData.onNext((Bitmap)o);
            System.out.println("onImagesFetchAddSuccess="+o);
        }

    private void onImagesFetchFailed(Throwable e) {

            e.printStackTrace();
            resultStatus.setValue(-1);
        }
}