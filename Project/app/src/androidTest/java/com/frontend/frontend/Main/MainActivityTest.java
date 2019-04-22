package com.frontend.frontend.Main;

import org.junit.Test;

import static org.junit.Assert.*;
import org.mockito.Mockito;

public class MainActivityTest {

    @Test
    public void onCreate() {
        Bundle bundleMock = Mockito.mock(Bundle.class);
        MainActivity x=new MainActivity();
        x.onCreate(bundleMock);
    }

    @Test
    public void onActivityResult(MainActivity x) {
        x.onActivityResult();
    }
}