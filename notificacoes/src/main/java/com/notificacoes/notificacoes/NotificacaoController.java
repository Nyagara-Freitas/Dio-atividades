package com.notificacoes.notificacoes;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoFactory factory;

    @PostMapping
    public String enviar(@RequestBody Notificacao notificacao) {
        var estrategia = factory.obterEstrategia(notificacao.getCanal());
        estrategia.enviar(notificacao);
        return "Notificação enviada via " + notificacao.getCanal();
    }
}