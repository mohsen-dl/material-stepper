package com.fcannizzaro.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.TabStepper;

public class TabSample extends TabStepper {

    private int i = 1;

    private String _SearchType = "_ORDER";

    private String _ORDER = "_ORDER";
    private String _RESERVE = "_RESERVE";

    private String _StepName = "_StepName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        boolean linear = getIntent().getBooleanExtra("linear", false);
        setErrorTimeout(1500);
        setLinear(linear);
        setTitle("Tab Stepper <small>(" + (linear ? "" : "Non ") + "Linear)</small>");
        setAlternativeTab(false);
        setPreviousVisible();

        _SearchType = _RESERVE;

        int tabCount = 0;
        if(_SearchType.equals(_ORDER)) {
            tabCount = 5;
        }else if (_SearchType.equals(_RESERVE)){
            tabCount = 4;
        }
        for (int _tabs = 0; _tabs < tabCount; _tabs++){
            addStep(createFragment(new StepSample()));
        }
        super.onCreate(savedInstanceState);
    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle b = new Bundle();
        if(_SearchType.equals(_ORDER)) {
            switch (i) {
                case 1:
                    b.putString(_StepName, getString(R.string.City_fa));
                    break;
                case 2:
                    b.putString(_StepName, getString(R.string.JobType_fa));
                    break;
                case 3:
                    b.putString(_StepName, getString(R.string.FoodType_fa));
                    break;
                case 4:
                    b.putString(_StepName, getString(R.string.DeliveryType_fa));
                    break;
                case 5:
                    b.putString(_StepName, getString(R.string.OtherDetails_fa));
                    break;
                default:
                    b.putString(_StepName, "");
                    break;
            }
        }else if(_SearchType.equals(_RESERVE)){
            switch (i) {
                case 1:
                    b.putString(_StepName, getString(R.string.City_fa));
                    break;
                case 2:
                    b.putString(_StepName, getString(R.string.JobType_fa));
                    break;
                case 3:
                    b.putString(_StepName, getString(R.string.FacilitiesType_fa));
                    break;
                case 4:
                    b.putString(_StepName, getString(R.string.OtherDetails_fa));
                    break;
                default:
                    b.putString(_StepName, "");
                    break;
            }
        }
        b.putInt("position", i++);

        fragment.setArguments(b);
        return fragment;
    }

}
