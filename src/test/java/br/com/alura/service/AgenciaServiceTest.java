package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.Endereco;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.exception.AgenciaNaoAtivaOuNaoEncontrada;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import br.com.alura.utils.AgenciaFixture;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static br.com.alura.utils.AgenciaFixture.criarAgenciaHttp;

@QuarkusTest
public class AgenciaServiceTest {

    @InjectMock
    private AgenciaRepository agenciaRepository;

    @InjectMock
    @RestClient
    public SituacaoCadastralHttpService situacaoCadastralHttpService;

    @Inject
    public AgenciaService agenciaService;

    @Test
    public void deveNaoCadastrarQuandoClientRetornarNull() {
        Agencia agencia = AgenciaFixture.criarAgencia();

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(null);

        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontrada.class, () -> agenciaService.cadastrar(agencia));

        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }

    @Test
    public void deveNaoCadastrarQuandoClientRetornarSituacaoCadastralInativo() {
        Agencia agencia = AgenciaFixture.criarAgencia();
        AgenciaHttp agenciaHttp = AgenciaFixture.criarAgenciaHttp(SituacaoCadastral.INATIVO);

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(agenciaHttp);

        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontrada.class, () -> agenciaService.cadastrar(agencia));

        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }

    @Test
    public void deveCadastrarQuandoClientRetornarSituacaoCadastralAtiva() {
        Agencia agencia = AgenciaFixture.criarAgencia();
        AgenciaHttp agenciaHttp = AgenciaFixture.criarAgenciaHttp(SituacaoCadastral.ATIVO);

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(agenciaHttp);

        agenciaService.cadastrar(agencia);

        Mockito.verify(agenciaRepository).persist(agencia);
    }
}
