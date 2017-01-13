package com.fcannizzaro.sample;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.fcannizzaro.materialstepper.AbstractStep;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample extends AbstractStep {

    private int i = 1;
    private Button button;
    private final static String CLICK = "click";
    private Context _context;
    private String _StepName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _context = getContext();
        View v = inflater.inflate(R.layout.step, container, false);
        button = (Button) v.findViewById(R.id.button);

        if (savedInstanceState != null) {
            i = savedInstanceState.getInt(CLICK, 0);
            _StepName = savedInstanceState.getString("_StepName");
        }

        String Tab_fa = getString(R.string.Tab_fa);
        Tab_fa += getString(R.string.fa_space);
        Tab_fa += String.valueOf(i);
        button.setText(Tab_fa);
        button.setTypeface(GetTypeFace("BYEKAN.TTF"));
//        button.setText(Html.fromHtml("Tap <b>" + i + "</b>"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button) view).setText(Html.fromHtml("Tap <b>" + (++i) + "</b>"));
                mStepper.getExtras().putInt(CLICK, i);
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(CLICK, i);
    }

    @Override
    public String name() {
        return getArguments().getString("_StepName");
//        return _StepName;
//        return "Tab " + getArguments().getInt("position", 0);
    }

    @Override
    public boolean isOptional() {
        return true;
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        System.out.println("onNext");
    }

    @Override
    public void onPrevious() {
        System.out.println("onPrevious");
    }

    @Override
    public String optional() {
        return "اختیاری";
    }

    @Override
    public boolean nextIf() {
        return i > 1;
    }

    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }

    private Typeface GetTypeFace(String fontName){
        Typeface face = Typeface.createFromAsset(_context.getAssets(), "fonts/" + fontName);
        return face;
    }
}
