package com.expresso.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by ArunLodhi on 20-05-2015.
 */
public class Express extends Activity implements View.OnClickListener {
    ImageButton btn_gallery;
    ImageView iv;
    LinearLayout lv;
    int i = 1;
    int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.express_screen);
        getWidgetReferences();
        bindWidgetEvents();
        initialization();
    }

    private void getWidgetReferences() {
        btn_gallery = (ImageButton) findViewById(R.id.btn_gallery);
        lv = (LinearLayout) findViewById(R.id.img_container);
        //iv = (ImageView) findViewById(R.id.express_iv1);
        //offset = (-lv.getWidth())/5;
        offset = 200;
    }

    private void bindWidgetEvents() {
        btn_gallery.setOnClickListener(this);
    }

    private void initialization() {
    }

    @Override
    public void onClick(View v) {

        if (v==btn_gallery) {
            animateHide(lv);
            /*if (i==1) {
                iv = (ImageView) findViewById(R.id.express_iv1);
                animateHide(iv);
                i+=1;
            }
            else if (i==2) {
                iv = (ImageView) findViewById(R.id.express_iv2);
                animateHide(iv);
                i+=1;
            }
            else if (i==3) {
                iv = (ImageView) findViewById(R.id.express_iv3);
                animateHide(iv);
                i+=1;
            }
            else if (i==4) {
                iv = (ImageView) findViewById(R.id.express_iv4);
                animateHide(iv);
                i+=1;
            }
            else if (i==5) {
                iv = (ImageView) findViewById(R.id.express_iv5);
                animateHide(iv);
                i+=1;
            }*/
        }
    }

    public void animateHide(View abc) {
        TranslateAnimation animate = new TranslateAnimation(0,offset,0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        abc.startAnimation(animate);
        //abc.setVisibility(View.GONE);
    }
}
