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
package pl.llp.aircasting.model;

import pl.llp.aircasting.InjectedTestRunner;
import pl.llp.aircasting.screens.common.sessionState.CurrentSessionManager;
import pl.llp.aircasting.screens.common.helpers.LocationHelper;

import android.location.Location;
import android.location.LocationManager;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(InjectedTestRunner.class)
public class VisibleSessionManagerMakeANoteTest
{
  @Inject
  CurrentSessionManager currentSessionManager;
  private Location location;
  private Date date;

  @Before
  public void setup()
  {
    location = new Location(LocationManager.GPS_PROVIDER);
    location.setLatitude(50);
    location.setLongitude(20);

    currentSessionManager.locationHelper = mock(LocationHelper.class);
    when(currentSessionManager.locationHelper.getLastLocation()).thenReturn(location);
    currentSessionManager.eventBus = mock(EventBus.class);
    currentSessionManager.startMobileSession(null, null, false);

    date = new Date();
  }

  @Test
  public void shouldStoreNotes()
  {
    Note expected = new Note(date, "Note text", location, "some file");

    currentSessionManager.makeANote(date, "Note text", "some file");

    assertThat(currentSessionManager.getCurrentSession().getNotes()).contains(expected);
  }

  @Test
  public void shouldReturnTheCreatedNote()
  {
    Note expected = new Note(date, "Note text", location, "some file", 0);

    assertThat(currentSessionManager.makeANote(date, "Note text", "some file")).isEqualTo(expected);
  }

  @Test
  public void shouldNumberNotes()
  {
    Note expected1 = new Note(date, "first", location, null, 0);
    Note expected2 = new Note(date, "second", location, null, 1);

    currentSessionManager.makeANote(date, "first", null);
    currentSessionManager.makeANote(date, "second", null);

    Session session = currentSessionManager.currentSession;
    assertThat(session.getNotes()).contains(expected1, expected2);
  }
}
