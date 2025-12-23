-- Script de inicialização do banco de dados PostgreSQL
-- Este script será executado automaticamente quando o container for criado

-- O banco hospitech_db já é criado pela variável POSTGRES_DB
-- Aqui podemos adicionar configurações adicionais se necessário

-- Criar extensões úteis
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Log de inicialização
SELECT 'Banco de dados hospitech_db inicializado com sucesso!' as status;