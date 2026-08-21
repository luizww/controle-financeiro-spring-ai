package br.com.loth.financeiro.infrastructure.http;

import jakarta.validation.constraints.NotBlank;

public record ComandoRequest(@NotBlank String mensagem) {}
