package br.com.pedrorodrigues.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.pedrorodrigues.todolist.user.iUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    iUserRepository iUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servLetPath = request.getServletPath();

        if (servLetPath.startsWith("/tasks/")) {

            // pegar autenticação(usuário, senha)
            var authorization = request.getHeader("Authorization");
            // validar usuário
            var authEncoded = authorization.substring("Basic".length()).trim();

            byte[] authDecote = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecote);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            var user = this.iUserRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401, "Usuário inválido");
            } else {
                // validar senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter((request), response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter((request), response);

        }

    }
    // continaur

}
