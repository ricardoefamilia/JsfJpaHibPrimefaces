package controllerBean;

import java.sql.SQLException;
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

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

import com.google.gson.Gson;

import dao.DaoFuncionario;
import dao.DaoGenerico;
import dao.DaoTelefone;
import model.Funcionario;
import model.Telefone;

@ManagedBean(name = "funcionarioManagedBean")
@ViewScoped
public class FuncionarioManagedBean {

	private Funcionario funcionario = new Funcionario();
	private DaoGenerico<Funcionario> daoGenerico = new DaoGenerico<Funcionario>();
	// atributo lista que receberá os dados da tabela Pessoa no méodo listarPessoa()
	List<Funcionario> listaFuncionarios = new ArrayList<Funcionario>();
	private DaoFuncionario<Funcionario> daoFuncionario = new DaoFuncionario<Funcionario>();
	// gráfico no primefaces
	private BarChartModel bcm = new BarChartModel();
	// atributo para telefone
	private Telefone telefone = new Telefone();
	private DaoTelefone<Telefone> daoTelefone = new DaoTelefone<Telefone>();

	// método para receber os registros da tabela pessoa
	// carrega automaticamente a dataTable com os registros da tabela pessoa
	@PostConstruct
	public void init() {

		listaFuncionarios = daoGenerico.listarDadosEntidade(Funcionario.class);
		graficoSalario();
	}

	public void setDaoFuncionario(DaoFuncionario<Funcionario> daoFuncionario) {
		this.daoFuncionario = daoFuncionario;
	}

	public DaoFuncionario<Funcionario> getDaoFuncionario() {
		return daoFuncionario;
	}

	public void setDaoTelefone(DaoTelefone<Telefone> daoTelefone) {
		this.daoTelefone = daoTelefone;
	}

	public DaoTelefone<Telefone> getDaoTelefone() {
		return daoTelefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	// atributo lista que receberá os dados da tabela Pessoa no méodo listarPessoa()
	public void setListaFuncionarios(List<Funcionario> listaFuncionarios) {
		this.listaFuncionarios = listaFuncionarios;
	}

	public BarChartModel getBcm() {
		return bcm;
	}

	public void setBcm(BarChartModel bcm) {
		this.bcm = bcm;
	}

	// método para carregar gráfico de salários dos funcionários
	public void graficoSalario() {
		listaFuncionarios = daoGenerico.listarDadosEntidade(Funcionario.class);

		// BarChartSeries cria um grupo de funcionários
		ChartSeries funcionarioSalario = new ChartSeries();
		funcionarioSalario.setLabel("Funcionários");
		for (Funcionario funcionario : listaFuncionarios) {
			// carregando os funcionários no gráfico do primefaces
			funcionarioSalario.set(funcionario.getNome(), funcionario.getSalario());
		}
		bcm.addSeries(funcionarioSalario); // add o grupo no BarChartModel
		bcm.setTitle("Gráfico de Salários dos Funcionários");
		bcm.setSeriesColors("66cc66");
		bcm.setLegendPosition("e");
		bcm.setMouseoverHighlight(true);
		bcm.setShowDatatip(false);
		bcm.setShowPointLabels(true);

		Axis xAxis = bcm.getAxis(AxisType.X);
		xAxis.setLabel("Funcionários");
		xAxis.setMin(0);
		xAxis.setMax(200);

		Axis yAxis = bcm.getAxis(AxisType.Y);
		yAxis.setLabel("Salários");
	}

	// método carregar a lista de registros da tabela pessoa após método acima
	// carregarPessoas()
	public List<Funcionario> getListaFuncionarios() {
		return listaFuncionarios;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public String salvar() {
		daoGenerico.inserirAtualizar(funcionario);
		limpar();
		listaFuncionarios.add(funcionario);
		graficoSalario();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso: ", "Dados gravados com sucesso!"));
		return "";
	}

	public String excluir() {
		try {
			daoGenerico.excluir(funcionario);
			listaFuncionarios.remove(funcionario);
			graficoSalario();
			limpar();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso: ", "Excluído com sucesso!"));
		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Aviso: ", "A pessoa possui " + "dependentes cadastrados"));
			} else {
				e.printStackTrace();
			}
		}

		return "";
	}

	public void limpar() {
		funcionario = new Funcionario();
	}

	public List<Funcionario> getLista() {
		listaFuncionarios = daoGenerico.listarDadosEntidade(Funcionario.class);
		return listaFuncionarios;
	}

	// método para inserir telefone no BD
	public void inserirTelefone() {
		try {
			// carrega os dados de telefone do formulário com os dados do funcionário
			telefone.setFuncionario(funcionario);
			/*
			 * System.out.println(telefone.getFuncionario().getId());
			 * System.out.println(telefone.getFuncionario().getNome());
			 * System.out.println(telefone.getNumero());
			 * System.out.println(telefone.getTipo());
			 */
			// insere/atualiza no BD o telefone e retorna os dados da tabela para o objeto
			// telefone
			telefone = daoTelefone.inserirAtualizar(telefone);
			// caregar os dados do novo telefone e add na lista
			funcionario.getTelefones().add(telefone);
			// limpa o formulário do telefone para cadastrar outro, se desejar.
			telefone = new Telefone();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso: ", "Dados cadastrados com sucesso!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso: ", "Erro na gravação dos dados."));
		}
	}

	// método para excluir telefone
	public void excluirTelefone() throws Exception {
		// pegando o id do telefone por parâmetro
		String idTelefone = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("codigoTel");
		System.out.println(idTelefone);
		Telefone telExcluir = new Telefone();
		telExcluir.setId(Long.parseLong(idTelefone));
		daoTelefone.excluir(telExcluir);
		funcionario.getTelefones().remove(telExcluir);
		FacesContext.getCurrentInstance().addMessage(null,
			new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso: ", "Dados excluídos com sucesso!"));

	}

}
