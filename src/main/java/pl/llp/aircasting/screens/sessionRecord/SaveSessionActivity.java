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
package pl.llp.aircasting.screens.sessionRecord;

import pl.llp.aircasting.Intents;
import pl.llp.aircasting.R;
import pl.llp.aircasting.screens.common.helpers.SettingsHelper;
import pl.llp.aircasting.screens.common.sessionState.CurrentSessionManager;
import pl.llp.aircasting.model.Session;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.inject.Inject;

import pl.llp.aircasting.screens.common.ApplicationState;
import pl.llp.aircasting.screens.common.base.DialogActivity;
import roboguice.inject.InjectView;

public class SaveSessionActivity extends DialogActivity implements View.OnClickListener
{
  @InjectView(R.id.save_button) Button saveButton;
  @InjectView(R.id.discard_button) Button discardButton;

  @InjectView(R.id.session_title) EditText sessionTitle;
  @InjectView(R.id.session_tags) EditText sessionTags;

  @Inject CurrentSessionManager currentSessionManager;
  @Inject SettingsHelper settingsHelper;

  @Inject
  ApplicationState state;

  private long sessionId;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    Session session = currentSessionManager.getCurrentSession();
    currentSessionManager.pauseSession();

    setContentView(R.layout.session_details);
    initDialogToolbar("Save Session");

    saveButton.setOnClickListener(this);
    discardButton.setOnClickListener(this);

    sessionTitle.setText(session.getTitle());
    sessionTags.setText(session.getTags());

    if (settingsHelper.isContributingToCrowdMap()) {
      discardButton.setVisibility(View.GONE);
    }
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    if(!getIntent().hasExtra(Intents.SESSION_ID))
    {
      throw new RuntimeException("Should have arrived here with a session id");
    }

    sessionId = getIntent().getLongExtra(Intents.SESSION_ID, 0);
    state.saving().markCurrentlySaving(sessionId);
  }

  @Override
  public void onBackPressed()
  {
    currentSessionManager.continueSession();
    finish();
  }

  @Override
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.save_button:
      {
        fillSessionDetails(sessionId);
        Session session = currentSessionManager.getCurrentSession();
        if(session.isLocationless()) {
          currentSessionManager.finishSession(sessionId, false);
        }
        else if (settingsHelper.isContributingToCrowdMap()) {
          currentSessionManager.finishSession(sessionId, true);
        }
        else {
          Intents.contribute(this, sessionId);
        }
        break;
      }
      case R.id.discard_button:
      {
        currentSessionManager.discardSession(sessionId);
        break;
      }
    }
    finish();
  }

  private void fillSessionDetails(long sessionId)
  {
    String title = sessionTitle.getText().toString();
    String tags = sessionTags.getText().toString();
    currentSessionManager.setTitleTags(sessionId, title, tags);
  }
}


