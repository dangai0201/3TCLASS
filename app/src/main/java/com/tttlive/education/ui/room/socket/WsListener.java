package com.tttlive.education.ui.room.socket;



public interface WsListener {
    /**
     * Handle the data, often display it.
     * <p>This method would be called on main thread.</p>
     */
    void handleData(String  data);
}
