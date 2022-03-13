package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import jpaEntityManagerFactory.JPAEntityManagerFactory;

@WebFilter("/*")
public class FilterRequisicao implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		//System.out.println("*** O FilterRequisicao foi acionado ***");
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}


	public void init(FilterConfig fConfig) throws ServletException {
		JPAEntityManagerFactory.getEntityManager();
	}

}
