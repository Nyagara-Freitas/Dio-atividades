package com.notificacoes.notificacoes;

import org.springframework.stereotype.Component;

@Component
public class WhatsappNotificacao implements NotificacaoStrategy {
    @Override
    public void enviar(Notificacao n) {
        System.out.println("💬 WhatsApp para " + n.getDestinatario() + ": " + n.getMensagem());
    }
}