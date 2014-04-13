package com.luciocossio.auth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * This thread runs while attempting to make an outgoing connection
 * with a device. It runs straight through; the connection either
 * succeeds or fails.
 */
public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private String mSocketType;
    private final BluetoothAdapter mAdapter;
    private BluetoothChatService chatService;

    public ConnectThread(BluetoothDevice device, boolean secure, BluetoothAdapter adapter, BluetoothChatService chatService) {
    	this.chatService = chatService; 
    	mAdapter = adapter;
        mmDevice = device;
        BluetoothSocket tmp = null;
        mSocketType = secure ? "Secure" : "Insecure";

        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            if (secure) {
                tmp = device.createRfcommSocketToServiceRecord(
                        Globals.MY_UUID_SECURE);
            } else {
                tmp = device.createInsecureRfcommSocketToServiceRecord(
                        Globals.MY_UUID_INSECURE);
            }
        } catch (IOException e) {
            Log.e(Globals.TAG, "Socket Type: " + mSocketType + "create() failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        Log.i(Globals.TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
        setName("ConnectThread" + mSocketType);

        // Always cancel discovery because it will slow down a connection
        mAdapter.cancelDiscovery();

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mmSocket.connect();
        } catch (IOException e) {
            // Close the socket
            try {
                mmSocket.close();
            } catch (IOException e2) {
                Log.e(Globals.TAG, "unable to close() " + mSocketType +
                        " socket during connection failure", e2);
            }
            chatService.connectionFailed();
            return;
        }

        chatService.resetConnectThread();

        // Start the connected thread
        chatService.connected(mmSocket, mmDevice, mSocketType);
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(Globals.TAG, "close() of connect " + mSocketType + " socket failed", e);
        }
    }
}