package ducere.lechal.pod.constants;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import ducere.lechal.pod.ble.ActionsToService;

/**
 * Created by sunde on 25-04-2016.
 */
public class Constants {
    /**
     * never change
     */
    public static final int NOTIFICATION_ID = 12706;
    public static final String NOTIFICATION_TEXT_FORMAT = "Goal %s : Battery %s";
    public static final String CURRENT_LOCATION_FORMAT = "%s\n%s";

    public static RotateAnimation getRotateAnimation() {
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(2000);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        return rotate;
    }

    public static AlphaAnimation getAlphaAnimation() {
        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(2000);
        fadeOut.setStartOffset(1000);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        return fadeOut;
    }

    public static void sendVibrate(Context context, String pattern) {
        Intent intent = new Intent(ActionsToService.VIBRATE);
        intent.putExtra(ActionsToService.VIBRATE, pattern);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
