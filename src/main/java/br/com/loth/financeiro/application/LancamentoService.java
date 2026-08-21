package br.com.loth.financeiro.application;

import br.com.loth.financeiro.domain.Lancamento;
import br.com.loth.financeiro.domain.LancamentoRepository;
import br.com.loth.financeiro.domain.TipoLancamento;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LancamentoService {
    private final LancamentoRepository repository;

    public LancamentoService(LancamentoRepository repository) {
        this.repository = repository;
    }

    public Lancamento criar(String descricao, BigDecimal valor, TipoLancamento tipo, String categoria,
                            LocalDate data) {
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor precisa ser maior que zero.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição é obrigatória.");
        }
        return repository.salvar(new Lancamento(null, descricao.trim(), valor, tipo,
                categoria == null || categoria.isBlank() ? "Outros" : categoria.trim(),
                data == null ? LocalDate.now() : data));
    }

    public List<Lancamento> listar() {
        return repository.listar();
    }

    public Map<String, Object> resumo(YearMonth mes) {
        List<Lancamento> doMes = repository.listar().stream()
                .filter(l -> YearMonth.from(l.data()).equals(mes))
                .toList();

        BigDecimal receitas = somar(doMes, TipoLancamento.RECEITA);
        BigDecimal despesas = somar(doMes, TipoLancamento.DESPESA);
        Map<String, BigDecimal> porCategoria = new LinkedHashMap<>();

        doMes.stream().filter(l -> l.tipo() == TipoLancamento.DESPESA)
                .forEach(l -> porCategoria.merge(l.categoria(), l.valor(), BigDecimal::add));

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("mes", mes.toString());
        resultado.put("receitas", receitas);
        resultado.put("despesas", despesas);
        resultado.put("saldo", receitas.subtract(despesas));
        resultado.put("despesasPorCategoria", porCategoria);
        return resultado;
    }

    private BigDecimal somar(List<Lancamento> lancamentos, TipoLancamento tipo) {
        return lancamentos.stream().filter(l -> l.tipo() == tipo)
                .map(Lancamento::valor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
