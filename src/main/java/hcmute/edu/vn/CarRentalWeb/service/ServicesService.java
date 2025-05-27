package hcmute.edu.vn.CarRentalWeb.service;

import hcmute.edu.vn.CarRentalWeb.entity.Services;
import hcmute.edu.vn.CarRentalWeb.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicesService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Services> getAllServices(){
        return serviceRepository.findAll();
    }

    public List<Services> getServiceList(String status){
        return serviceRepository.findAllByStatus(status);
    }

    public Page<Services> getServicePage(String status, Pageable pageable){
        return serviceRepository.findAllByStatus(status, pageable);
    }

    public Services getServiceById(int id){
        return serviceRepository.findById(id);
    }
    public void save(Services service)
    {
        serviceRepository.save(service);
    }
    public void deleteById(int id)
    {
        serviceRepository.deleteById(id);
    }

}
