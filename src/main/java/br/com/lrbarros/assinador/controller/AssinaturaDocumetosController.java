package br.com.lrbarros.assinador.controller;

import br.com.lrbarros.assinador.model.Documento;
import br.com.lrbarros.assinador.service.AssinadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documetos/asinaturas")
public class AssinaturaDocumetosController {

    private final AssinadorService signerService;

    public AssinaturaDocumetosController(AssinadorService signerService) {
        this.signerService = signerService;
    }

    @PostMapping
    public ResponseEntity<Documento> assinar(@RequestBody Documento doc) {
        Documento assinatura = signerService.assinar(doc);
        return ResponseEntity.ok(assinatura);
    }

    @PostMapping("/verificar")
    public ResponseEntity<Boolean> verifica(@RequestBody Documento documento) {
        boolean valido = signerService.verify(documento.conteudo(), documento.assinatura());
        return ResponseEntity.ok(valido);
    }

    @GetMapping("/algoritimo")
    public ResponseEntity<String> getAlgorithm() {
        return ResponseEntity.ok(signerService.getAlgorithm());
    }
}
