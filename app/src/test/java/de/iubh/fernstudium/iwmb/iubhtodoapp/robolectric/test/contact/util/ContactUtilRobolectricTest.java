package de.iubh.fernstudium.iwmb.iubhtodoapp.robolectric.test.contact.util;

import android.content.ContentProvider;
import android.content.ContentResolver;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import de.iubh.fernstudium.iwmb.iubhtodoapp.BuildConfig;
import de.iubh.fernstudium.iwmb.iubhtodoapp.utils.ContactUtils;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 26)
public class ContactUtilRobolectricTest {

    private ContactUtils contactUtils;
    ContentResolver contentResolver;

    @Before
    public void initliaze(){
        contentResolver = RuntimeEnvironment.application.getContentResolver();
    }
}
