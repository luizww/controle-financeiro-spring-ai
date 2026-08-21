package br.com.loth.financeiro.infrastructure.http;

import br.com.loth.financeiro.application.LancamentoService;
import br.com.loth.financeiro.domain.Lancamento;
import br.com.loth.financeiro.infrastructure.AiFinanceService;
import jakarta.validation.Valid;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class FinanceController {
    private final LancamentoService lancamentoService;
    private final AiFinanceService aiFinanceService;
    private final TranscriptionModel transcriptionModel;
    private final TextToSpeechModel textToSpeechModel;

    public FinanceController(LancamentoService lancamentoService, AiFinanceService aiFinanceService,
                             TranscriptionModel transcriptionModel, TextToSpeechModel textToSpeechModel) {
        this.lancamentoService = lancamentoService;
        this.aiFinanceService = aiFinanceService;
        this.transcriptionModel = transcriptionModel;
        this.textToSpeechModel = textToSpeechModel;
    }

    @PostMapping("/lancamentos")
    @ResponseStatus(HttpStatus.CREATED)
    public Lancamento criar(@Valid @RequestBody LancamentoRequest request) {
        return lancamentoService.criar(request.descricao(), request.valor(), request.tipo(),
                request.categoria(), request.data());
    }

    @GetMapping("/lancamentos")
    public List<Lancamento> listar() {
        return lancamentoService.listar();
    }

    @PostMapping("/ia/comandos")
    public String executarComando(@Valid @RequestBody ComandoRequest request) {
        return aiFinanceService.conversar(request.mensagem());
    }

    @PostMapping(value = "/ia/audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = "audio/mpeg")
    public ResponseEntity<Resource> executarAudio(@RequestParam("file") MultipartFile file) {
        String mensagem = transcriptionModel.transcribe(file.getResource());
        String resposta = aiFinanceService.conversar(mensagem);
        byte[] audio = textToSpeechModel.call(resposta);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .header("Content-Disposition", "attachment; filename=financeiro.mp3")
                .body(new ByteArrayResource(audio));
    }
}
