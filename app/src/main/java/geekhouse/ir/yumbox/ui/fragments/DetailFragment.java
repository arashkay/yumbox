package geekhouse.ir.yumbox.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import geekhouse.ir.yumbox.R;
import geekhouse.ir.yumbox.util.DailyMeals;

public class DetailFragment extends AppCompatDialogFragment {

    private int viewNumber;

    @Bind(R.id.collery_detail_page) TextView calorie;
    @Bind(R.id.description_detail_page) TextView description;
    @Bind(R.id.food_name_detail_page) TextView foodName;
    @Bind(R.id.ingredients_detail_page) TextView ingredient;
    @Bind(R.id.close_detail_page) TextView close;

    @Bind(R.id.food_image_detail_page) ImageView foodImage;


    public static DetailFragment newInstance(int whichParseObject) {

        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("num", whichParseObject);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        viewNumber = bundle.getInt("num");

        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.detail_page_fragment, false)
                .show();
        View detailPage = dialog.getCustomView();
        ButterKnife.bind(this, detailPage);
        setImageAndTextViews();

        TypefaceHelper.typeface(detailPage);

        return dialog;
    }

    @OnClick(R.id.close_detail_page)
    @SuppressWarnings("unused")
    public void closeClickListener () {
        DetailFragment.this.getDialog().cancel();
    }

    private void setImageAndTextViews() {

        Picasso.with(getContext()).load(MainActivityFragment.TEST_PIC).fit().into(foodImage);

        description.setText(DailyMeals.dailyMeals.get(viewNumber).main_dish.description);
        foodName.setText(DailyMeals.dailyMeals.get(viewNumber).main_dish.name);
        ingredient.setText(" مواد: " + DailyMeals.dailyMeals.get(viewNumber).main_dish.contains);
        calorie.setText(DailyMeals.dailyMeals.get(viewNumber).main_dish.calories + " کالری ");
    }

}