package com.demented.cphelps76.powermenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

class Actions {
    private Context context;
    private AudioManager am;

    private static final String COMMAND_SHUTDOWN_BROADCAST = "am broadcast android.intent.action.ACTION_SHUTDOWN";
    private static final String COMMAND_SHUTDOWN = "reboot -p";
    private static final String COMMAND_REBOOT = "reboot";
    private static final String COMMAND_REBOOT_RECOVERY = "reboot recovery";
    private static final String COMMAND_REBOOT_BOOTLOADER = "reboot bootloader";
    private static final String COMMAND_SLEEP = "sleep 2";
    private static final String COMMAND_FLIGHT_MODE_ON = "settings put global airplane_mode_on 1";
    private static final String COMMAND_FLIGHT_MODE_OFF = "settings put global airplane_mode_on 0";
    private static final String COMMAND_FLIGHT_MODE_BROADCAST_ON = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state 1";
    private static final String COMMAND_FLIGHT_MODE_BROADCAST_OFF = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state 0";

    Actions(Context context){
        this.context = context;
        am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }

    private static boolean isRooted(){
        return Shell.SU.available();
    }

    boolean isFlightModeOn() {
        return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    private void showProgress(){
        ProgressDialog pgdlg = new ProgressDialog(context,android.R.style.Theme_DeviceDefault_Dialog_Alert);
        pgdlg.setIndeterminate(true);
        pgdlg.setMessage("Shutting Down...");
        pgdlg.show();
    }

    private void showToast(String msg,int duration){
        Toast toast = Toast.makeText(context,msg,duration);

        View view = toast.getView();
        view.setBackgroundResource(android.R.drawable.toast_frame);

        TextView toastMessage = view.findViewById(android.R.id.message);

        toastMessage.setBackgroundColor((Color.parseColor("#00000000")));

        toast.show();
    }

    void shutdown(){

        if(!isRooted()){
            showToast("This Action requires ROOT permissions",Toast.LENGTH_LONG);
            return;
        }

        showProgress();

        (new BackgroundTask(new String[]{COMMAND_SHUTDOWN_BROADCAST,COMMAND_SLEEP,COMMAND_SHUTDOWN})).execute();
    }

    void reboot(){
        if(!isRooted()){
            showToast("This Action requires ROOT permissions", Toast.LENGTH_LONG);
            return;
        }
        showProgress();
        (new BackgroundTask(new String[]{COMMAND_SHUTDOWN_BROADCAST,COMMAND_SLEEP,COMMAND_REBOOT})).execute();
    }

    void rebootRecovery(){
        if(!isRooted()){
            showToast("This Action requires ROOT permissions", Toast.LENGTH_LONG);
            return;
        }
        showProgress();
        (new BackgroundTask(new String[]{COMMAND_SHUTDOWN_BROADCAST,COMMAND_SLEEP,COMMAND_REBOOT_RECOVERY})).execute();
    }

    void rebootBootloader(){
        if(!isRooted()){
            showToast("This Action requires ROOT permissions", Toast.LENGTH_LONG);
            return;
        }
        showProgress();
        (new BackgroundTask(new String[]{COMMAND_SHUTDOWN_BROADCAST,COMMAND_SLEEP,COMMAND_REBOOT_BOOTLOADER})).execute();
    }

    void flightModeToggle(){

        if(!isRooted()){
            showToast("This Action requires ROOT permissions", Toast.LENGTH_LONG);
            return;
        }
        if(isFlightModeOn()) {
            showToast("Exiting Flight Mode...",Toast.LENGTH_SHORT);
            (new BackgroundTask(new String[]{COMMAND_FLIGHT_MODE_OFF,COMMAND_FLIGHT_MODE_BROADCAST_OFF})).execute();
        }
        else{
            showToast("Entering Flight Mode...",Toast.LENGTH_SHORT);
            (new BackgroundTask(new String[]{COMMAND_FLIGHT_MODE_ON,COMMAND_FLIGHT_MODE_BROADCAST_ON})).execute();
        }
    }

    void ringerSilent(){
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        //showToast("Silent Mode Activated",Toast.LENGTH_SHORT);
    }

    void ringerVibrate(){
        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        //showToast("Vibration Mode Activated",Toast.LENGTH_SHORT);
    }

    void ringerNormal(){
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        //showToast("Normal Mode Activated",Toast.LENGTH_SHORT);
    }

    private class BackgroundTask extends AsyncTask<Void,Void,List<String>>{

        String commands[];

        BackgroundTask(String[] commands){
            this.commands = commands;
        }

        @Override
        protected List<String> doInBackground(Void... params) {

            return Shell.SU.run(commands);
        }

        @Override
        protected void onPostExecute(List<String> result){
            if(result==null){
                showToast("Failed to Execute command - Root Permission was denied",Toast.LENGTH_LONG);
            }
        }
    }
}
