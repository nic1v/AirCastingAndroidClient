/**
 AirCasting - Share your Air!
 Copyright (C) 2011-2012 HabitatMap, Inc.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 You can contact the authors by email at <info@habitatmap.org>
 */
package pl.llp.aircasting.screens.stream.map;

import pl.llp.aircasting.model.Sensor;
import pl.llp.aircasting.model.internal.MeasurementLevel;
import pl.llp.aircasting.sensor.common.ThresholdsHolder;

import com.google.inject.Inject;

public class SoundHelper
{
  @Inject ThresholdsHolder thresholds;

  public MeasurementLevel level(Sensor sensor, double value)
  {
    return sensor.level(value);
  }

  public boolean shouldDisplay(Sensor sensor, double value)
  {
    MeasurementLevel measurementLevel = thresholds.getLevel(sensor, value);
    return measurementLevel != MeasurementLevel.VERY_HIGH && measurementLevel != MeasurementLevel.TOO_LOW;
  }
}
