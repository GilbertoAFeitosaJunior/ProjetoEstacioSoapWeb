/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobi.stos.projetoestacio.enumm;

/**
 *
 * @author Gilberto
 */
public enum SexoEnum {
    NAO_INFORMAR("Não informado"),
    MASCULINO("Masculino"),
    FEMININO("Feminino");

    private final String name;

    private SexoEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
