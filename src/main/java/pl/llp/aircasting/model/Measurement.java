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
package pl.llp.aircasting.model;

import pl.llp.aircasting.util.Constants;

import com.google.common.base.Predicate;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class Measurement {
    @Expose private double latitude;
    @Expose private double longitude;
    @Expose private double value;
    @Expose private Date time;

    @Expose
    @SerializedName("measured_value")
    private double measuredValue;

    private transient Long seconds;

    public Measurement(double value) {
        this(0, 0, value);
    }

    public Measurement(double latitude, double longitude, double value) {
        this(latitude, longitude, value, new Date());
    }

    public Measurement(double latitude, double longitude, double value, Date time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.value = value;
        setTime(time);
    }

    public Measurement(double latitude, double longitude, double value, double measuredValue, Date time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.value = value;
        this.measuredValue = measuredValue;
        setTime(time);
    }

    public double getMeasuredValue() {
        return measuredValue;
    }

    public void setMeasuredValue(double measuredValue) {
        this.measuredValue = measuredValue;
    }

    public Measurement() {
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getLongitude() {

        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {

        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measurement that = (Measurement) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;
        if (Double.compare(that.value, value) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = latitude != +0.0d ? Double.doubleToLongBits(latitude) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = longitude != +0.0d ? Double.doubleToLongBits(longitude) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = value != +0.0d ? Double.doubleToLongBits(value) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", value=" + value +
                ", time=" + time +
                '}';
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    /*
    * seconds since time 0
    **/
    public long getSecond() {
        if (seconds == null && time != null) {
            seconds = getTime().getTime() / Constants.MILLIS_IN_SECOND;
        }
        return seconds;
    }

    public static Predicate<Measurement> timeFitsIn(final long start, final long end) {
        return new Predicate<Measurement>() {
            @Override
            public boolean apply(@Nullable Measurement measurement) {
                if (measurement == null) {
                    return false;
                }

                return start <= measurement.getTime().getTime() && measurement.getTime().getTime() <= end;
            }
        };
    }

    public int getMilliseconds() {
        return (int) (time.getTime() % Constants.MILLIS_IN_SECOND);
    }
}
