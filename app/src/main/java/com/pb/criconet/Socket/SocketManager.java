package com.pb.criconet.Socket;

import android.util.Log;
import com.pb.criconet.util.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {

    private Socket socket = null;

    private Socket mSocket;

    public SocketManager() {
        try {
            socket = IO.socket(Global.SOCKETURL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on(Socket.EVENT_CONNECT, args -> {
            //Exception e = (Exception) args[0];
            // Log.e("SocketManager", "Connection error: " + e.getMessage());
            //Toaster.customToast("connected");
            String socketId = socket.id();
            System.out.println("connected"+"/"+socketId);
        });

//        try {
//            IO.Options options = new IO.Options();
//            //options.transports = new String[]{"websocket"}; // Force WebSocket transport
//            options.reconnection = true; // Enable reconnection
//            options.reconnectionDelay = 1000; // Reconnect after 1 second
//            options.reconnectionAttempts = Integer.MAX_VALUE; // Retry indefinitely
//
//            socket = IO.socket(Global.SOCKETURL, options);
//            socket.connect();
//
//            /*http://chat.criconetonline.com*/
//
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

//        socket.on(Socket.EVENT_CONNECT, args -> {
//            Log.d("SocketIO", "connected from server");
//            //System.out.println("connected");
//        });
//
//        // Listen for disconnect event
//        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                socket.connect();
//                Log.e("SocketIO", "Disconnected from server");
//            }
//        });
//
//        // Listen for reconnect event
//        socket.on(Manager.EVENT_RECONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.d("SocketIO", "Reconnected to server");
//            }
//        });
//
//        // Listen for reconnect error event
//        socket.on(Manager.EVENT_RECONNECT_ERROR, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.d("SocketIO", "Reconnect error occurred");
//            }
//        });

    }

    public Socket getSocket(){
        return this.socket;
    }

    public void connect() {
        if (socket != null) {
            socket.connect();
            // Set a connection callback to handle errors
//            socket.on(Socket.EVENT_CONNECT_ERROR, args -> {
//                Exception e = (Exception) args[0];
//                Log.e("SocketManager", "Connection error: " + e.getMessage());
//            });
        }
    }

   /* public void disconnect() {
        if (socket != null) {
            System.out.println("disconnect");
            // socket.disconnect();
        }
    }*/

    public boolean isConnected() {
        Log.d("Connect", "connect" + socket.connected());
        return socket != null && socket.connected();
    }

    public void onMessageReceived(Listener listener) {
        if (socket != null) {
            socket.on("broadCast", args -> {

                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    Log.d("Meessage", messageJson.toString());
                    listener.onReceived(messageJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });


        }
    }


    public void onPing(Listener listener) {
        if (socket != null) {
            socket.on("ping", args -> {

                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    Log.d("ping", messageJson.toString());
                    listener.onReceived(messageJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });


        }
    }

    public void onMessageReceivedd(Listener listener) {
        if (socket != null) {
            socket.on("liveChat", args -> {

                        if (args.length > 0) {
                            if (args[0] instanceof String) {
                                String receivedString = (String) args[0];
                                // Process the received string
                                Log.d("SocketIO", "Received string: " + receivedString);
                            }
                        }

                    }
            );
        }
    }

    public void onMessageReceiveParty(Listener listener) {
        if (socket != null) {
            socket.on("partyGroup", args -> {

                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());

                    //String message = messageJson.getString("message");
                    Log.d("Meessage", messageJson.toString());
                    listener.onReceived(messageJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });


        }
    }

    public void sendMessage(String message) {
        if (socket != null) {
            Log.d("Message", message);
            socket.emit("sendMessage", message);

        }
    }


    public void JoinRoom(String matchId) {
        if (socket != null) {
            Log.d("joinMatchId", matchId);
            socket.emit("joinMatchId", matchId);

        }
    }

    public void joinWatchParty(String matchId) {
        if (socket != null) {
            Log.d("JoinWatchParty", matchId);
            socket.emit("JoinWatchParty", matchId);

        }
    }

    public void createWatchParty(JSONObject payload) {
        if (socket != null) {
            Log.d("joinWatchParty", payload.toString());
            socket.emit("joinWatchParty", payload);

        }
    }
    public void playerControl(JSONObject payload){
        if (socket != null) {
            Log.d("playerControl", payload.toString());
            socket.emit("playerControl", payload);

        }
    }

    public void getPlayerControl(Listener listener){
        if (socket != null) {
            socket.on("playerControl", args -> {

                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());

                    //String message = messageJson.getString("message");
                    Log.d("Meessage", messageJson.toString());
                    listener.onReceived(messageJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });


        }
    }

    public void leaveWatchParty(JSONObject payload) {
        if (socket != null) {
            Log.d("leaveWatchParty", payload.toString());
            socket.emit("leaveWatchParty", payload);

        }
    }


    @FunctionalInterface
    public interface Listener {
        void onReceived(JSONObject message);
    }
}

