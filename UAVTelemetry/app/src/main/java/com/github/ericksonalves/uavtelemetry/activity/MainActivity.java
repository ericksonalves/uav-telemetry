package com.github.ericksonalves.uavtelemetry.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.ericksonalves.uavtelemetry.R;

import org.zeromq.ZMQ;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.generate_mission_button)
    public Button generateMissionButton;
    @BindView(R.id.start_listening_button)
    public Button startListeningButton;
    @BindView(R.id.stop_listening_button)
    public Button stopListeningButton;
    @BindView(R.id.computer_ip_edit_text)
    public EditText computerIpEditText;
    @BindView(R.id.computer_port_edit_text)
    public EditText computerPortEditText;
    @BindView(R.id.device_ip_edit_text)
    public EditText deviceIpEditText;
    @BindView(R.id.device_port_edit_text)
    public EditText devicePortEditText;
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

    private PublisherThread publisherThread;
    private SubscriberThread subscriberThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateMissionValues(0, 0);
        updateTelemetryValues(0, 0, 0, 0, 0);
        startListeningButton.setEnabled(true);
        stopListeningButton.setEnabled(false);
    }

    @OnClick(R.id.generate_mission_button)
    public void onGenerateMissionButtonClicked() {
        publisherThread = new PublisherThread();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                publisherThread.run();
            }
        });
    }

    @OnClick(R.id.start_listening_button)
    public void onStartListeningButtonClicked() {
        startListeningButton.setEnabled(false);
        stopListeningButton.setEnabled(true);
        computerIpEditText.setEnabled(false);
        computerPortEditText.setEnabled(false);
        deviceIpEditText.setEnabled(false);
        devicePortEditText.setEnabled(false);
        subscriberThread = new SubscriberThread();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                subscriberThread.run();
            }
        });
    }

    @OnClick(R.id.stop_listening_button)
    public void onStopListeningButtonClicked() {
        startListeningButton.setEnabled(true);
        stopListeningButton.setEnabled(false);
        computerIpEditText.setEnabled(true);
        computerPortEditText.setEnabled(true);
        deviceIpEditText.setEnabled(true);
        devicePortEditText.setEnabled(true);
        subscriberThread.interrupt();
        subscriberThread = null;
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

    private class PublisherThread extends Thread {
        @Override
        public void run() {
            try {
                ZMQ.Context context = ZMQ.context(1);
                ZMQ.Socket socket = context.socket(ZMQ.PUB);
                String ip = deviceIpEditText.getText().toString();
                String port = devicePortEditText.getText().toString();
                socket.bind("tcp://" + ip + ":" + port);
                String x = xEditText.getText().toString();
                String y = xEditText.getText().toString();
                while (!Thread.currentThread().isInterrupted()) {
                    socket.sendMore("mission".getBytes());
                    socket.send(x + ":" + y);
                }
                socket.close();
                context.term();
            } catch (Exception e) {
                Log.e(TAG, "Exception caught", e);
            }
        }
    }

    private class SubscriberThread extends Thread {
        @Override
        public void run() {
            try {
                ZMQ.Context context = ZMQ.context(1);
                ZMQ.Socket socket = context.socket(ZMQ.SUB);
                socket.subscribe("telemetry".getBytes());
                String ip = computerIpEditText.getText().toString();
                String port = computerPortEditText.getText().toString();
                socket.connect("tcp://" + ip + ":" + port);
                while (!Thread.currentThread().isInterrupted()) {
                    String topic = new String(socket.recv(0), ZMQ.CHARSET);
                    String message = new String(socket.recv(0), ZMQ.CHARSET);
                    if (topic.equals("telemetry")) {
                        final String[] parts = message.split(":");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                double altitude = Double.parseDouble(parts[0]);
                                double battery = Double.parseDouble(parts[1]);
                                double latitude = Double.parseDouble(parts[2]);
                                double longitude = Double.parseDouble(parts[3]);
                                double speed = Double.parseDouble(parts[4]);
                                updateTelemetryValues(altitude, battery, latitude, longitude,
                                        speed);
                            }
                        });
                    }
                }
                socket.close();
                context.term();
            } catch (Exception e) {
                Log.e(TAG, "Exception caught", e);
            }
        }
    }
}
