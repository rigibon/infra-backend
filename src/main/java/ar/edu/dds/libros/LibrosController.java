package ar.edu.dds.libros;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import io.javalin.http.Context;

import java.util.Collection;
import java.util.List;

public class LibrosController {

	private EntityManagerFactory entityManagerFactory;

	public LibrosController(EntityManagerFactory entityManagerFactory) {
		super();
		this.entityManagerFactory = entityManagerFactory;
	}

	public void searchLibro(Context ctx) {
		EntityManager em = entityManagerFactory.createEntityManager();

		RepoLibros repo = new RepoLibros(em);

		Collection<Libro> libros = repo.findAll();

		String keyword = ctx.pathParam("keyword");

		Libro libro = libros.stream()
				.filter(user -> user.getNombre().equals(keyword))
				.findFirst()
				.orElse(null);

		if (libro != null) {
			ctx.json(libro);

			ctx.status(201);
		} else {
//				ctx.status(404);
		}
	}

	public  void listLibros(Context ctx) {
		EntityManager em = entityManagerFactory.createEntityManager();
		
		RepoLibros repo = new RepoLibros(em);

		Collection<Libro> libros = repo.findAll();

		ctx.json(libros);

		em.close();
	}

	public void getLibro(Context ctx) {
		String id = ctx.pathParam("id");

		EntityManager em = entityManagerFactory.createEntityManager();

		RepoLibros repo = new RepoLibros(em);

		Libro libro = repo.findById(Long.parseLong(id));

		if (libro != null) {
			ctx.json(libro);

			ctx.status(201);
		} else {
			ctx.status(405);
		}

		em.close();
	}

	public  void addLibro(Context ctx) {
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
	    RepoLibros repo = new RepoLibros(em);
	    Libro libro = ctx.bodyAsClass(Libro.class);
	    repo.save(libro);
		ctx.json(libro);
	    ctx.status(201);
	    em.getTransaction().commit();
	    em.close();
	}

	public  void deleteLibro(Context ctx) {
		String id = ctx.pathParam("id");
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		RepoLibros repo = new RepoLibros(em);

		Libro libro = repo.findById(Long.parseLong(id));

		if (libro != null) {
			repo.delete(libro);

			ctx.status(204);
		} else {
			ctx.status(405);
		}

		em.getTransaction().commit();
		em.close();
	}

	public void patchLibro(Context ctx) {
		String id = ctx.pathParam("id");

		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();

		RepoLibros repo = new RepoLibros(em);

		Libro libro = repo.findById(Long.parseLong(id));

		if (libro == null) {
			ctx.status(405);
			return;
		}

		Libro data = ctx.bodyAsClass(Libro.class);

		if (data.getAutor() != null) {
			libro.setAutor(data.getAutor());
		}

		if (data.getNombre() != null) {
			libro.setNombre(data.getNombre());
		}

		ctx.json(libro);

		ctx.status(201);

		em.getTransaction().commit();
		em.close();
	}
}