package br.com.loth.financeiro.infrastructure;

import br.com.loth.financeiro.application.FinanceTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiFinanceService {
    private final ChatClient chatClient;

    public AiFinanceService(ChatClient.Builder builder, FinanceTools financeTools) {
        this.chatClient = builder
                .defaultSystem("Você é um assistente financeiro direto e claro. "
                        + "Use as ferramentas disponíveis para registrar ou consultar dados. "
                        + "Nunca invente valores. Responda em português do Brasil.")
                .defaultTools(financeTools)
                .build();
    }

    public String conversar(String mensagem) {
        return chatClient.prompt()
                .user(mensagem)
                .call()
                .content();
    }
}
