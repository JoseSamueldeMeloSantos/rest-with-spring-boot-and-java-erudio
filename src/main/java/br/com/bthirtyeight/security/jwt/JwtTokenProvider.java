package br.com.bthirtyeight.security.jwt;

import br.com.bthirtyeight.data.dto.security.TokenDTO;
import br.com.bthirtyeight.exception.InvalidJwtAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {
    /*
    O provider lida diretamente com o token em si — sua geração e verificação.
    Ele normalmente faz:

    Gerar token JWT quando o usuário faz login.

    Validar se um token recebido é legítimo e não expirou.

    Extrair o username ou outras claims (dados) de dentro do toke
     */

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey;

    @Value("${security.jwt.token.expire-lenght:3600000}")
    private long validityInMilliseconds = 3600000;//igual a 1h

    @Autowired
    private UserDetailsService userDetailsService;//padrao do security

    Algorithm algorithm = null;//do jwt

    /**
     * PostConstruct -> que marca um método para ser executado automaticamente logo depois que o bean é criado
     *                  e suas dependências são injetadas pelo Spring,mas antes de qualquer interação com o usuario
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenDTO createAcessToken(String username, List<String> roles) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String accesToken = getAcessToken(username, roles, now, validity);
        String refreshToken = getRefreshToken(username, roles, now);

        return new TokenDTO(username, true, now, validity, accesToken, refreshToken);
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {

        Date refreshTokenValidity = new Date(now.getTime() + (validityInMilliseconds * 3));

        return JWT.create()
                .withClaim("roles", roles)//roles são os niveis de autorização do user
                .withIssuedAt(now)//ate quando vai durar
                .withExpiresAt(refreshTokenValidity)//data de expiração do token
                .withSubject(username)
                .sign(algorithm);//foma de criptografar os dados
    }

    private String getAcessToken(String username, List<String> roles, Date now, Date validity) {
        String issueUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();//pega a url do servidor(no nosso caso e localhost)

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(issueUrl)
                .sign(algorithm);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJwt = decodedToken(token);
        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJwt.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
       Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
       JWTVerifier verifier = JWT.require(alg).build();
       DecodedJWT decodedJWT = verifier.verify(token);

       return decodedJWT;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");//esse header e padrao

        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }

        return null;
    }

    public boolean validatedToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            if (decodedJWT.getExpiresAt().before(new Date())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or Invalid JWT Token");
        }
    }
}
