package mobi.stos.projetoestacio.bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import mobi.stos.projetoestacio.enumm.SexoEnum;
import mobi.stos.projetoestacio.util.Util;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author feitosa
 */
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_cpf", columnNames = {"cpf"}))
@Entity
@DynamicInsert
@DynamicUpdate
public class Aluno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
   
    @Column(length = 100)
    private String foto;
   
    @Column(length = 100)
    private String nomeMae;
    
    private int cep;
   
    @Column(length = 100)
    private String logradouro;
    
    @Column(length = 100)
    private String complemento;
    
    @Column(length = 50)
    private String bairro;
   
    @Column(length = 50)   
    private String cidade;
    
    @Column(length = 2)
    private String uf;
    
    @Column(length = 100, nullable = false)
    private String email;   
    
    private int celular;
    private int ddd;
   
    private SexoEnum sexoEnum;
    
    @Temporal(TemporalType.DATE)
    private Date dataDeNascimento;
   
    @Column(nullable = false)
    private Long cpf;
    
    @Column(length = 10)
    private String orgaoEmissor;
    
    private Long rg;
    
    
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
   
    public int getCelular() {
        return celular;
    }

    public void setCelular(int celular) {
        this.celular = celular;
    }

    public int getDdd() {
        return ddd;
    }

    public void setDdd(int ddd) {
        this.ddd = ddd;
    }

    public SexoEnum getSexoEnum() {
        return sexoEnum;
    }

    public void setSexoEnum(SexoEnum sexoEnum) {
        this.sexoEnum = sexoEnum;
    }

    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public Long getCpf() {
        return cpf;
    }

    public String getCpfMask() {
        return Util.format("###.###.###-##", Util.zeroFill(cpf, 11));
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public void setCpfMask(String cpf) {
        this.cpf = Long.parseLong(Util.onlyNumber(cpf));
    }

    public String getOrgaoEmissor() {
        return orgaoEmissor;
    }

    public void setOrgaoEmissor(String orgaoEmissor) {
        this.orgaoEmissor = orgaoEmissor;
    }

    public Long getRg() {
        return rg;
    }

    public void setRg(Long rg) {
        this.rg = rg;
    }

    public int getCep() {
        return cep;
    }

    public String getCepMask() {
        return Util.format("#####-###", Util.zeroFill(8, cep));
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

}
