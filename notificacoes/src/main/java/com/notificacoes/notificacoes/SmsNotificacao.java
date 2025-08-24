package com.notificacoes.notificacoes;

import org.springframework.stereotype.Component;

@Component
public class SmsNotificacao implements NotificacaoStrategy {
    @Override
    public void enviar(Notificacao n) {
        System.out.println("📱 SMS para " + n.getDestinatario() + ": " + n.getMensagem());
    }
}
