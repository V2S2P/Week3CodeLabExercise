package app.DAO;

import app.entities.Course;
import app.entities.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TeacherDAO implements IDAO<Teacher, Integer> {

    private final EntityManagerFactory emf;

    public TeacherDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Teacher create(Teacher teacher) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(teacher);
            em.getTransaction().commit();
        }
        return teacher;
    }

    @Override
    public boolean update(Teacher teacher) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(teacher);
            em.getTransaction().commit();
        }
        return true;
    }

    @Override
    public boolean delete(Teacher teacher) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(teacher);
            em.getTransaction().commit();
        }
        return true;
    }

    @Override
    public Teacher find(Integer id) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Teacher teacher = em.find(Teacher.class, id);
            em.getTransaction().commit();
            return teacher;
        }
    }

    @Override
    public List<Teacher> getAll() {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            TypedQuery<Teacher> query = em.createQuery("SELECT t FROM Teacher t", Teacher.class);
            List<Teacher> teachers = query.getResultList();
            em.getTransaction().commit();
            return teachers;
        }
    }
}
