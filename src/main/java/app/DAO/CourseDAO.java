package app.DAO;

import app.entities.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CourseDAO implements IDAO<Course, Integer> {
    private final EntityManagerFactory emf;

    public CourseDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Course create(Course course) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(course);
            em.getTransaction().commit();
        }
        return course;
    }

    @Override
    public boolean update(Course course) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.merge(course);
            em.getTransaction().commit();
        }
        return true;
    }

    @Override
    public boolean delete(Course course) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.remove(course);
            em.getTransaction().commit();
        }
        return true;
    }

    @Override
    public Course find(Integer id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Course course = em.find(Course.class, id);
            em.getTransaction().commit();
            return course;
        }
    }

    @Override
    public List<Course> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            TypedQuery<Course>  query = em.createQuery("SELECT c FROM Course c", Course.class);
            List<Course> courses = query.getResultList();
            em.getTransaction().commit();
            return courses;
        }
    }
}
