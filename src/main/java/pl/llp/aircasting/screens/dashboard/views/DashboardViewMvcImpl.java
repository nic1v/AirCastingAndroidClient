package pl.llp.aircasting.screens.dashboard.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.TreeMap;

import pl.llp.aircasting.R;
import pl.llp.aircasting.model.Sensor;
import pl.llp.aircasting.screens.common.helpers.ResourceHelper;
import pl.llp.aircasting.screens.dashboard.adapters.DashboardRecyclerAdapter;

public class DashboardViewMvcImpl implements DashboardViewMvc, View.OnClickListener, DashboardRecyclerAdapter.Listener {
    private final View mRootView;
    private final LayoutInflater mInflater;
    private final View mEmptyLayout;
    private final RecyclerView mRecyclerView;
    private final View mMicrophoneButton;
    private final View mSensorsButton;
    private final View mAirbeam2ConfigButton;
    private final DashboardRecyclerAdapter mRecyclerAdapter;
    private final AppCompatActivity mContext;
    private Listener mListener;

    public DashboardViewMvcImpl(AppCompatActivity context, ViewGroup parent, ResourceHelper resourceHelper) {
        mContext = context;
        mInflater = mContext.getLayoutInflater();
        mRootView = mInflater.inflate(R.layout.dashboard, parent, false);

        mEmptyLayout = findViewById(R.id.layout_empty);
        mRecyclerView = findViewById(R.id.recycler_view);
        mMicrophoneButton = findViewById(R.id.dashboard_microphone);
        mSensorsButton = findViewById(R.id.dashboard_sensors);
        mAirbeam2ConfigButton = findViewById(R.id.configure_airbeam2);

        if (mMicrophoneButton != null) { mMicrophoneButton.setOnClickListener(this); }
        if (mSensorsButton != null) { mSensorsButton.setOnClickListener(this); }
        if (mAirbeam2ConfigButton != null) { mAirbeam2ConfigButton.setOnClickListener(this); }

        mRecyclerAdapter = new DashboardRecyclerAdapter(mInflater, this, resourceHelper);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public void registerListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public void unregisterListener() {
        mListener = null;
    }

    @Override
    public void bindSensorData(List data) {
        Log.w("dashboard view", "binding data to adapter");
        Log.w("data size", String.valueOf(data.size()));
        Log.w("data ", String.valueOf(data));

        if (!data.isEmpty()) {
            mEmptyLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mEmptyLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

        mRecyclerAdapter.bindData(data);
    }

    @Override
    public void bindNowValues(TreeMap recentMeasurementsData) {
        Log.w("bindNowValues", String.valueOf(recentMeasurementsData));
        mRecyclerAdapter.bindNowValues(recentMeasurementsData);
    }

    private <T extends View> T findViewById(int id) {
        return getRootView().findViewById(id);
    }

    @Override
    public void onClick(View v) {
        mListener.onDashboardButtonClicked(v);
    }

    @Override
    public void onStreamClicked(View view) {
        mListener.onStreamClicked(view);
    }
}
