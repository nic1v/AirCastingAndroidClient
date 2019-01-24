/**
 * AirCasting - Share your Air!
 * Copyright (C) 2011-2012 HabitatMap, Inc.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * You can contact the authors by email at <info@habitatmap.org>
 */
package pl.llp.aircasting.screens.stream.map;

import android.util.Log;
import pl.llp.aircasting.screens.common.sessionState.VisibleSession;
import pl.llp.aircasting.screens.common.helpers.ResourceHelper;
import pl.llp.aircasting.model.Measurement;
import pl.llp.aircasting.model.Sensor;
import pl.llp.aircasting.screens.stream.MeasurementPresenter;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;
import com.google.inject.Inject;

import java.util.List;

import static pl.llp.aircasting.util.DrawableTransformer.centerAt;

public class TraceOverlay extends BufferingOverlay<Measurement> {
    @Inject
    VisibleSession visibleSession;
    @Inject
    MeasurementPresenter measurementPresenter;
    @Inject
    ResourceHelper resourceHelper;
    @Inject
    SoundHelper soundHelper;

    @Override
    protected void performDraw(Canvas canvas, Projection projection) {
        if (shouldSkipDrawing()) return;
        Log.w("trace", "perform draw");

        List<Measurement> fullView = measurementPresenter.getFullView();

        // Avoid concurrent modification
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, fullViewSize = fullView.size(); i < fullViewSize - 1; i++) {
            Measurement measurement = fullView.get(i);
            drawPoint(canvas, projection, measurement);
        }
    }

    private boolean shouldSkipDrawing() {
        return visibleSession.isSessionLocationless();
    }

    @Override
    protected void performUpdate(Canvas canvas, Projection projection, Measurement measurement) {
        drawPoint(canvas, projection, measurement);
    }

    private void drawPoint(Canvas canvas, Projection projection, Measurement measurement) {
        double value = measurement.getValue();
        Sensor sensor = visibleSession.getSensor();

        if (soundHelper.shouldDisplay(sensor, value)) {
            Drawable bullet = resourceHelper.getBulletAbsolute(sensor, value);

            GeoPoint geoPoint = LocationConversionHelper.geoPoint(measurement.getLatitude(), measurement.getLongitude());
            Point point = projection.toPixels(geoPoint, null);

            centerAt(bullet, point);
            bullet.draw(canvas);
        }
    }

    public void stopDrawing(MapView view) {
        super.stopDrawing(view);
    }
}
