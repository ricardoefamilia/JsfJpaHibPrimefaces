package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Funcionario{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	private Long id;
	private String nome;
	private String cargo;
	private String sexo;
	@Temporal(TemporalType.DATE)
	private Date dtAdmissao;	
	private double salario;
	@OneToMany(mappedBy = "funcionario", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE,
			orphanRemoval = true)
	private List<Telefone> telefones = new ArrayList<Telefone>();
	
	public List<Telefone> getTelefones() {
		return telefones;
	}
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	
	public Date getDtAdmissao() {
		return dtAdmissao;
	}
	public void setDtAdmissao(Date dtAdmissao) {
		this.dtAdmissao = dtAdmissao;
	}
	
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getSexo() {
		return sexo;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public double getSalario() {
		return salario;
	}
	public void setSalario(double salario) {
		this.salario = salario;
	}
	

}
