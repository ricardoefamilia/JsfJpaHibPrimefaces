package dao;

import java.util.List;
import javax.persistence.Query;
import model.Pessoa;

public class DaoPessoa<E> extends DaoGenerico<Pessoa> {

	public void removerPessoa(Pessoa pessoa) throws Exception{
		//removendo os FK para depois os PK referidos.
		getEntityManager().getTransaction().begin();
		String sqlDeleteDependente = "delete from dependente where pessoa_id = " + pessoa.getId();
		getEntityManager().createNativeQuery(sqlDeleteDependente).executeUpdate();
		getEntityManager().getTransaction().commit();
		super.excluir(pessoa);
		
		getEntityManager().getTransaction().begin();
		getEntityManager().remove(pessoa);
		getEntityManager().getTransaction().commit();
	}

	public List<Pessoa> pesquisar(String pesquisa){
		Query query = super.getEntityManager().createQuery("from Pessoa where nome like '%" + 
				pesquisa + "%' ");
		return query.getResultList();
	}
}
