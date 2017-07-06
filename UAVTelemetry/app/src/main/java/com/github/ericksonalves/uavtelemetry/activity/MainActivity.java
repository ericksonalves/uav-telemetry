package com.github.ericksonalves.uavtelemetry.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.github.ericksonalves.uavtelemetry.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.x_edit_text)
    public EditText xEditText;
    @BindView(R.id.y_edit_text)
    public EditText yEditText;
    @BindView(R.id.altitude_text_view)
    public TextView altitudeTextView;
    @BindView(R.id.battery_text_view)
    public TextView batteryTextView;
    @BindView(R.id.latitude_text_view)
    public TextView latitudeTextView;
    @BindView(R.id.longitude_text_view)
    public TextView longitudeTextView;
    @BindView(R.id.speed_text_view)
    public TextView speedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateMissionValues(0, 0);
        updateTelemetryValues(0, 0, 0, 0, 0);
    }

    private void updateMissionValues(int x, int y) {
        xEditText.setText(String.valueOf(x));
        yEditText.setText(String.valueOf(y));
    }

    private void updateTelemetryValues(double altitude, double battery, double latitude, double
            longitude, double speed) {
        altitudeTextView.setText(String.valueOf(altitude));
        batteryTextView.setText(String.valueOf(battery));
        latitudeTextView.setText(String.valueOf(latitude));
        longitudeTextView.setText(String.valueOf(longitude));
        speedTextView.setText(String.valueOf(speed));
    }
}
