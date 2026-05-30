package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.exception.AgenciaNaoAtivaOuNaoEncontrada;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class AgenciaService {

    @Inject
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    private final AgenciaRepository agenciaRepository;
    private final MeterRegistry meterRegistry;

    AgenciaService(AgenciaRepository agenciaRepository, MeterRegistry meterRegistry) {
        this.agenciaRepository = agenciaRepository;
        this.meterRegistry = meterRegistry;
    }

    public void cadastrar(Agencia agencia) {
        AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());

        if(agenciaHttp != null && agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)) {
            Log.info("A agência com CNPJ " + agencia.getCnpj() + " foi cadastrada.");
            meterRegistry.counter("agencia_adicionada_counter").increment();
            agenciaRepository.persist(agencia);
        } else {
            Log.info("A agência com CNPJ " + agencia.getCnpj() + " não foi cadastrada.");
            meterRegistry.counter("agencia_nao_adicionada_counter").increment();
            throw new AgenciaNaoAtivaOuNaoEncontrada();
        }
    }

    public List<Agencia> listar() {
        return agenciaRepository.listAll();
    }

    public Agencia buscarPorId(Long id) {
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id) {
        Log.info("A agência com ID " + id + " foi deletada.");
        agenciaRepository.deleteById(id);
    }

    public void alterar(Agencia agencia) {
        Log.info("A agência com CNPJ " + agencia.getCnpj() + " foi alterada.");
        agenciaRepository.update("nome = ?1, razaoSocial = ?2, cnpj = ?3 where id = ?4",
                agencia.getNome(),
                agencia.getRazaoSocial(),
                agencia.getCnpj(),
                agencia.getId()
        );
    }
}
