package com.demented.cphelps76.powermenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FooterElementGenerator {

    Context context;
    LinearLayout elementView;

    FooterElementGenerator(Context context){
        this.context = context;
    }

    public View getView(Drawable icon){

        elementView = new LinearLayout(context);

        ImageView iconView = new ImageView(context);
        iconView.setImageDrawable(icon);
        iconView.setPaddingRelative(10, 10, 10, 10);

        elementView.addView(iconView);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        elementView.setLayoutParams(param);
        elementView.setGravity(Gravity.CENTER);
        elementView.setPadding(20, 20, 20, 20);
        elementView.setClickable(true);
        elementView.setBackground(ContextCompat.getDrawable(context, R.drawable.action_indicator_oval));

        return elementView;
    }
}
