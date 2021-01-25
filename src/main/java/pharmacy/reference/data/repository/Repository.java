package pharmacy.reference.data.repository;

import pharmacy.reference.data.Response;
import pharmacy.reference.data.db.PharmacyReferenceDataSource;
import pharmacy.reference.data.db.dao.MedicineDAO;
import pharmacy.reference.data.db.dao.PharmacyDAO;
import pharmacy.reference.data.entity.Medicine;
import pharmacy.reference.data.entity.Pharmacy;

import java.util.List;

public class Repository {
    public final PharmacyReferenceDataSource dataSource;

    public Repository(PharmacyReferenceDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Response<String> createPharmacy(final Pharmacy pharmacy) {
        return dataSource.createPharmacy(pharmacy);
    }

    public Response<String> createAllPharmacies(final List<Pharmacy> pharmacies) {
        return dataSource.createAllPharmacies(pharmacies);
    }

    public Response<Pharmacy> readPharmacy(final long id) {
        return dataSource.readPharmacy(id);
    }

    public Response<List<Pharmacy>> readAllPharmacies() {
        return dataSource.readAllPharmacies();
    }

    public Response<String> deletePharmacy(final long id) {
        return dataSource.deletePharmacy(id);
    }

    public Response<String> updatePharmacy(final Pharmacy pharmacy) {
        return dataSource.updatePharmacy(pharmacy);
    }

    public Response<String> createMedicine(final Medicine medicine) {
        return dataSource.createMedicine(medicine);
    }

    public Response<String> createAllMedicines(List<Medicine> medicines) {
        return dataSource.createAllMedicines(medicines);
    }

    public Response<Medicine> readMedicine(final long id) {
        return dataSource.readMedicine(id);
    }

    public Response<List<Medicine>> readAllMedicines() {
        return dataSource.readAllMedicines();
    }

    public Response<String> deleteMedicine(final long id) {
        return dataSource.deleteMedicine(id);
    }

    public Response<String> updateMedicine(final Medicine medicine) {
        return dataSource.updateMedicine(medicine);
    }

    public Response<List<Medicine>> readMedicineWithFilterParameter( String filterParameter) {
        return dataSource.readMedicineWithFilterParameter(filterParameter);
    }
}
