package ca.cmpt213.tokimon_server.controller;

import java.util.ArrayList;
import java.util.List;

public class BikeManager implements Filter {
    private List<Bike> bikes = new ArrayList<>();

    public void add(Bike bike) {
        bikes.add(bike);
    }

    public boolean contains(Bike bike) {
        return bikes.contains(bike);
    }

    public List<Bike> getFiltered(Filter filter) {

        List<Bike> filteredList = new ArrayList<>();
        for(int i=0;i<bikes.size();i++){
            if(filter.include(bikes.get(i)))
                filteredList.add(bikes.get(i));
        }
        return filteredList;
    }

    @Override
    public void include(Bike bike) {
        return false;
    }
}