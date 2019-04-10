package org.wikipedia.descriptions;

import android.content.Context;
import android.content.Intent;

import org.wikipedia.activity.SingleFragmentActivityTransparent;

import androidx.annotation.NonNull;

public class DescriptionEditSuccessActivity
        extends SingleFragmentActivityTransparent<DescriptionEditSuccessFragment>
        implements DescriptionEditSuccessFragment.Callback {

    static Intent newIntent(@NonNull Context context) {
        return new Intent(context, DescriptionEditSuccessActivity.class);
    }

    @Override protected DescriptionEditSuccessFragment createFragment() {
        return DescriptionEditSuccessFragment.newInstance();
    }

    @Override
    public void onDismissClick() {
        setResult(RESULT_OK);
        finish();
    }
}
