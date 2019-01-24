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
package pl.llp.aircasting.screens.userAccount;

import pl.llp.aircasting.Intents;
import pl.llp.aircasting.R;
import pl.llp.aircasting.screens.common.base.DialogActivity;
import pl.llp.aircasting.screens.common.base.SimpleProgressTask;
import pl.llp.aircasting.networking.drivers.UsersDriver;
import pl.llp.aircasting.networking.schema.UserInfo;
import pl.llp.aircasting.screens.common.helpers.SettingsHelper;
import pl.llp.aircasting.screens.common.ToastHelper;
import pl.llp.aircasting.networking.httpUtils.HttpResult;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.inject.Inject;
import roboguice.inject.InjectView;

import static pl.llp.aircasting.networking.httpUtils.Status.SUCCESS;

public class SignInActivity extends DialogActivity implements View.OnClickListener
{
  @InjectView(R.id.ok) Button ok;

  @InjectView(R.id.login) EditText loginField;
  @InjectView(R.id.password) EditText passwordField;

  @Inject UsersDriver userDriver;
  @Inject SettingsHelper settingsHelper;

  @Inject Application context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.sign_in);
    initDialogToolbar("Log In");

    ok.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.ok:
        if(validateInput()) {
          signIn();
        }
        break;
    }
  }

  private boolean validateInput() {
    if (getLogin().isEmpty() || getPassword().isEmpty()) {
      if (getLogin().isEmpty()) {
        ToastHelper.show(context, R.string.profile_name_or_email_field_blank, Toast.LENGTH_LONG);
      }

      if (getPassword().isEmpty()) {
        ToastHelper.show(context, R.string.password_blank, Toast.LENGTH_LONG);
      }

      return(false);
    }

    return(true);
  }

  private void signIn() {
    //noinspection unchecked
    new SimpleProgressTask<Void, Void, HttpResult<UserInfo>>(this) {

      @Override
      protected HttpResult<UserInfo> doInBackground(Void... voids) {
        HttpResult<UserInfo> result = userDriver.connect(getLogin(), getPassword());

        if (result.getStatus() == SUCCESS) {
          settingsHelper.setAuthToken(result.getContent().getAuthenticationToken());
          settingsHelper.setUserLogin(result.getContent().getUsername());
          settingsHelper.setUserEmail(result.getContent().getEmail());
        }

        return result;
      }

      @Override
      protected void onPostExecute(HttpResult<UserInfo> result) {
        super.onPostExecute(result);

        switch (result.getStatus()) {
          case ERROR:
            ToastHelper.show(context, R.string.unknown_error, Toast.LENGTH_LONG);
            break;
          case FAILURE:
            ToastHelper.show(context, R.string.login_error, Toast.LENGTH_LONG);
            break;
          default:
            syncInBackground();
            break;
        }
      }

      private void syncInBackground()
      {
        Intents.triggerSync(context);
        finish();
      }
    }.execute();
  }

  private String getLogin() {
    return loginField.getText().toString();
  }

  public String getPassword() {
    return passwordField.getText().toString();
  }
}
