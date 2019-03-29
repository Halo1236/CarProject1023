// IBTRoutingService.aidl
package com.smkbt.aidl;


interface IBTRoutingService {

    //Bluetooth settings

    // Bluetooth device manager

    // Bluetooth music
    void reqAvrcpBackward();
    void reqAvrcpForward();
    void reqAvrcpPause();
    void reqAvrcpPlay();
    void reqAvrcpPlayOrPause();
    String getBtMusicTitle();
    String getBtMusicArtist();
    String getBtMusicAlbum();
}
