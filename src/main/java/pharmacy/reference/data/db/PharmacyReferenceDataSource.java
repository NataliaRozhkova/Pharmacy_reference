package pharmacy.reference.data.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import pharmacy.reference.data.Response;
import pharmacy.reference.data.db.dao.MedicineDAO;
import pharmacy.reference.data.db.dao.PharmacyDAO;
import pharmacy.reference.data.entity.Medicine;
import pharmacy.reference.data.entity.Pharmacy;

import java.io.IOException;
import java.util.List;


public class PharmacyReferenceDataSource {

    private final String url;

    public final SessionFactory sessionFactory;

    public PharmacyReferenceDataSource(String url) {

        this.sessionFactory = buildSessionFactory();
        this.url = url;
    }

    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public Response<String> createPharmacy(final Pharmacy pharmacy) {
        return new PharmacyDAO(sessionFactory.openSession()).create(pharmacy);
    }

    public Response<String> createAllPharmacies(final List<Pharmacy> pharmacies) {
        StringBuilder response = new StringBuilder();
        int count = 0;
        for (Pharmacy s : pharmacies) {
            if (createPharmacy(s).state == Response.State.SUCCESS) count++;
        }
        return new Response<>("Добавлено записей " + count, Response.State.SUCCESS);
    }

    public Response<Pharmacy> readPharmacy(final long id) {
        return new PharmacyDAO(sessionFactory.openSession()).readById(id);
    }

    public Response<List<Pharmacy>> readAllPharmacies() {
        return new PharmacyDAO(sessionFactory.openSession()).readAll();
    }

    public Response<String> deletePharmacy(final long id) {
        return new PharmacyDAO(sessionFactory.openSession()).delete(id);
    }

    public Response<String> updatePharmacy(final Pharmacy pharmacy) {
        return new PharmacyDAO(sessionFactory.openSession()).update(pharmacy);
    }

    public Response<String> createMedicine(final Medicine medicine) {
        return new MedicineDAO(sessionFactory.openSession()).create(medicine);
    }

    public Response<String> createAllMedicines(List<Medicine> medicines) {
        StringBuilder response = new StringBuilder();
        int count = 0;
        for (Medicine t : medicines) {
            if (createMedicine(t).state == Response.State.SUCCESS) {
                count++;
            }
        }
        return new Response<>("Добавлено записей " + count, Response.State.SUCCESS);
    }

    public Response<Medicine> readMedicine(final long id) {
        return new MedicineDAO(sessionFactory.openSession()).readById(id);
    }

    public Response<List<Medicine>> readAllMedicines() {
        return new MedicineDAO(sessionFactory.openSession()).readAll();
    }

    public Response<String> deleteMedicine(final long id) {
        return new MedicineDAO(sessionFactory.openSession()).delete(id);
    }

    public Response<String> updateMedicine(final Medicine medicine) {
        return new MedicineDAO(sessionFactory.openSession()).update(medicine);
    }

    public Response<List<Medicine>> readMedicineWithFilterParameter( String filterParameter) {
        return new MedicineDAO(sessionFactory.openSession()).readWithFilterParameter( filterParameter);
    }


}
