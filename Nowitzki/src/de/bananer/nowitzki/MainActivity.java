/*
 * @author Philip Frank <ich@philipfrank.de>
 * @description
 */

package de.bananer.nowitzki;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 *
 * @author Philip Frank <ich@philipfrank.de>
 */
public class MainActivity extends Activity {

    private GameView mView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // tell system to use the layout defined in our XML file
        this.setContentView(R.layout.main);


        // get handles to the LunarView from XML, and its LunarThread
        mView = (GameView) this.findViewById(R.id.main);

        // Set full screen view
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

}
