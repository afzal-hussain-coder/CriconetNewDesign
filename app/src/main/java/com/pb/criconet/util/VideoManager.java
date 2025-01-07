package com.pb.criconet.util;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;

public class VideoManager {

    private static VideoManager instance;
    private YouTubePlayer currentPlayer;

    private VideoManager() {}

    public static synchronized VideoManager getInstance() {
        if (instance == null) {
            instance = new VideoManager();
        }
        return instance;
    }

    public void setCurrentPlayer(YouTubePlayer player) {
        if (currentPlayer != null) {
            currentPlayer.pause();
        }
        currentPlayer = player;
    }

    public void releasePlayer() {
        if (currentPlayer != null) {
            currentPlayer.pause();
            currentPlayer = null;
        }
    }
}

