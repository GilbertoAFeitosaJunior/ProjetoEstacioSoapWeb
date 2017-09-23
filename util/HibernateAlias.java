package mobi.stos.projetoestacio.util;

import org.hibernate.sql.JoinType;

public class HibernateAlias {

    private String tableJoin;
    private String alias;
    private JoinType joinType;

    public HibernateAlias() {
    }

    public HibernateAlias(String tableJoin, String alias, JoinType joinType) {
        this.tableJoin = tableJoin;
        this.alias = alias;
        this.joinType = joinType;
    }

    public String getTableJoin() {
        return tableJoin;
    }

    public void setTableJoin(String tableJoin) {
        this.tableJoin = tableJoin;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

}
