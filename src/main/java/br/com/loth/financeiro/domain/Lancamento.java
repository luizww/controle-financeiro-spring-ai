package br.com.loth.financeiro.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Lancamento(
        Long id,
        String descricao,
        BigDecimal valor,
        TipoLancamento tipo,
        String categoria,
        LocalDate data
) {}
