package mobi.stos.projetoestacio.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

public class Consulta implements Serializable {

    //NENHUM
    public static final int NENHUM = 0;
    //STRING
    public static final int OPERADOR_LIKE_ALL = 1;
    public static final int OPERADOR_LIKE_BEGIN = 2;
    public static final int OPERADOR_LIKE_END = 3;
    public static final int OPERADOR_STR_EQUALS = 4;
    //NUMERICO
    public static final int OPERADOR_GREATER = 5;
    public static final int OPERADOR_GREATER_OR_EQUALS = 6;
    public static final int OPERADOR_LESS = 7;
    public static final int OPERADOR_LESS_OR_EQUALS = 8;
    public static final int OPERADOR_NUM_EQUALS = 9;
    //DATA
    public static final int OPERADOR_DATE = 10;
    // CONVERSÕES
    public static final int CONVERT_STRING = 0;
    public static final int CONVERT_INTEGER = 1;
    public static final int CONVERT_BIGDECIMAL = 2;
    public static final int CONVERT_DOUBLE = 3;
    public static final int CONVERT_FLOAT = 4;
    public static final int CONVERT_LONG = 5;
    public static final int CONVERT_DATE = 6;
    public static final int CONVERT_BIGINT = 7;
    //CAMPOS
    private String campo;
    private int operador = 1;
    private String valor = "";
    private int limiteResultados = 10;
    private int paginaAtual = 1;
    private String dataInicial = "";
    private String dataFinal = "";
    private int qtdResultados = 0;
    private List<Criterion> criterions;
    private final List<HibernateAlias> alias;
    private List<Order> orders;
    private String order = "";
    private String ordem = "asc";
    private int conversao = 1;

    public Consulta() {
        this.alias = new ArrayList<>();
        if (this.criterions == null) {
            this.criterions = new ArrayList<>();
            this.orders = new ArrayList<>();
        }
    }

    public Consulta(String campo) {
        this.alias = new ArrayList<>();
        if (this.criterions == null) {
            this.criterions = new ArrayList<>();
            this.orders = new ArrayList<>();
        }
        this.campo = campo;
    }

    /**
     * createCriterion<br /> Função monta o objeto Criterion a partir de duas
     * datas com o comando SQL BETWEEN<br /> Saída: campo BETWEEN dataInicial
     * AND dataFinal
     *
     * @param campo String
     * @param dataInicial String dd/MM/yyyy
     * @param dataFinal String dd/MM/yyyy
     * @return Criterion
     * @throws ParseException
     */
    public Criterion createCriterion(String campo, String dataInicial, String dataFinal) throws ParseException {
        try {

            Criterion cri;
            cri = Restrictions.between(campo, new SimpleDateFormat("dd/MM/yyyy").parse(dataInicial), new SimpleDateFormat("dd/MM/yyyy").parse(dataFinal));

            return cri;
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    /**
     * createCriterion<br /> Função monta o objeto Criterion a partir de
     * informações para construção do comando SQL
     *
     * @param campo String
     * @param valor String
     * @param operador int comando para saber qual operador SQL (LIKE, >, <, >=,
     * <=, =)
     * @param conversao int conversão String, int, double, float, bigdecimal,
     * data, etc. A conversão só é habilitada para operadores entre não LIKE
     * @return Criterion
     * @throws ParseException
     */
    public Criterion createCriterion(String campo, String valor, int operador, int conversao) throws ParseException {
        try {

            Criterion cri = null;

            Object object = null;
            if (operador >= 5 && operador <= 9) {
                switch (conversao) {
                    case Consulta.CONVERT_INTEGER:
                        object = Util.parseInt(valor);
                        break;
                    case Consulta.CONVERT_BIGDECIMAL:
                        object = BigDecimal.valueOf(Double.parseDouble(valor));
                        break;
                    case Consulta.CONVERT_DOUBLE:
                        object = Double.parseDouble(valor);
                        break;
                    case Consulta.CONVERT_FLOAT:
                        object = Float.parseFloat(valor);
                        break;
                    case Consulta.CONVERT_LONG:
                        object = Long.parseLong(valor);
                        break;
                    case Consulta.CONVERT_DATE:
                        object = new SimpleDateFormat("dd/MM/yyyy").parse(valor);
                        break;
                    case Consulta.CONVERT_BIGINT:
                        object = new BigInteger(valor);
                        break;
                    default:
                        object = valor + "";
                        break;
                }
            }

            switch (operador) {
                case Consulta.OPERADOR_LIKE_ALL:
                    cri = Restrictions.ilike(campo, "%" + valor + "%");
                    break;
                case Consulta.OPERADOR_LIKE_BEGIN:
                    cri = Restrictions.ilike(campo, valor + "%");
                    break;
                case Consulta.OPERADOR_LIKE_END:
                    cri = Restrictions.ilike(campo, "%" + valor);
                    break;
                case Consulta.OPERADOR_STR_EQUALS:
                    cri = Restrictions.ilike(campo, valor);
                    break;
                case Consulta.OPERADOR_GREATER:
                    cri = Restrictions.gt(campo, object);
                    break;
                case Consulta.OPERADOR_GREATER_OR_EQUALS:
                    cri = Restrictions.ge(campo, object);
                    break;
                case Consulta.OPERADOR_LESS:
                    cri = Restrictions.lt(campo, object);
                    break;
                case Consulta.OPERADOR_LESS_OR_EQUALS:
                    cri = Restrictions.le(campo, object);
                    break;
                case Consulta.OPERADOR_NUM_EQUALS:
                    cri = Restrictions.eq(campo, object);
                    break;
            }

            return cri;
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    public List<Criterion> getCondicaoConsulta() {
        try {
            if (this.campo != null && !this.campo.trim().equals("")) {
                if (this.operador == Consulta.OPERADOR_DATE) {
                    this.criterions.add(this.createCriterion(this.campo, this.dataInicial, this.dataFinal));
                } else {
                    this.criterions.add(this.createCriterion(this.campo, this.valor, this.operador, this.conversao));
                }
            }

        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
        return criterions;
    }

    public void addAliasTable(String table, String alias) {
        addAliasTable(table, alias, JoinType.INNER_JOIN);
    }

    public void addAliasTable(String table, String alias, JoinType joinType) {
        this.alias.add(new HibernateAlias(table, alias, joinType));
    }

    public List<HibernateAlias> getAliasTable() {
        return this.alias;
    }

    /**
     * Cria condição de consulta in
     *
     * @param campo String Nome do campo que deseja filtrar
     * @param i Collection Coleção com os dados que irá carregar para
     * comparação.
     */
    public void in(String campo, Collection i) {
        this.criterions.add(Restrictions.in(campo, i));
    }

    public int getQtdResultados() {
        return qtdResultados;
    }

    public void setQtdResultados(int qtdResultados) {
        this.qtdResultados = qtdResultados;
    }

    /**
     * Obtem os valores dos Criteions, caso exista condição em consulta ou
     * consulta in e qualquer outra consulta que venha ser alimentado. Em caso
     * de não está instanciado a função ela se iniciará. Caso contrário
     * retornará o valor corrente.
     *
     * @return List
     */
    public List<Criterion> getCriterions() {
        return criterions;
    }

    public void clearCriterion() {
        this.criterions = null;
        this.criterions = new ArrayList<>();
    }

    public void addCriterion(Criterion criterion) {
        this.criterions.add(criterion);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public int getLimiteResultados() {
        return limiteResultados;
    }

    public void setLimiteResultados(int limiteResultados) {
        this.limiteResultados = limiteResultados;
    }

    public int getOperador() {
        return operador;
    }

    /**
     * Seleciona qual o tipo da operação.<br />
     * Entenda-se:
     * <ol>
     * <li>Todas as partes</li>
     * <li>Apenas no início</li>
     * <li>Apenas no fim</li>
     * <li>Palavra ou frase igual a</li>
     * <li>Maior</li>
     * <li>Maior ou igual</li>
     * <li>Menor</li>
     * <li>Menor ou igual</li>
     * <li>Igual a</li>
     * <li>Entre</li>
     * </ol>
     *
     * @param operador
     */
    public void setOperador(int operador) {
        this.operador = operador;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getPaginaAtual() {
        return paginaAtual;
    }

    public void setPaginaAtual(int paginaAtual) {
        this.paginaAtual = paginaAtual;
    }

    //metodo que calcula a paginaçao de resultados.
    public int getPaginacao() {
        return (this.getPaginaAtual() - 1) * this.getLimiteResultados();
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public int getPaginas() {
        return (int) Math.ceil((double) this.getQtdResultados() / (double) this.getLimiteResultados());
    }

    public String getOrdem() {
        return ordem;
    }

    public void setOrdem(String ordem) {
        this.ordem = ordem;
    }

    public int getConversao() {
        return conversao;
    }

    /**
     * Seleciona qual o tipo da conversão.<br />
     * Entenda-se:
     * <ol>
     * <li>Integer (padrão)</li>
     * <li>BigDecimal</li>
     * <li>Double</li>
     * <li>Float</li>
     * <li>Long</li>
     * <li>Data</li>
     * </ol>
     *
     * @param conversao
     */
    public void setConversao(int conversao) {
        this.conversao = conversao;
    }

    public String getOrder() {
        if (this.order == null || this.order.trim().equals("")) {
            this.order = this.getCampo();
        }
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
