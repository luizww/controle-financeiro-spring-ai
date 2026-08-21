package br.com.loth.financeiro.application;

import br.com.loth.financeiro.domain.Lancamento;
import br.com.loth.financeiro.domain.TipoLancamento;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Component
public class FinanceTools {
    private final LancamentoService service;

    public FinanceTools(LancamentoService service) {
        this.service = service;
    }

    @Tool(description = "Registra uma receita ou despesa financeira do usuário.")
    public Lancamento registrarLancamento(
            @ToolParam(description = "Descrição curta do lançamento") String descricao,
            @ToolParam(description = "Valor positivo do lançamento") BigDecimal valor,
            @ToolParam(description = "Use RECEITA ou DESPESA") TipoLancamento tipo,
            @ToolParam(description = "Categoria, como alimentação, transporte ou trabalho") String categoria,
            @ToolParam(description = "Data no formato yyyy-MM-dd") String data) {
        return service.criar(descricao, valor, tipo, categoria,
                data == null || data.isBlank() ? LocalDate.now() : LocalDate.parse(data));
    }

    @Tool(description = "Consulta o resumo financeiro de um mês, incluindo saldo e gastos por categoria.")
    public Object consultarResumo(
            @ToolParam(description = "Mês no formato yyyy-MM. Se não informado, use o mês atual") String mes) {
        YearMonth periodo = mes == null || mes.isBlank() ? YearMonth.now() : YearMonth.parse(mes);
        return service.resumo(periodo);
    }
}
