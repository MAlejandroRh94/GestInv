package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.BroadcastReceiver.NotificationBroadcast;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.Calendar;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }


    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        DatabaseManager db;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Preference button = findPreference("cargar_datos_prueba");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    db = DatabaseManager.getInstance(getActivity());
                    db.openDB();
                    if (db.cargarDatosPrueba()) {
                        Toast.makeText(getActivity(), "Datos de prueba cargados correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Los Datos de prueba no han sido cargados correctamente", Toast.LENGTH_SHORT).show();
                    }
                    db.closeDB();
                    return true;
                }
            });

            button = findPreference("borrar_base_datos");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    db = DatabaseManager.getInstance(getActivity());
                    db.openDB();
                    db.eliminarDB();
                    db.closeDB();
                    return true;
                }
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("pref_ajustar_inventario")) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String cantDias = sharedPref.getString("pref_ajustar_inventario", "0");
                AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getActivity(), NotificationBroadcast.class);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
                Calendar calendar = Calendar.getInstance();
                alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        (AlarmManager.INTERVAL_DAY * Integer.parseInt(cantDias)), alarmIntent);

                Toast.makeText(getActivity(), "Notificacion de aviso de ajuste de inventario establecida cada " + cantDias + " dia/s", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
