package com.notificacoes.notificacoes;

import lombok.Data;

@Data
public class Notificacao {
    private CanalNotificacao canal;
    private String destinatario;
    private String mensagem;
}