package com.github.fcannizzaro.materialstepper.style;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.R;
import com.github.fcannizzaro.materialstepper.util.LinearityChecker;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class TabStepper extends BasePager implements View.OnClickListener {

    private Context _context;

    protected TextView mError;

    // attributes
    int unselected = Color.parseColor("#9e9e9e");
    int green = Color.parseColor("#43A047");
    int _colorLightBlueHolo = Color.parseColor("#1976d2");
    // views
    private HorizontalScrollView mTabs;
    private LinearLayout mStepTabs;
    private boolean mLinear;
    private boolean showPrevButton = false;
    private boolean disabledTouch = false;
    private boolean mTabAlternative;
    private ViewSwitcher mSwitch;
    private LinearityChecker mLinearity;
    private TextView mContinue;
    private TextView mPreviousButton;

    private FloatingActionButton search_fab;

    protected void setLinear(boolean mLinear) {
        this.mLinear = mLinear;
    }

    protected void setDisabledTouch() {
        this.disabledTouch = true;
    }

    protected void setPreviousVisible() {
        this.showPrevButton = true;
    }

    protected void setAlternativeTab(boolean mTabAlternative) {
        this.mTabAlternative = mTabAlternative;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyTheme();

        _context = this.getApplicationContext();

        if (getSupportActionBar() != null)
            getSupportActionBar().setElevation(0);

        setContentView(R.layout.style_horizontal_tabs);

        init();

        mTabs = (HorizontalScrollView) findViewById(R.id.steps);
        mStepTabs = (LinearLayout) mTabs.findViewById(R.id.stepTabs);
        mSwitch = (ViewSwitcher) findViewById(R.id.stepSwitcher);
        mError = (TextView) findViewById(R.id.stepError);
        mPreviousButton = (TextView) findViewById(R.id.stepPrev);
        mPreviousButton.setTypeface(GetTypeFace("fontawesome-webfont.ttf"));
        mPreviousButton.setTextColor(_colorLightBlueHolo);

        mContinue = (TextView) findViewById(R.id.continueButton);
        mContinue.setTextColor(_colorLightBlueHolo);
        mContinue.setOnClickListener(this);

        search_fab = (FloatingActionButton)findViewById(R.id.search_fab);
        search_fab.setOnClickListener(this);

        mSwitch.setDisplayedChild(0);
        mSwitch.setInAnimation(TabStepper.this, R.anim.in_from_bottom);
        mSwitch.setOutAnimation(TabStepper.this, R.anim.out_to_bottom);

        mLinearity = new LinearityChecker(mSteps.total());

        if (!showPrevButton)
            mPreviousButton.setVisibility(View.GONE);

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPrevious();
            }
        });

        onUpdate();
    }

    @Override
    public void onUpdate() {

        int i = 0;

        if (mStepTabs.getChildCount() == 0) {
            while (i < mSteps.total()) {
                AbstractStep step = mSteps.get(i);
                mStepTabs.addView(createStepTab(i++, step.name(), step.isOptional(), step.optional()));
            }
        }

        int size = mStepTabs.getChildCount();

        for (i = 0; i < size; i++) {

            View view = mStepTabs.getChildAt(i);

            boolean done = mLinearity.isDone(i);
            View doneIcon = view.findViewById(R.id.done);
            View stepIcon = view.findViewById(R.id.step);

            doneIcon.setVisibility(done ? View.VISIBLE : View.GONE);
            stepIcon.setVisibility(!done ? View.VISIBLE : View.GONE);
            color(done ? doneIcon : stepIcon, i == mSteps.current() || done);

            TextView text = (TextView) view.findViewById(R.id.title);
            text.setTypeface(i == mSteps.current() || done ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            text.setTypeface(GetTypeFace("BYEKAN.TTF"));
            text.setTextColor(i == mSteps.current() || done ? green : unselected);
            view.findViewById(R.id.title).setAlpha(i == mSteps.current() || done ? 1 : 0.54f);

            TextView mOptional = (TextView) view.findViewById(R.id.optional);
            mOptional.setTextColor(i == mSteps.current() || done ? green : unselected);
            view.findViewById(R.id.optional).setAlpha(i == mSteps.current() || done ? 1 : 0.54f);
            mPreviousButton.setVisibility(showPrevButton && mSteps.current() > 0 ? View.VISIBLE : View.GONE);

        }

        if (mSteps.current() == mSteps.total() - 1){
            mContinue.setText(R.string.ms_end);
            mContinue.setVisibility(View.GONE);
        }else{
            mContinue.setText(R.string.fa_angle_right);
            mContinue.setTypeface(GetTypeFace("fontawesome-webfont.ttf"));
            mContinue.setVisibility(View.VISIBLE);
        }


    }

    private boolean updateDoneCurrent() {
        if (mSteps.getCurrent().nextIf()) {
            mLinearity.setDone(mSteps.current() + 1);
            return true;
        }
        return mSteps.getCurrent().isOptional();
    }

    private View createStepTab(final int position, String title, boolean isOptional, String optionalStr) {
        View view = getLayoutInflater().inflate(mTabAlternative ? R.layout.step_tab_alternative : R.layout.step_tab, mStepTabs, false);

        TextView mstep = (TextView) view.findViewById(R.id.step);
        mstep.setTypeface(GetTypeFace("BYEKAN.TTF"));
        mstep.setText(String.valueOf(position + 1));
//        ((TextView) view.findViewById(R.id.step)).setText(String.valueOf(position + 1));

        if (isOptional) {
            view.findViewById(R.id.optional).setVisibility(View.VISIBLE);
//            ((TextView) view.findViewById(R.id.optional)).setText(optionalStr);
            TextView moptionalStr = (TextView) view.findViewById(R.id.optional);
            moptionalStr.setTypeface(GetTypeFace("IRANSansWeb_Medium.ttf"));
            moptionalStr.setText(_context.getString(R.string.Select_fa));
//            moptionalStr.setText(optionalStr);
        }

        if (position == mSteps.total() - 1)
            view.findViewById(R.id.divider).setVisibility(View.GONE);

//        String Tab_fa = _context.getString(R.string.Select_fa);
//        Tab_fa += getString(R.string.fa_space);
//        Tab_fa += String.valueOf(position + 1);

        TextView mtitle = (TextView) view.findViewById(R.id.title);
        mtitle.setTypeface(GetTypeFace("BYEKAN.TTF"));
        mtitle.setText(title);
//        switch (position){
//            case 0:
//                mtitle.setText(title);
////                mtitle.setText(_context.getString(R.string.City_fa));
//                break;
//            case 1:
//                mtitle.setText(_context.getString(R.string.JobType_fa));
//                break;
//            case 2:
//                mtitle.setText(_context.getString(R.string.FoodType_fa));
//                break;
//            case 3:
//                mtitle.setText(_context.getString(R.string.DeliveryType_fa));
//                break;
//            case 4:
//                mtitle.setText(_context.getString(R.string.OtherDetails_fa));
//                break;
//            default:
//                mtitle.setText("");
//                break;
//        }

//        mtitle.setText(Tab_fa);
//        ((TextView) view.findViewById(R.id.title)).setText(title);

        if (!disabledTouch)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    boolean optional = mSteps.getCurrent().isOptional();

                    if (position != mSteps.current())
                        updateDoneCurrent();

                    if (!mLinear || optional || mLinearity.beforeDone(position)) {
                        mSteps.current(position);
                        updateScrolling(position);
                    } else
                        onError();

                    onUpdate();
                }
            });

        return view;
    }

    private void color(View view, boolean selected) {
        Drawable d = view.getBackground();
        d.setColorFilter(new PorterDuffColorFilter(selected ? green : unselected, PorterDuff.Mode.SRC_ATOP));
    }

    private void updateScrolling(int newPosition) {
        View tab = mStepTabs.getChildAt(mSteps.current());
        boolean isNear = mSteps.current() == newPosition - 1 || mSteps.current() == newPosition + 1;
        mPager.setCurrentItem(mSteps.current(), isNear);
        mTabs.smoothScrollTo(tab.getLeft() - 20, 0);
        onUpdate();
    }

    @Override
    public void onError() {

        mError.setText(Html.fromHtml(mSteps.getCurrent().error()));

        if (mSwitch.getDisplayedChild() == 0)
            mSwitch.setDisplayedChild(1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSwitch.getDisplayedChild() == 1) mSwitch.setDisplayedChild(0);
            }
        }, getErrorTimeout() + 300);

    }

    @Override
    public void onPrevious() {
        super.onPrevious();
        updateScrolling(mSteps.current() - 1);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.continueButton) {
            if (updateDoneCurrent()) {
                onNext();
                updateScrolling(mSteps.current() + 1);
            } else {
                onError();
            }
        }else if (i == R.id.search_fab) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("data","data");
            Class _Class = null;
            try {
                String class_name_str = "ir.uli.uli.view.SearchResultActivity";
                _Class = Class.forName(class_name_str);
                returnIntent = new Intent(this, _Class);
                startActivity(returnIntent);
            } catch (ClassNotFoundException e) {
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }

    @Deprecated
    protected void disabledTouch() {
        this.disabledTouch = true;
    }

    @Deprecated
    protected void showPreviousButton() {
        this.showPrevButton = true;
    }

    private Typeface GetTypeFace(String fontName){
        Typeface face = Typeface.createFromAsset(_context.getAssets(), "fonts/" + fontName);
        return face;
    }
}
