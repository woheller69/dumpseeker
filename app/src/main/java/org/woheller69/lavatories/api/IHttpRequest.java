package org.woheller69.lavatories.api;

/**
 * This generic interface is for making an HTTP request to some API, process the data and
 * finally trigger some mechanism to update the UI.
 */
public interface IHttpRequest {

    /**
     * @param lat The latitude of the city to get the data for.
     * @param lon The longitude of the city to get the data for.
     */
    void perform(float lat, float lon, int cityId);

}
