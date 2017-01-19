package com.ewgvip.buyer.android.websocket.async.http.socketio;

public interface ConnectCallback {
    public void onConnectCompleted(Exception ex, SocketIOClient client);
}