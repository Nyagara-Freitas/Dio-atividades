package com.notificacoes.notificacoes;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificacaoFactory {
    private final Map<CanalNotificacao, NotificacaoStrategy> estrategias;

    public NotificacaoFactory(EmailNotificacao email, SmsNotificacao sms, WhatsappNotificacao whatsapp) {
        this.estrategias = Map.of(
                CanalNotificacao.EMAIL, email,
                CanalNotificacao.SMS, sms,
                CanalNotificacao.WHATSAPP, whatsapp
        );
    }

    public NotificacaoStrategy obterEstrategia(CanalNotificacao canal) {
        return estrategias.get(canal);
    }
}
