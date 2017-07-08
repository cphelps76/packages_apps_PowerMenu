package com.demented.cphelps76.powermenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PowerMenuActivity extends Activity {

    LinearLayout viewFooterElements;
    View shutdown, reboot, flightMode;
    View silent, vibrate, normal;

    FooterElementGenerator footerElementGenerator;
    Actions actions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.power_menu_activity);

        actions = new Actions(this);

        addMainElements();
        addFooterElements();
    }

    private void addMainElements() {
        shutdown = findViewById(R.id.shutdown_container);
        reboot = findViewById(R.id.reboot_container);
        flightMode = findViewById(R.id.flightMode_container);

        ImageView shutDownView = findViewById(R.id.shutdown_icon);
        Drawable shutDownIcon = ContextCompat.getDrawable(this,
                R.drawable.ic_power_settings);
        shutDownView.setImageDrawable(shutDownIcon);
        shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.shutdown();
            }
        });

        ImageView rebootView = findViewById(R.id.reboot_icon);
        Drawable rebootIcon = ContextCompat.getDrawable(this,
                R.drawable.ic_loop);
        rebootView.setImageDrawable(rebootIcon);
        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog();
            }
        });

        Drawable flightOffIcon = ContextCompat.getDrawable(this,
                R.drawable.ic_airplanemode_inactive);
        Drawable flightOnIcon = ContextCompat.getDrawable(this,
                R.drawable.ic_airplanemode_active);

        String flightModeOn = getResources().getString(R.string.flightMode_on);
        String flightModeOff = getResources().getString(R.string.flightMode_off);

        ImageView flightModeIcon = findViewById(R.id.flightMode_icon);
        TextView flightModeText = findViewById(R.id.flightMode_message);

        if (actions.isFlightModeOn()) {
            flightModeIcon.setImageDrawable(flightOnIcon);
            flightModeText.setText(flightModeOn);
            flightMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actions.flightModeToggle();
                    PowerMenuActivity.this.finish();
                }
            });
        } else {
            flightModeIcon.setImageDrawable(flightOffIcon);
            flightModeText.setText(flightModeOff);
            flightMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actions.flightModeToggle();
                    PowerMenuActivity.this.finish();
                }
            });
        }
    }

    private void setDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(PowerMenuActivity.this);
        builderSingle.setIcon(R.drawable.ic_loop);
        builderSingle.setTitle(getResources().getString(R.string.reboot_option));

        String rebootSystem = getResources().getString(R.string.reboot_system);
        String rebootRecovery = getResources().getString(R.string.reboot_recovery);
        String rebootBootloader = getResources().getString(R.string.reboot_bootloader);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                PowerMenuActivity.this, R.layout.select_dialog_item);
        arrayAdapter.add(rebootSystem);
        arrayAdapter.add(rebootRecovery);
        arrayAdapter.add(rebootBootloader);

        String cancel = getResources().getString(R.string.cancel);
        builderSingle.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        actions.reboot();
                        break;
                    case 1:
                        actions.rebootRecovery();
                        break;
                    case 2:
                        actions.rebootBootloader();
                        break;
                }
            }
        });
        builderSingle.show();
    }

    private void addFooterElements() {
        viewFooterElements = findViewById(R.id.viewFooterElements);
        footerElementGenerator = new FooterElementGenerator(this);

        Drawable silentIcon = ContextCompat.getDrawable(this,R.drawable.ic_volume_off);
        silent = footerElementGenerator.getView(silentIcon);
        silent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.ringerSilent();
                PowerMenuActivity.this.finish();
            }
        });

        Drawable vibrateIcon = ContextCompat.getDrawable(this,R.drawable.ic_vibration);
        vibrate = footerElementGenerator.getView(vibrateIcon);
        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.ringerVibrate();
                PowerMenuActivity.this.finish();
            }
        });

        Drawable normalIcon = ContextCompat.getDrawable(this,R.drawable.ic_volume_up);
        normal = footerElementGenerator.getView(normalIcon);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.ringerNormal();
                PowerMenuActivity.this.finish();
            }
        });

        viewFooterElements.addView(silent);
        viewFooterElements.addView(vibrate);
        viewFooterElements.addView(normal);
    }
}
