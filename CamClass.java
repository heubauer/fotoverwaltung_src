package com.heubauer.fotoverwaltung;

import android.content.Intent;
import android.provider.MediaStore;

public class CamClass {
    /**
     * Startet die Kamera um ein Bild zu schießen
     * @return Intent Das Intent, das das Bild schießt
     */
    public Intent startCam(){
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }
}
