package com.jstech.fairy.Navigation.Navi_Security;

import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.View;
import android.widget.TextView;

import com.jstech.fairy.R;

/**
 * Created by 박PC on 2017-10-04.
 */


public class PasscodeManagePasswordActivity extends AbstractPasscodeKeyboardActivity {
    public static final String  KEY_TYPE = "type";

    private int type = -1;
    private String unverifiedPasscode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getInt(KEY_TYPE, -1);
        }

        if(type ==PasscodePreferenceFragment.DISABLE_PASSLOCK){
            AppLockManager.getInstance().getAppLock().setPassword(null);
            authenticationSucceeded();
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFingerprintManager.isHardwareDetected() &&
                mFingerprintManager.hasEnrolledFingerprints() &&
                type == PasscodePreferenceFragment.DISABLE_PASSLOCK) {
            mFingerprintManager.authenticate(null, 0, mCancel = new CancellationSignal(), getFingerprintCallback(), null);
            View view = findViewById(R.id.image_fingerprint);
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPinLockInserted() {
        String passLock = mPinCodeField.getText().toString();
        mPinCodeField.setText("");

        switch (type) {
            case PasscodePreferenceFragment.ENABLE_PASSLOCK:
                if (unverifiedPasscode == null) {
                    ((TextView) findViewById(R.id.passcodelock_prompt)).setText(R.string.passcode_re_enter_passcode);
                    unverifiedPasscode = passLock;
                } else {
                    if (passLock.equals(unverifiedPasscode)) {
                        AppLockManager.getInstance().getAppLock().setPassword(passLock);
                        authenticationSucceeded();
                    } else {
                        unverifiedPasscode = null;
                        topMessage.setText(R.string.passcodelock_prompt_message);
                        authenticationFailed();
                    }
                }
                break;
            case PasscodePreferenceFragment.CHANGE_PASSWORD:
                //verify old password
                if (AppLockManager.getInstance().getAppLock().verifyPassword(passLock)) {
                    topMessage.setText(R.string.passcodelock_prompt_message);
                    type = PasscodePreferenceFragment.ENABLE_PASSLOCK;
                } else {
                    authenticationFailed();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected FingerprintManagerCompat.AuthenticationCallback getFingerprintCallback() {
        return new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                AppLockManager.getInstance().getAppLock().setPassword(null);
                authenticationSucceeded();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                authenticationFailed();
            }
        };
    }
}
