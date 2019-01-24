package pl.llp.aircasting.screens.settings;

import pl.llp.aircasting.R;
import pl.llp.aircasting.screens.common.base.DialogActivity;
import pl.llp.aircasting.screens.common.helpers.SettingsHelper;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import com.google.inject.Inject;
import roboguice.inject.InjectView;

public class DisableMapSettingsActivity extends DialogActivity
{
  @InjectView(R.id.disable_maps_checkbox) CheckBox checkBox;
  @InjectView(R.id.disable_maps_ok) Button button;

  @Inject Application context;

  @Inject
  SettingsHelper settings;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.disable_map_settings);
    initDialogToolbar("Disable Maps");
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    checkBox.setChecked(settings.areMapsDisabled());
    button.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        settings.setDisableMaps(checkBox.isChecked());
        finish();
      }
    });
  }
}
