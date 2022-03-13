package controllerBean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoDependente;
import dao.DaoPessoa;
import model.Dependente;
import model.Pessoa;

@ManagedBean(name = "dependenteManagedBean")
@ViewScoped
public class DependenteManagedBean {

	private Pessoa pessoa = new Pessoa();
	private DaoPessoa<Pessoa> daoPessoa = new DaoPessoa<Pessoa>();
	private Dependente dependente = new Dependente();
	List<Dependente> listaDependentes = new ArrayList<Dependente>();
	private DaoDependente<Dependente> daoDependente = new DaoDependente<Dependente>();
	
	public void setDaoDependente(DaoDependente<Dependente> daoDependente) {
		this.daoDependente = daoDependente;
	}
	public DaoDependente<Dependente> getDaoDependente() {
		return daoDependente;
	}
	
	public List<Dependente> getListaDependentes() {
		return listaDependentes;
	}

	public void setListaDependentes(List<Dependente> listaDependentes) {
		this.listaDependentes = listaDependentes;
	}

	@PostConstruct
	public void init() {
		//captura o parâmetro idPessoa passado no formulário 
		String idPessoa = FacesContext.getCurrentInstance().getExternalContext().
				getRequestParameterMap().get("idPessoa");
		//System.out.println("IdP: " + idPessoa);
		//hibernate carrega inner join de pessoa e dependente
		pessoa = daoPessoa.pesquisarEntidadePorId(Long.parseLong(idPessoa), Pessoa.class);
	}
	
	public String salvar() {
		dependente.setPessoa(pessoa);
		daoDependente.inserirAtualizar(dependente);
		pessoa = daoPessoa.pesquisarEntidadePorId(pessoa.getId(), Pessoa.class);
		limpar();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem: ", "Dados gravados com sucesso!"));
		return "";
	}
	public String excluir() {
		daoDependente.excluir(dependente);
		pessoa = daoPessoa.pesquisarEntidadePorId(pessoa.getId(), Pessoa.class);
		limpar();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem: ", "Excluído com sucesso!"));
		return "";
	}
	
	public void limpar() {
		dependente = new Dependente();
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Dependente getDependente() {
		return dependente;
	}

	public void setDependente(Dependente dependente) {
		this.dependente = dependente;
	}
	
	
}
