package pharmacy.reference.data.db.dao;

import org.hibernate.Session;
import org.hibernate.TransientPropertyValueException;
import pharmacy.reference.data.Response;
import pharmacy.reference.data.entity.Medicine;

import javax.persistence.PersistenceException;
import java.util.List;

public class MedicineDAO {
    private final Session session;

    public MedicineDAO(Session session) {
        this.session = session;
    }

    public Response<String> create(final Medicine medicine) {
        session.beginTransaction();
        Response<String> response;

        try {
            session.save(medicine);
            session.getTransaction().commit();
            response = new Response<>("Success", Response.State.SUCCESS);
        } catch (TransientPropertyValueException e) {
            response = new Response<>("Pharmacy not found", Response.State.ERROR);
        } catch (PersistenceException e) {
            response = new Response<>(e.getMessage(), Response.State.ERROR);
        } finally {
            session.close();
        }
        return response;
    }

    public Response<Medicine> readById(final long id) {
        session.getTransaction();
        Response<Medicine> response;
        try {
            Medicine medicine = session.get(Medicine.class, id);
            session.getTransaction().commit();
            if (medicine == null) {
                response = new Response<>(null, Response.State.ERROR);
            } else {
                response = new Response<>(medicine, Response.State.SUCCESS);
            }
        } catch (PersistenceException e) {
            response = new Response<>(null, Response.State.ERROR);
        } finally {
            session.close();
        }
        return response;
    }

    public Response<String> delete(final long id) {
        session.beginTransaction();
        Response<String> response;
        Medicine medicine = session.get(Medicine.class, id);
        session.getTransaction().commit();
        if (medicine != null) {
            try {
                session.beginTransaction();
                session.delete(medicine);
                session.getTransaction().commit();
                response = new Response<>("Success", Response.State.SUCCESS);
            } catch (Exception e) {
                response = new Response<>(e.getMessage(), Response.State.ERROR);
            } finally {
                session.close();
            }
        } else {
            response = new Response<>("Element not found", Response.State.ERROR);
        }
        return response;
    }

    public Response<String> update(Medicine medicine) {
        session.beginTransaction();
        Response<String> response;
        try {
            session.update(medicine);
            session.getTransaction().commit();
            response = new Response<>("Success", Response.State.SUCCESS);
        } catch (PersistenceException e) {
            response = new Response<>(e.getCause().getMessage(), Response.State.ERROR);
        } finally {
            session.close();
        }
        return response;

    }

    public Response<List<Medicine>> readAll() {
        session.beginTransaction();
        List<Medicine> medicines = session.createQuery("FROM Medicine").list();
        session.getTransaction().commit();
        session.close();
        return new Response<>(medicines, Response.State.SUCCESS);
    }

    public Response<List<Medicine>> readWithFilterParameter(String value) {
        session.beginTransaction();
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM Medicine i JOIN FETCH i.pharmacy");
        query.append(setFilterParameter(value));
        List<Medicine> securities = session.createQuery(query.toString(), Medicine.class).getResultList();
        ;
        session.close();
        return new Response<>(securities, Response.State.SUCCESS);

    }

    private String setFilterParameter(String value) {
        return " WHERE lower(i.name)  LIKE  lower(\'%" + value + "%\')";

    }


}
