package pl.llp.aircasting.screens.dashboard;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import pl.llp.aircasting.R;
import pl.llp.aircasting.screens.common.helpers.ResourceHelper;
import pl.llp.aircasting.screens.common.sessionState.SessionDataFactory;
import pl.llp.aircasting.screens.common.sessionState.SessionState;
import pl.llp.aircasting.screens.common.ApplicationState;
import pl.llp.aircasting.model.MeasurementStream;
import pl.llp.aircasting.model.Sensor;
import pl.llp.aircasting.model.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static pl.llp.aircasting.screens.common.sessionState.ViewingSessionsSensorManager.PLACEHOLDER_SENSOR_NAME;

/**
 * Created with IntelliJ IDEA.
 * User: marcin
 * Date: 10/21/13
 * Time: 6:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class StreamViewHelper {
    public static final String FIXED_LABEL = "Last Minute";
    public static final String MOBILE_LABEL = "Last Second";

    @Inject
    ResourceHelper resourceHelper;
    @Inject
    SessionState sessionState;
    @Inject
    SessionDataFactory sessionData;
    @Inject ApplicationState applicationState;

    private static List<Integer> positionsWithTitle = new ArrayList<Integer>();

    public void setPositionsWithTitle(List positions) {
        positionsWithTitle = positions;
    }

    public void updateMeasurements(long sessionId, Sensor sensor, View view, int position) {
        RelativeLayout sessionTitleContainer = (RelativeLayout) view.findViewById(R.id.title_container);

        if (sensor.getSensorName().startsWith(PLACEHOLDER_SENSOR_NAME)) {
            setTitleView(sessionId, sessionTitleContainer);
            view.findViewById(R.id.placeholder_chart).setVisibility(View.VISIBLE);
            view.findViewById(R.id.actual_chart).setVisibility(View.GONE);
            return;
        }

        int now = (int) sessionData.getNow(sensor, sessionId);
        TextView nowTextView = (TextView) view.findViewById(R.id.now);
        TextView lastMeasurementLabel = (TextView) view.findViewById(R.id.last_measurement_label);
        TextView timestamp = (TextView) view.findViewById(R.id.timestamp);

        lastMeasurementLabel.setText(getLastMeasurementLabel(sessionId));
        showAndSetTimestamp(timestamp, sensor, sessionId);

        if (positionsWithTitle.contains(position)) {
            setTitleView(sessionId, sessionTitleContainer);
        } else {
            sessionTitleContainer.setVisibility(View.GONE);
        }

        nowTextView.setBackgroundDrawable(resourceHelper.streamValueGrey);

        if (sessionState.sessionHasColoredBackground(sessionId)){
            setBackground(sensor, nowTextView, now);
        }

        if (sessionState.sessionHasNowValue(sessionId)) {
            nowTextView.setText(String.valueOf(now));
        } else {
            nowTextView.setText(R.string.empty);
        }
    }

    private void setTitleView(long sessionId, RelativeLayout sessionTitleView) {
        Session session = sessionData.getSession(sessionId);
        TextView sessionTitle = (TextView) sessionTitleView.findViewById(R.id.session_title);
        LinearLayout sessionButtonsContainer = (LinearLayout) sessionTitleView.findViewById(R.id.session_reorder_buttons);

        sessionTitleView.setVisibility(View.VISIBLE);
        sessionTitle.setCompoundDrawablesWithIntrinsicBounds(session.getDrawable(), 0, 0, 0);

        if (applicationState.dashboardState().isSessionReorderInProgress()) {
            sessionButtonsContainer.setVisibility(View.VISIBLE);
        } else {
            sessionButtonsContainer.setVisibility(View.GONE);
        }

        if (!session.hasTitle()) {
            sessionTitle.setText(R.string.unnamed);
        } else {
            sessionTitle.setText(session.getTitle());
        }
    }

    private String getLastMeasurementLabel(long sessionId) {
        if (sessionData.getSession(sessionId).isFixed()) {
            return FIXED_LABEL;
        } else {
            return MOBILE_LABEL;
        }
    }

    private void showAndSetTimestamp(TextView timestamp, Sensor sensor, long sessionId) {
        if (sessionState.isSessionCurrent(sessionId)) {
            timestamp.setVisibility(View.GONE);
        } else {
            timestamp.setVisibility(View.VISIBLE);
            timestamp.setText(getTimestamp(sensor, sessionId));
        }
    }

    private String getTimestamp(Sensor sensor, long sessionId) {
        double time;
        MeasurementStream stream = sessionData.getStream(sensor.getSensorName(), sessionId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm MM/dd/yy");

        if (!sessionState.isSessionCurrent(sessionId)) {
            Date lastMeasurement = stream.getLastMeasurementTime();
            time = lastMeasurement.getTime();
        } else {
            Calendar calendar = Calendar.getInstance();
            time = calendar.getTime().getTime();
        }

        return dateFormat.format(time);
    }

    private void setBackground(Sensor sensor, View view, double value) {
        view.setBackgroundDrawable(resourceHelper.getStreamValueBackground(sensor, value));
    }
}
