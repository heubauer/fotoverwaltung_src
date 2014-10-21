package com.heubauer.fotoverwaltung;

import android.content.Intent;
import android.provider.MediaStore;

/**
 * Created by admin on 21.10.2014.
 */
public class CamClass {
    public Intent startCam(){
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }
}
