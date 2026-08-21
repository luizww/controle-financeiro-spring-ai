package br.com.loth.financeiro.infrastructure;

import br.com.loth.financeiro.domain.Lancamento;
import br.com.loth.financeiro.domain.LancamentoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryLancamentoRepository implements LancamentoRepository {
    private final List<Lancamento> lancamentos = new ArrayList<>();
    private final AtomicLong ids = new AtomicLong(1);

    @Override
    public synchronized Lancamento salvar(Lancamento lancamento) {
        Lancamento salvo = new Lancamento(ids.getAndIncrement(), lancamento.descricao(),
                lancamento.valor(), lancamento.tipo(), lancamento.categoria(), lancamento.data());
        lancamentos.add(salvo);
        return salvo;
    }

    @Override
    public synchronized List<Lancamento> listar() {
        return List.copyOf(lancamentos);
    }
}
