package dao;

import java.util.List;
import javax.persistence.Query;

import model.Funcionario;
import model.Pessoa;

public class DaoFuncionario<E> extends DaoGenerico<Funcionario> {

	public void removerFuncionario(Funcionario funcionario) throws Exception{
		//removendo os FK para depois os PK referidos.
		getEntityManager().getTransaction().begin();
		getEntityManager().remove(funcionario);
		getEntityManager().getTransaction().commit();
	}

	public List<Funcionario> pesquisar(String pesquisa){
		Query query = super.getEntityManager().createQuery("from Funcionario where nome like '%" + 
				pesquisa + "%' ");
		return query.getResultList();
	}
}
