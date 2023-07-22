package eu.czsoft.legacygreecontrol;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
public class DeviceHelpActivity extends AppCompatActivity {
    private Feature feature;

    public enum Feature {
        UNKNOWN,
        AIR,
        HEALTH,
        DRY,
        SLEEP,
        QUIET,
        TURBO,
        SAVING,
        LIGHT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_help);

        feature = (Feature) getIntent().getSerializableExtra(DeviceActivity.EXTRA_FEATURE_HELP);

        updateTitle();
    }

    private void updateTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.device_help_title));
        sb.append(" ");

        switch (feature)
        {
            case AIR:
                sb.append(getString(R.string.device_help_air));
                break;

            case HEALTH:
                sb.append(getString(R.string.device_help_health));
                break;

            case DRY:
                sb.append(getString(R.string.device_help_dry));
                break;

            case SLEEP:
                sb.append(getString(R.string.device_help_sleep));
                break;

            case QUIET:
                sb.append(getString(R.string.device_help_quiet));
                break;

            case TURBO:
                sb.append(getString(R.string.device_help_turbo));
                break;

            case SAVING:
                sb.append(getString(R.string.device_help_saving));
                break;

            case LIGHT:
                sb.append(getString(R.string.device_help_light));
                break;

            case UNKNOWN:
                break;
        }

        setTitle(sb.toString());
    }
}
