package com.badrul.awlacompany;

import android.app.Application;

public class ShowCustomFonts extends Application {

    public void onCreate() {
        super.onCreate();

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/sofiapromedium.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/sofiapromedium.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/sofiapromedium.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/sofiapromedium.ttf");
    }
}
