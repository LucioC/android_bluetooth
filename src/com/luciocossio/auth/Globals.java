package com.luciocossio.auth;

import java.util.UUID;

public class Globals {

    // Debugging
    public static final String TAG = "BluetoothChatService";
    public static final boolean D = true;

    // Name for the SDP record when creating server socket
    public static final String NAME_SECURE = "BluetoothChatSecure";
    public static final String NAME_INSECURE = "BluetoothChatInsecure";

    // Unique UUID for this application
    public static final UUID MY_UUID_SECURE =
        UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final UUID MY_UUID_INSECURE =
        UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
}
