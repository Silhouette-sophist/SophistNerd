package com.example.coverage.component;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coverage.R;
import com.example.coverage.util.JacocoHelper;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.example.coverage.placeholder.PlaceholderContent;
import com.example.coverage.databinding.FragmentItemDetailBinding;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListFragment}
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The placeholder content this fragment is presenting.
     */
    private PlaceholderContent.PlaceholderItem mItem;
    private CollapsingToolbarLayout mToolbarLayout;
    private TextView mTextView;

    private final View.OnDragListener dragListener = (v, event) -> {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            ClipData.Item clipDataItem = event.getClipData().getItemAt(0);
            mItem = PlaceholderContent.ITEM_MAP.get(clipDataItem.getText().toString());
            updateContent();
        }
        return true;
    };
    private FragmentItemDetailBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the placeholder content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = PlaceholderContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentItemDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        mToolbarLayout = rootView.findViewById(R.id.toolbar_layout);
        mTextView = binding.itemDetail;
        binding.fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getActivity(), "导出jacoco数据", Toast.LENGTH_LONG).show();
                new Thread(() -> {
                    /*Class<?> aClass = null;
                    try {
                        aClass = Class.forName("org.jacoco.agent.rt.RT");
                        Object agent = aClass.getMethod("getAgent").invoke(null);
                        for (Method declaredMethod : agent.getClass().getDeclaredMethods()) {
                            System.out.println("declaredMethod -- " + declaredMethod);
                        }
                        Object getExecutionData = agent.getClass().getMethod("getExecutionData", boolean.class).invoke(agent, false);

                        if (getExecutionData instanceof byte[]) {
                            byte[] arr = (byte[]) getExecutionData;
                            System.out.println("getAgent ---- " + Arrays.toString(arr));
                        }

                        System.out.println("getAgent ---- " + getExecutionData);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("getAgent error --- " + Arrays.toString(e.getStackTrace()));
                    }*/
                    JacocoHelper.generateEcFile(true);
                }).start();
                return false;
            }
        });

        // Show the placeholder content as text in a TextView & in the toolbar if available.
        updateContent();
        rootView.setOnDragListener(dragListener);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateContent() {
        if (mItem != null) {
            mTextView.setText(mItem.details);
            if (mToolbarLayout != null) {
                mToolbarLayout.setTitle(mItem.content);
            }
        }
    }
}