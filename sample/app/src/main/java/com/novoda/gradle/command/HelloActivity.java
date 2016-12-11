package com.novoda.gradle.command;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public final class HelloActivity extends Activity {

    public static final String EXTRA_NAME = "NAME";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        greet();
    }

    private void greet() {
        String userName = getIntent().getStringExtra(EXTRA_NAME);
        if (TextUtils.isEmpty(userName)) {
            userName = "stranger";
        }
        ((TextView) findViewById(R.id.hello_text)).setText("Hello " + userName + "!");
    }

    public static Intent forUser(String user, Activity source) {
        final Intent intent = new Intent(source, HelloActivity.class);
        intent.putExtra(HelloActivity.EXTRA_NAME, user);
        return intent;
    }
}
