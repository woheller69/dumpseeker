package org.woheller69.lavatories.api.openstreetmap;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import org.woheller69.lavatories.database.Lavatory;
import org.woheller69.lavatories.api.IDataExtractor;

import java.nio.charset.StandardCharsets;


public class OSMDataExtractor implements IDataExtractor {

    @Override
    public boolean wasCityFound(String data) {
        try {
            JSONObject json = new JSONObject(data);
            return json.has("elements");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Lavatory extractLavatory(String data, int cityId, Context context) {
        try {
            //fix issues with Ã¼ instead of ü, etc. OSM data is UTF-8 encoded
            //Overpass-API does not provide info about utf-8 charset in header
            //String(byte[] bytes, Charset charset) constructs a new String by decoding the specified array of bytes using the specified charset.
            data = (new String(data.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            Lavatory lavatory = new Lavatory();
            lavatory.setTimestamp((long) ((System.currentTimeMillis())/ 1000));

            JSONObject json = new JSONObject(data);
            if (!json.has("tags")) return null;

            lavatory.setOperator(" ");
            lavatory.setOpeningHours(" ");
            lavatory.setAddress1(" ");
            lavatory.setAddress2(" ");

            lavatory.setUuid(json.getString("type").equals("node") ? "N" + json.getString("id") : "W" + json.getString("id"));
            JSONObject tags = json.getJSONObject("tags");
            if (tags.has("amenity") && tags.getString("amenity").contains("sanitary_dump_station")) {
                if (tags.has("operator")) lavatory.setOperator(tags.getString("operator"));
                else if (tags.has("name")) lavatory.setOperator(tags.getString("name"));
                if (tags.has("network")) lavatory.setOperator(lavatory.getOperator()+" "+tags.getString("network"));  //Todo: Make separate database item and TextView
                if (tags.has("opening_hours")) lavatory.setOpeningHours(tags.getString("opening_hours"));
                if (tags.has("access")){
                    if (tags.getString("access").contains("customers") || tags.getString("access").contains("destination")) lavatory.setOpeningHours("Customers only " + lavatory.getOpeningHours()); //TODO extract string resource
                    else if (tags.getString("access").contains("network")) lavatory.setOpeningHours("Network only " + lavatory.getOpeningHours());  //TODO extract string resource
                    else if (!tags.getString(("access")).contains("yes")) return null ;
                }

                if ((tags.has("sanitary_dump_station:chemical_toilet") && !tags.getString(("sanitary_dump_station:chemical_toilet")).equals("no")) ||
                        (tags.has("chemical_toilet") && !tags.getString(("chemical_toilet")).equals("no")) ||
                        (tags.has("sanitary_dump_station:basin") && !tags.getString(("sanitary_dump_station:basin")).equals("no")) ||
                        (tags.has("sanitary_dump_station:round_drain") && !tags.getString(("sanitary_dump_station:round_drain")).equals("no")) ||
                        (tags.has("sanitary_dump_station:accepted") && tags.getString(("sanitary_dump_station:accepted")).contains("black_water"))) lavatory.setChemicalToilet(true);

                if ((tags.has("sanitary_dump_station:water_point") && !tags.getString(("sanitary_dump_station:water_point")).equals("no")) ||
                        (tags.has("water_point") && !tags.getString(("water_point")).equals("no"))) lavatory.setWaterPoint(true);

                if ((tags.has("sanitary_dump_station:fee") && !tags.getString(("sanitary_dump_station:fee")).equals("no")) ||
                    (tags.has("fee") && !tags.getString(("fee")).equals("no"))) lavatory.setPaid(true);

                return lavatory;
            } else if (tags.has("sanitary_dump_station") && tags.getString("sanitary_dump_station").contains("yes")){
                if (tags.has("name")) lavatory.setOperator(tags.getString("name"));
                if (tags.has("opening_hours")) lavatory.setOpeningHours(tags.getString("opening_hours"));


                if (tags.has("access")){
                    if (tags.getString("access").contains("customers") || tags.getString("access").contains("destination")) lavatory.setOpeningHours("Customers only " + lavatory.getOpeningHours()); //TODO extract string resource
                    else if (tags.getString("access").contains("network")) lavatory.setOpeningHours("Network only " + lavatory.getOpeningHours());  //TODO extract string resource
                    else if (!tags.getString(("access")).contains("yes")) return null ;
                }

                if ((tags.has("sanitary_dump_station:water_point") && !tags.getString(("sanitary_dump_station:water_point")).equals("no")) ||
                        (tags.has("water_point") && !tags.getString(("water_point")).equals("no"))) lavatory.setWaterPoint(true);

                if ((tags.has("sanitary_dump_station:chemical_toilet") && !tags.getString(("sanitary_dump_station:chemical_toilet")).equals("no")) ||
                        (tags.has("chemical_toilet") && !tags.getString(("chemical_toilet")).equals("no")) ||
                        (tags.has("sanitary_dump_station:basin") && !tags.getString(("sanitary_dump_station:basin")).equals("no")) ||
                        (tags.has("sanitary_dump_station:round_drain") && !tags.getString(("sanitary_dump_station:round_drain")).equals("no")) ||
                        (tags.has("sanitary_dump_station:accepted") && tags.getString(("sanitary_dump_station:accepted")).contains("black_water"))) lavatory.setChemicalToilet(true);

                if ((tags.has("sanitary_dump_station:fee") && !tags.getString(("sanitary_dump_station:fee")).equals("no")) ||
                        (tags.has("fee") && !tags.getString(("fee")).equals("no"))) lavatory.setPaid(true);

                return lavatory;
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
