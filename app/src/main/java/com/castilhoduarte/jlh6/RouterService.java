package com.castilhoduarte.jlh6;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

public final class RouterService extends Service {

    public static void start(Context ctx) {
        ctx.startService(new Intent(ctx, RouterService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RouterManager mgr = RouterManager.get();
        mgr.restoreIfEnabled(this);
        scheduleStop(mgr, startId);
        return START_NOT_STICKY;
    }

    private void scheduleStop(RouterManager mgr, int startId) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override public void run() {
                if (mgr.getState() == RouterManager.State.STARTING) {
                    new Handler(Looper.getMainLooper()).postDelayed(this, 1_000);
                } else {
                    stopSelf(startId);
                }
            }
        }, 1_000);
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}
