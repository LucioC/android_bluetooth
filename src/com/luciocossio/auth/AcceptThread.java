package com.luciocossio.auth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * This thread runs while listening for incoming connections. It behaves
 * like a server-side client. It runs until a connection is accepted
 * (or until cancelled).
 */
public class AcceptThread extends Thread {
    // The local server socket
    private final BluetoothServerSocket mmServerSocket;
    private String mSocketType;
    private final BluetoothAdapter mAdapter;
    private final BluetoothChatService chatService;

    public AcceptThread(boolean secure, BluetoothAdapter adapter, BluetoothChatService chatService) {
    	this.chatService = chatService;
    	this.mAdapter = adapter;
        BluetoothServerSocket tmp = null;
        mSocketType = secure ? "Secure":"Insecure";

        // Create a new listening server socket
        try {
            if (secure) {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(Globals.NAME_SECURE,
                		Globals.MY_UUID_SECURE);
            } else {
                tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
                		Globals.NAME_INSECURE, Globals.MY_UUID_INSECURE);
            }
        } catch (IOException e) {
            Log.e(Globals.TAG, "Socket Type: " + mSocketType + "listen() failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        if (Globals.D) Log.d(Globals.TAG, "Socket Type: " + mSocketType +
                "BEGIN mAcceptThread" + this);
        setName("AcceptThread" + mSocketType);

        chatService.listen(mmServerSocket, mSocketType);        
        
        if (Globals.D) Log.i(Globals.TAG, "END mAcceptThread, socket Type: " + mSocketType);

    }

    public void cancel() {
        if (Globals.D) Log.d(Globals.TAG, "Socket Type" + mSocketType + "cancel " + this);
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(Globals.TAG, "Socket Type" + mSocketType + "close() of server failed", e);
        }
    }
}
