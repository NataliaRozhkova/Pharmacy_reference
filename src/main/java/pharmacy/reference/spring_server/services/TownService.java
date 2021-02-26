package pharmacy.reference.spring_server.services;

import pharmacy.reference.spring_server.entitis.Town;

import java.util.List;

public interface TownService {

    Town findById(Long id);

    List<Town> findAll();

    Town save(Town town);

}
