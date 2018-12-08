package dk.au.itsmap.group4.crispy.service.notification;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LifecycleService;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.model.IRepository;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * TODO: Remove this class, if we will not use it! It is not possible to use LiveData from background service
 */

/**
 * This service needs to be normal service not IntenetService, because it has to do async tasks
 * Solution how to handle requests in background serviceInspired by documentation:
 * https://developer.android.com/guide/components/services#ExtendingService
 */
public class NotificationService extends LifecycleService {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {

        LifecycleService mService;

        public ServiceHandler(Looper looper, LifecycleService service) {
            super(looper);
            mService = service;
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d("NOTIFICATION_SERVICE", "handleMessage before");

            IRepository repository = FSRepository.getInstance();
            repository.getAllMeals().observe(mService, meals -> {
                Log.d("NOTIFICATION_SERVICE", "handleMessage received");
            });

            Log.d("NOTIFICATION_SERVICE", "handleMessage after");

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("NOTIFICATION_SERVICE", "onCreate");


        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("NOTIFICATION_SERVICE", "onStartCommand");

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);


        LifecycleService service = this;

        Runnable r = new MyRunnable(this);
        new Thread(r).start();

        // If we get killed, after returning from here, restart
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        super.onBind(intent);
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("NOTIFICATION_SERVICE", "onDestroy");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    public class MyRunnable implements Runnable {

        Service mService;

        public MyRunnable(Service service) {
            mService = service;
        }

        public void run() {
            Log.d("NOTIFICATION", "thread");
//            IRepository repository = FSRepository.getInstance();
//            repository.getAllMeals().observe((LifecycleService)mService, meals -> {
//                Log.d("NOTIFICATION_SERVICE", "handleMessage received");
//                mService.stopSelf();
//            });
        }
    }
}
