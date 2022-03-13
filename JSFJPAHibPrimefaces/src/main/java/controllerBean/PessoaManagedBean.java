package controllerBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import com.google.gson.Gson;

import dao.DaoGenerico;
import dao.DaoPessoa;
import model.Pessoa;

@ManagedBean(name = "pessoaManagedBean")
@ViewScoped
public class PessoaManagedBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGenerico<Pessoa> daoGenerico = new DaoGenerico<Pessoa>();
	// atributo lista que receberá os dados da tabela Pessoa no méodo listarPessoa()
	List<Pessoa> listaPessoas = new ArrayList<Pessoa>();
	private DaoPessoa<Pessoa> daoPessoa = new DaoPessoa<Pessoa>();

	// atributo lista que receberá os dados da tabela Pessoa no méodo listarPessoa()
	public void setListaPessoas(List<Pessoa> listaPessoas) {
		this.listaPessoas = listaPessoas;
	}

	// método para receber os registros da tabela pessoa
	// carrega automaticamente a dataTable com os registros da tabela pessoa
	@PostConstruct
	public void init() {

		listaPessoas = daoGenerico.listarDadosEntidade(Pessoa.class);
	}

	// método carregar a lista de registros da tabela pessoa após método acima
	// carregarPessoas()
	public List<Pessoa> getListaPessoas() {
		return listaPessoas;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String salvar() {
		daoGenerico.inserirAtualizar(pessoa);
		limpar();
		listaPessoas.add(pessoa);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem: ", "Dados gravados com sucesso!"));
		return "";
	}

	public String excluir() {
		try {
			daoPessoa.removerPessoa(pessoa);
			listaPessoas.remove(pessoa);
			limpar();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem: ", "Excluído com sucesso!"));
		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Mensagem: ", "A pessoa possui " + "dependentes cadastrados"));
			} else {
				e.printStackTrace();
			}
		}

		return "";
	}

	public void limpar() {
		pessoa = new Pessoa();
	}

	public List<Pessoa> getLista() {
		listaPessoas = daoGenerico.listarDadosEntidade(Pessoa.class);
		return listaPessoas;
	}

	// método para retornar os dados de endereco com base no cep
	public void pesquisaCep(AjaxBehaviorEvent event) {
		// System.out.println("CEP: " + pessoa.getCep());

		try {
			// java.net
			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/");
			// conexão para enviar cep para viacep
			URLConnection conn = url.openConnection();
			// executa e retorna os dados de endereço
			InputStream is = conn.getInputStream();
			// ler e armazenar os dados em um buffer
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			// variável String que receberá os dados aninhados
			String endereco = "";
			// concatenar strings dinamicamente
			StringBuilder jEndereco = new StringBuilder();

			while ((endereco = br.readLine()) != null) {
				jEndereco.append(endereco);
			}
			// System.out.println(jEndereco);

			// Add gson 2.8.5 maven no pom.xml
			// objeto pessoaAux para capturar dados do jEndereco de mesmo nome dos
			// atributos.
			Pessoa pessoaAux = new Gson().fromJson(jEndereco.toString(), Pessoa.class);

			pessoa.setCep(pessoaAux.getCep());
			pessoa.setLogradouro(pessoaAux.getLogradouro());
			pessoa.setComplemento(pessoaAux.getComplemento());
			pessoa.setBairro(pessoaAux.getBairro());
			pessoa.setLocalidade(pessoaAux.getLocalidade());
			pessoa.setUf(pessoaAux.getUf());

			System.out.println(pessoa.getCep());
			System.out.println(pessoa.getLogradouro());
			System.out.println(pessoa.getComplemento());
			System.out.println(pessoa.getBairro());
			System.out.println(pessoa.getLocalidade());
			System.out.println(pessoa.getUf());

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem: ", "Erro ao consultar o CEP."));
		}
	}

}
