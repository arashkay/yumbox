package geekhouse.ir.yumbox.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.norbsoft.typefacehelper.TypefaceHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import geekhouse.ir.yumbox.R;
import geekhouse.ir.yumbox.YumboxApp;
import geekhouse.ir.yumbox.ui.MainActivity;
import geekhouse.ir.yumbox.util.Helper;


/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFragment extends Fragment {

    private final static String ARG_TITLE = "title";

    @Bind(R.id.launch_text_view) TextView textView;

    @Bind(R.id.launch_view_pager_frag_floating_action_button) FloatingActionButton button;

    public IntroFragment() {
        // Required empty public constructor
    }

    public static IntroFragment newInstance(String title) {

        IntroFragment launchFragment = new IntroFragment();
        Bundle b = new Bundle();
        b.putString(ARG_TITLE, title);
        launchFragment.setArguments(b);

        return launchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intor_fragmnet, container, false);
        ButterKnife.bind(this, view);

        ((YumboxApp) getActivity().getApplication()).getComponent().inject(this);

        setTextViewAndFAB();
        TypefaceHelper.typeface(view);
        return view;

    }

    /**
     * starts MainActivity
     */
    private void startSecond() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.launch_view_pager_frag_floating_action_button)
    public void fabOnClickListener() {
        if (Helper.isConnected()) {
            startSecond();
        } else {
            Helper.showNoInternetToast(getActivity());
        }
    }

    private void setTextViewAndFAB() {

        final Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString(ARG_TITLE) == null) {
                textView.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
            } else {
                textView.setText(bundle.getString(ARG_TITLE));
            }
        }
    }
}
