package mobi.stos.projetoestacio.util;

public enum LimiteResultadoEnum {

    DEZ(10, "10 registros"),
    VINTE(20, "20 registros"),
    TINTA(30, "30 registros"),
    QUARENTA(40, "40 registros"),
    CINQUENTA(50, "50 registros"),
    CEM(100, "100 registros");
    private final int quantidade;
    private final String descricao;

    private LimiteResultadoEnum(int quantidade, String descricao) {
        this.quantidade = quantidade;
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getDescricao() {
        return descricao;
    }
}
