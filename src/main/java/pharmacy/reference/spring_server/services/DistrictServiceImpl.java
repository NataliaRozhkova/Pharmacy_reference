package pharmacy.reference.spring_server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacy.reference.spring_server.entitis.District;
import pharmacy.reference.spring_server.repositories.DistrictRepository;

import java.util.List;

@Service("districtService")
public class DistrictServiceImpl implements DistrictService{

    private DistrictRepository districtRepository;

    @Override
    public List<District> findAll() {
        return districtRepository.findAll();
    }

    @Override
    public District findById(Long id) {
        return districtRepository.findById(id).get();
    }

    @Override
    public District save(District district) {
        return districtRepository.save(district);
    }

    @Autowired
    public void setDistrictRepository(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }
}
