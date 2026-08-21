# Controle Financeiro com Spring AI

Projeto final da trilha de Spring Boot com uma evolução própria do exemplo de orçamento da DIO.

A aplicação registra receitas e despesas e oferece um endpoint de IA capaz de entender comandos em linguagem natural. A IA recebe ferramentas reais da aplicação para registrar lançamentos e consultar o resumo do mês.

## Exemplo

```http
POST /ia/comandos
Content-Type: application/json

{
  "mensagem": "Gastei 42 reais com almoço hoje"
}
```

Nesse caso, o modelo pode chamar a ferramenta `registrarLancamento` e criar uma despesa de alimentação. Também é possível pedir:

```json
{
  "mensagem": "Como está meu saldo este mês?"
}
```

A resposta é gerada pela IA depois que a ferramenta `consultarResumo` consulta os dados reais da aplicação.

## O que foi implementado

- `ChatClient` configurado com Spring AI;
- Tool Calling com as ferramentas de lançamento e resumo mensal;
- transcrição de áudio com `TranscriptionModel`;
- geração de resposta em MP3 com `TextToSpeechModel`;
- camada de domínio com contrato de repositório;
- caso de uso para registrar e resumir transações;
- endpoint REST para uso direto sem IA;
- validação de valores e descrições;
- persistência em memória para facilitar os testes;
- README com exemplos de requisição.

Além do fluxo de texto, esta versão expõe `/ia/audio`: o arquivo é transcrito, a mensagem passa pelo mesmo Tool Calling e a resposta volta em MP3. A melhoria escolhida foi adicionar a consulta de resumo mensal como uma nova ferramenta da IA.

## Como executar

É necessário Java 17 e uma chave da OpenAI.

Linux/macOS:

```bash
export OPENAI_API_KEY="sua-chave-aqui"
gradle bootRun
```

Windows PowerShell:

```powershell
$env:OPENAI_API_KEY="sua-chave-aqui"
gradle bootRun
```

Sem uma chave, os endpoints de cadastro e listagem continuam disponíveis, mas o endpoint `/ia/comandos` não consegue chamar o modelo.

## Endpoints

| Método | Rota | Função |
|---|---|---|
| POST | `/lancamentos` | Registra uma receita ou despesa |
| GET | `/lancamentos` | Lista os lançamentos |
| POST | `/ia/comandos` | Envia uma frase para a IA |
| POST | `/ia/audio` | Recebe áudio e devolve a resposta em MP3 |

## Tecnologias

- Java 17
- Spring Boot
- Spring AI
- OpenAI Chat Model
- Gradle

## O que aprendi

O principal aprendizado foi perceber que a IA não deve salvar dados diretamente. Ela interpreta a mensagem e chama uma ferramenta da aplicação. A regra de negócio continua no serviço, o domínio não depende do provedor de IA e o fluxo fica mais fácil de testar e evoluir.
