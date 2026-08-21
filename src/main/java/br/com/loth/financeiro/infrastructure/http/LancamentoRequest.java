package br.com.loth.financeiro.infrastructure.http;

import br.com.loth.financeiro.domain.TipoLancamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoRequest(
        @NotBlank String descricao,
        @NotNull @Positive BigDecimal valor,
        @NotNull TipoLancamento tipo,
        String categoria,
        LocalDate data
) {}
