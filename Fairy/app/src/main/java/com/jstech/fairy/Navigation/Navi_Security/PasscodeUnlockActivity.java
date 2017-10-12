package com.jstech.fairy.Navigation.Navi_Security;

import android.content.Intent;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.View;

import com.jstech.fairy.R;

/**
 * Created by 박PC on 2017-10-04.
 */

public class PasscodeUnlockActivity extends AbstractPasscodeKeyboardActivity {
    @Override
    public void onResume() {

        if (isFingerprintSupportedAndEnabled()) {
            mCancel = new CancellationSignal();
            mFingerprintManager.authenticate(null, 0, mCancel, getFingerprintCallback(), null);
            View view = findViewById(R.id.image_fingerprint);
            view.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        getAppLock().forcePasswordLock();
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
        finish();
    }

    @Override
    protected void onPinLockInserted() {
        String passLock = mPinCodeField.getText().toString();
        if (getAppLock().verifyPassword(passLock)) {
            authenticationSucceeded();
        } else {
            authenticationFailed();
        }
    }

    @Override
    protected FingerprintManagerCompat.AuthenticationCallback getFingerprintCallback() {
        return new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                // without the call to verifyPassword the unlock screen will show multiple times
                getAppLock().verifyPassword(AbstractAppLock.FINGERPRINT_VERIFICATION_BYPASS);
                authenticationSucceeded();
            }
            @Override
            public void onAuthenticationFailed() {
                authenticationFailed();
            }
            @Override public void onAuthenticationError(int errMsgId, CharSequence errString) { }
            @Override public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) { }
        };
    }

    private boolean isFingerprintSupportedAndEnabled() {
        return mFingerprintManager.isHardwareDetected() &&
                mFingerprintManager.hasEnrolledFingerprints() &&
                getAppLock().isFingerprintEnabled();
    }
}
