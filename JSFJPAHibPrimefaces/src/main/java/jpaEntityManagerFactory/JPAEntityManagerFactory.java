package jpaEntityManagerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAEntityManagerFactory {

private static EntityManagerFactory factory = null;
	
	static {
		if(factory == null) {
			factory = Persistence.createEntityManagerFactory("JSFJPAHibPrimefaces");
		}
	}
	
	public static EntityManager getEntityManager() {
		return factory.createEntityManager();
	}
	
	//Método para passar o id do objeto a ser excluído
	public static Object getId(Object entidade) {
		return factory.getPersistenceUnitUtil().getIdentifier(entidade);
	}
}
