package com.example.opencovid.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class Covid {


    private Context appContext;
    private int countryIndex;

    public Covid(Context context) {
        this.appContext = context;
    }

    public int getCountryIndex() {
        return countryIndex;
    }

    public void setCountryIndex(int countryIndex) {
        this.countryIndex = countryIndex;
    }

    public String getCountry() {
        String myCountry = getCountry(appContext);
        return myCountry;

    }

    private String getCountry(Context context) {

        String country;

        LocationManager locationManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            @SuppressLint("MissingPermission") Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null) {
                location = networkLocation;
            }
            if (location == null) {
                Log.d("Covid()","Couldn't get location from network and gps providers");
                return "";
            }
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);

                if (addresses != null && !addresses.isEmpty()) {
                    country = addresses.get(0).getCountryName();
                    if (country != null) {
                        return country;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        country = getCountryBasedOnSimCardOrNetwork(context);
        if (country != null) {
            return country;
        }
        return "";
    }


    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    private String getCountryBasedOnSimCardOrNetwork(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

}
