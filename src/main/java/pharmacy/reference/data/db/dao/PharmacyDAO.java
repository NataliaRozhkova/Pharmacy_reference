package pharmacy.reference.data.db.dao;

import org.hibernate.Session;
import pharmacy.reference.data.Response;
import pharmacy.reference.data.entity.Pharmacy;

import javax.persistence.PersistenceException;
import java.util.List;

public class PharmacyDAO {
    private final Session session;

    public PharmacyDAO(Session session) {
        this.session = session;
    }

    public Response<String> create(final Pharmacy pharmacy) {
        session.beginTransaction();
        Response<String> response;
        try {
            session.save(pharmacy);
            session.getTransaction().commit();
            response = new Response<>("Success", Response.State.SUCCESS);
        } catch (IllegalArgumentException ex) {
            response = new Response<>(ex.getMessage(), Response.State.ERROR);
        } catch (PersistenceException e) {
            response = new Response<>("An Element with this id already exists", Response.State.ERROR);
        } finally {
            session.close();
        }
        return response;
    }

    public Response<List<Pharmacy>> readAll() {
        session.beginTransaction();
        List<Pharmacy> pharmacies = session.createQuery("FROM pharmacies").list();
        session.getTransaction().commit();
        session.close();
        return new Response<>(pharmacies, Response.State.SUCCESS);
    }

    public Response<Pharmacy> readById(final long id) {
        session.beginTransaction();
        Response<Pharmacy> response;
        try {
            Pharmacy pharmacy = session.get(Pharmacy.class, id);
            session.getTransaction().commit();
            if (pharmacy == null) {
                response = new Response<>(null, Response.State.ERROR);
            } else {
                response = new Response<>(pharmacy, Response.State.SUCCESS);
            }
        } catch (PersistenceException e) {
            response = new Response<>(null, Response.State.ERROR);
        } finally {
            session.close();
        }
        return response;
    }

    public Response<String> delete(long id) {
        session.beginTransaction();
        Pharmacy pharmacy = session.get(Pharmacy.class, id);
        session.getTransaction().commit();
        Response<String> response;
        if (pharmacy != null) {
            try {
                session.beginTransaction();
                session.delete(pharmacy);
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

    public Response<String> update(Pharmacy pharmacy) {
        session.beginTransaction();
        Response<String> response;
        try {
            session.update(pharmacy);
            session.getTransaction().commit();
            response = new Response<>("Success", Response.State.SUCCESS);
        } catch (PersistenceException e) {
            response = new Response<>(e.getCause().getMessage(), Response.State.ERROR);
        } finally {
            session.close();
        }
        return response;

    }


}

