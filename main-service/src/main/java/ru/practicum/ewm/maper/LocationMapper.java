package ru.practicum.ewm.maper;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.location.LocationEvent;
import ru.practicum.ewm.entity.Location;

@Service
public class LocationMapper {

    public LocationEvent fromEntityToLocationEvent(Location location) {
        LocationEvent locationEvent = new LocationEvent();

        locationEvent.setLat(location.getLat());
        locationEvent.setLon(location.getLon());

        return locationEvent;
    }

    public Location fromLocationEventToEntity(LocationEvent locationEvent) {
        Location location = new Location();

        location.setLat(locationEvent.getLat());
        location.setLon(locationEvent.getLon());

        return location;
    }
}
