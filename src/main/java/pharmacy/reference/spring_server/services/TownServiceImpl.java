package pharmacy.reference.spring_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacy.reference.spring_server.entitis.Town;
import pharmacy.reference.spring_server.repositories.TownRepository;

import java.util.List;

@Service("townService")
public class TownServiceImpl implements TownService{

    private TownRepository townRepository;

    @Override
    public Town findById(Long id) {
        return townRepository.findById(id).get();
    }

    @Override
    public List<Town> findAll() {
        return townRepository.findAll();
    }

    @Override
    public Town save(Town town) {
        return townRepository.save(town);
    }

    @Autowired
    public void setTownRepository(TownRepository townRepository) {
        this.townRepository = townRepository;
    }
}
