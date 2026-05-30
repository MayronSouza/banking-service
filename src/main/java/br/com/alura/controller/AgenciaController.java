package br.com.alura.controller;

import br.com.alura.domain.Agencia;
import br.com.alura.service.AgenciaService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/agencias")
public class AgenciaController {

    private final AgenciaService agenciaService;

    AgenciaController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    @POST
    @Transactional
    public RestResponse<Void> cadastrar(Agencia agencia, @Context UriInfo uriInfo) {
        this.agenciaService.cadastrar(agencia);
        return RestResponse.created(uriInfo.getAbsolutePath());
    }

    @GET
    public  RestResponse<List<Agencia>> listar() {
        var agencias = agenciaService.listar();
        return RestResponse.ok(agencias);
    }

    @GET
    @Path("{id}")
    public RestResponse<Agencia> buscarPorId(Long id) {
        Agencia agencia = this.agenciaService.buscarPorId(id);
        return RestResponse.ok(agencia);
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public RestResponse<Void> deletar(Long id) {
        this.agenciaService.deletar(id);
        return RestResponse.ok();
    }

    @PUT
    @Transactional
    public RestResponse<Void> alterar(Agencia agencia) {
        this.agenciaService.alterar(agencia);
        return RestResponse.ok();
    }
}
