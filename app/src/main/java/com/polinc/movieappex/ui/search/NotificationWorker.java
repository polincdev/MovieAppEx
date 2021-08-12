package com.polinc.movieappex.ui.search;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NotificationWorker extends Worker {


    Method notMethod;
   public NotificationWorker(
       @NonNull Context context,
       @NonNull WorkerParameters params) {
       super(context, params);
   }



   @Override
   public Result doWork() {


       // Indicate whether the work finished successfully with the Result
     return Result.success();
   }
}
