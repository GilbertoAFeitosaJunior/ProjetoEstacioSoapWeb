package mobi.stos.projetoestacio.bean;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
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
import javax.xml.bind.annotation.XmlTransient;
import mobi.stos.projetoestacio.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author Weibson
 */
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uk_login", columnNames = {"login"})
})
@Entity
@DynamicInsert
@DynamicUpdate
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(nullable = false, length = 100)
    private String login;
    @Column(nullable = false, length = 32)
    private String senha;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable = false)
    private Date ultimoAcesso;
    @Column(length = 32)
    private String hash;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) throws NoSuchAlgorithmException {
        if (StringUtils.isNotEmpty(senha) && senha.length() < 32) {
            this.senha = Util.md5(senha);
        } else {
            this.senha = senha;
        }
    }

    public Date getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(Date ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @XmlTransient
    @JSON(serialize = false)
    public String getUltimoAcessoFormat() {
        if (ultimoAcesso == null) {
            return "Nunca acessou";
        } else {
            try {
                int dias = Util.diferenceDates(ultimoAcesso, new Date());
                if (dias == 0) {
                    Date now = new Date();
                    long seconds = (now.getTime() - ultimoAcesso.getTime()) / 1000;
                    if (seconds < 30) {
                        return "Agora";
                    }
                    if (seconds < 60) {
                        return seconds + "s";
                    } else {
                        int minutos = (int) seconds / 60;
                        if (minutos < 60) {
                            return minutos + "min atrás";
                        } else {
                            int horas = (int) minutos / 60;
                            return horas + "h atrás";
                        }
                    }
                } else {
                    return +dias + " dia" + (dias > 1 ? "s" : "") + " atrás";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }

}
