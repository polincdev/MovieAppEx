package com.polinc.movieappex.ui.prods;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class FirebaseUploader {
    private Context appContext;
    private FirebaseStorage storage;

    public FirebaseUploader(Context appContext) {
        this.appContext = appContext;
        this.storage = FirebaseStorage.getInstance();
    }

    // Uploading a single file
    public Observable<String> send(Uri file) {
        return toObservable(Observable.just(file));
    }

    // Uploading multiple files
    public Observable<String> send(List<Uri> files) {
        return toObservable(Observable.fromIterable(files));
    }

    private Observable<String> toObservable(Observable<Uri> observable) {
        return observable.flatMap(this::upload);
    }

    private Observable<String> upload(Uri file) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                StorageReference branch = storage.getReference().child("images");
                StorageReference leaf = branch.child(generateUniqueId(file));
                UploadTask uploadTask = leaf.putFile(file);

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) emitter.onError(task.getException());
                        return leaf.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String url = downloadUri.toString();
                            emitter.onNext(url);
                            //emitter.onComplete();
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            }
        });
    }

    private String generateUniqueId(Uri file) {
        String fileType = appContext.getContentResolver().getType(file);
        String extension = (fileType.equals("image/jpeg")) ? ".jpg" : ".png";
        String fileName = UUID.randomUUID().toString();
        return fileName + extension;
    }
}