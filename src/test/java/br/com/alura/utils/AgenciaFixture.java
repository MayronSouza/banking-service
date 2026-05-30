package br.com.alura.utils;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.Endereco;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;

public class AgenciaFixture {

    public static AgenciaHttp criarAgenciaHttp(SituacaoCadastral situacaoCadastral) {
        return new AgenciaHttp("Agencia Test", "Razão Agencia Test", "123", situacaoCadastral);
    }

    public static Agencia criarAgencia()  {
        Endereco endereco = new Endereco(1, "Rua 2","Quadra Test","Travessa Test", 1);
        return new Agencia(1,"Agencia Test", "Razão Agencia Test","123", endereco);
    }
}
