package pl.llp.aircasting.screens.extsens;

import pl.llp.aircasting.R;
import pl.llp.aircasting.sensor.common.ExternalSensorDescriptor;

import android.content.Context;
import android.widget.SimpleAdapter;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class SensorAdapter extends SimpleAdapter {
    public static final String ADDRESS = "address";
    public static final String NAME = "name";
    public static final String ACTION = "action";

    public static final String[] KEYS = new String[]{ADDRESS, NAME, ACTION};
    public static final int[] FIELDS = new int[]{R.id.address, R.id.name, R.id.action};

    protected List<Map<String, String>> data;

    SensorAdapter(Context context) {
        this(context, Lists.<Map<String, String>>newArrayList());
    }

    SensorAdapter(Context context, List<Map<String, String>> data) {
        super(context, data, R.layout.external_sensor_item, KEYS, FIELDS);
        this.data = data;
    }

    public ExternalSensorDescriptor get(int position) {
        Map<String, String> map = data.get(position);
        return new ExternalSensorDescriptor(map.get(NAME), map.get(ADDRESS), map.get(ACTION));
    }

    public Map<String, String> remove(int position) {
        Map<String, String> remove = data.remove(position);
        notifyDataSetChanged();
        return remove;
    }

    protected int findPosition(ExternalSensorDescriptor descriptor) {
        return findPosition(descriptor.getAddress());
    }

    boolean contains(String address) {
        return findPosition(address) > -1;
    }

    protected int findPosition(String address) {
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> deviceMap = data.get(i);
            if (address.equalsIgnoreCase(deviceMap.get(ADDRESS)))
                return i;
        }
        return -1;
    }
}
