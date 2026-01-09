package servicehistory_api.config;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherResult;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GraphQLAuthInterceptor implements WebGraphQlInterceptor {
    
    private final JwtUtil jwtUtil;
    
    public GraphQLAuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return createErrorResponse("Acesso negado: Token de autenticação obrigatório");
        }
        
        String token = authHeader.substring(7);
        
        if (!jwtUtil.isTokenValid(token)) {
            return createErrorResponse("Acesso negado: Token inválido");
        }
        
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);
        
        // Adicionar informações do usuário ao contexto
        request.configureExecutionInput((executionInput, builder) -> 
            builder.graphQLContext(context -> context
                .put("username", username)
                .put("role", role)
            ).build()
        );
        
        return chain.next(request);
    }
    
    private Mono<WebGraphQlResponse> createErrorResponse(String message) {
        return Mono.error(new org.springframework.security.access.AccessDeniedException(message));
    }
}