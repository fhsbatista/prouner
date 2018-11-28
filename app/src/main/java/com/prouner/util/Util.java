package com.prouner.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {

    /**
     * Generate a <b>.mp3</b> file from a array of bytes
     * @param bytes Array of bytes that has to be converted to a file
     * @param context Necessary to the method be able to access the applications's file system
     *                and save the file.
     * @return A FileInputStream
     */
    public static FileInputStream getFileFromByteArray(byte[] bytes, Context context) {

        try {
            File temp = File.createTempFile("audio", "mp3", context.getCacheDir());
            temp.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(temp);
            fos.write(bytes);
            fos.close();

            FileInputStream fis = new FileInputStream(temp);

            return fis;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *Checks if the device is currently connected to a network (it can be a internet connection or local connection).
     * @param context Necessary to the method be able to access the Devices's Network data.
     * @return true if there is a network connection, otherwise returns false.
     */
    public static boolean isConnectedToNetwork(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;

        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }
}
