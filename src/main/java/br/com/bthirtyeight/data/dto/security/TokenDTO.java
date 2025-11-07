package br.com.bthirtyeight.data.dto.security;

import java.util.Date;

//ese DTO e oq sera retornado ao usuario pela api apos a tentativa da authenticacao
public class TokenDTO {

    private String username;
    private String password;
    private Boolean authenticated;
    private Date created;//data de criação
    private Date expiration;//data de expiracao
    private String acessToken;//token de acesso
    private String refreshToken;//usado quando o token de acesso expirar
}
