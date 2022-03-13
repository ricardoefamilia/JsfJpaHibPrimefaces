package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import jpaEntityManagerFactory.JPAEntityManagerFactory;

@SuppressWarnings("unchecked")
public class DaoGenerico<E> {

	// criar/inserir dados em uma tabela, conforme entidade mapeada no
	// persistence.xml
	private EntityManager entityManager = JPAEntityManagerFactory.getEntityManager();

	// método para criar tabela e inserir dados
	public void inserir(E entidade) {
		// iniciar a transaction com BD
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		// inserir dados na entidade referenciada
		entityManager.persist(entidade);
		entityTransaction.commit();
		//entityManager.close();
	}

	// método para criar, inserir/atualizar dados
	public E inserirAtualizar(E entidade) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		// inserir/atualizar dados na entidade referenciada
		E retornaEntidade = entityManager.merge(entidade);
		entityTransaction.commit();
		//entityManager.close();
		return retornaEntidade;
	}

	// método para excluir dados
	public void excluir(E entidade) {
		// captura o id da entidade para exclusão dos dados
		Object id = JPAEntityManagerFactory.getId(entidade);
		
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		// excluir dados na entidade referenciada
		entityManager.createQuery("delete from " + entidade.getClass().getSimpleName() +
				" where id = " + id).executeUpdate();
		entityTransaction.commit();
		//entityManager.close();
	}

	// método para retornar um objeto com base nas informações da entidade,
	// capturando-se a PK
	public E pesquisarEntidade(E entidade) {
		Object id = JPAEntityManagerFactory.getId(entidade);
		E e = (E) entityManager.find(entidade.getClass(), id);
		return e;
	}

	// método para retornar um objeto com base na PK específica
	public E pesquisarEntidadePorId(Long id, Class<E> entidade) {
		//clear() limpa o cache do formulário (interface)
		entityManager.clear();
		E e = (E) entityManager.createQuery("from " + entidade.getSimpleName() + " where id = " + id).getSingleResult();
		return e;
	}

	// método para retornar registros de uma tabela em formato de lista de objetos
	public List<E> listarDadosEntidade(Class<E> entidade) {
		// iniciando a transaction
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		// preencher a lista de Objetos
		List<E> lista = entityManager.createQuery("from " + entidade.getName()).getResultList();

		entityTransaction.commit();
		//entityManager.close();

		return lista;

	}

	// método para acesso ao entityManager declarado
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
