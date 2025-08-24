package com.notificacoes.notificacoes;

import org.springframework.stereotype.Component;

@Component
public class EmailNotificacao implements NotificacaoStrategy {
    @Override
    public void enviar(Notificacao n) {
        System.out.println("📧 Email para " + n.getDestinatario() + ": " + n.getMensagem());
    }
}