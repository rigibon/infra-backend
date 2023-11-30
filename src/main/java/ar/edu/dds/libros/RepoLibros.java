package ar.edu.dds.libros;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class RepoLibros {
	  /**
     * Para mejor comprensión de las consultas, este link es un buen punto de inicio
     * https://www.arquitecturajava.com/jpa-criteria-api-un-enfoque-diferente/
     */

    private EntityManager entityManager;

    public RepoLibros(EntityManager em) {
        this.entityManager = em;
    }

    public void delete(Libro libro) {
        this.entityManager.remove(libro);
    }

    public Long count() {
        //https://stackoverflow.com/questions/2883887/in-jpa-2-using-a-criteriaquery-how-to-count-results
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(Libro.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    public boolean existsById(Long id) {
        return entityManager.find(Libro.class,id) != null;

    }

    public Libro findById(Long id) {
        return entityManager.find(Libro.class,id) ;
    }  

    public Libro findByName(String name) {
        return entityManager.find(Libro.class, name);
    }

    public Collection<Libro> findAll( ) {

        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Libro> consulta = cb.createQuery(Libro.class);
        Root<Libro> libros = consulta.from(Libro.class);
        return this.entityManager.createQuery(consulta.select(libros)).getResultList();

    }

    public Libro save(Libro libro) {
        this.entityManager.persist(libro);
        return libro;
    }
}
