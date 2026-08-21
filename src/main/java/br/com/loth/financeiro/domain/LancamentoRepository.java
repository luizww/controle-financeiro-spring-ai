package br.com.loth.financeiro.domain;

import java.util.List;

public interface LancamentoRepository {
    Lancamento salvar(Lancamento lancamento);
    List<Lancamento> listar();
}
