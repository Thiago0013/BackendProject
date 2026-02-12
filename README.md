🔐 Autenticação e Segurança

A API utiliza JWT (JSON Web Token) para proteger os endpoints.

    Endpoints Públicos: /auth/register e /auth/login.

    Endpoints Protegidos: Todas as outras rotas exigem autenticação.

Para acessar as rotas protegidas, é necessário enviar o token no cabeçalho da requisição:
HTTP

Authorization: Bearer <seu_token_aqui>

DOCUMENTAÇÃO DA API
1. Autenticação (/auth)
Registro de Usuário

Cria um novo usuário no sistema.

    Rota: POST /auth/register

    Acesso: Público

Corpo da Requisição (JSON):
Campo	Tipo	Obrigatório	Descrição
name	String	Sim	Nome completo do usuário.
email	String	Sim	Deve ser um e-mail válido.
password	String	Sim	Senha do usuário.
phone	String	Não	Telefone de contato.
userType	String/Enum	Sim	Tipo de usuário (ex: "client", "provider").
JSON

{
  "name": "Ian Silva",
  "email": "ian@exemplo.com",
  "password": "senha123",
  "phone": "85999999999",
  "userType": "client"
}

Resposta (201 Created):
JSON

{
  "name": "Ian Silva",
  "email": "ian@exemplo.com",
  "password": "$2a$10$EncryptedPassword...", 
  "phone": "85999999999"
}

Login

Autentica o usuário e gera o token de acesso.

    Rota: POST /auth/login

    Acesso: Público

Corpo da Requisição (JSON):
Campo	Tipo	Descrição
login	String	Pode ser o email ou username (conforme implementação).
password	String	Senha do usuário.
JSON

{
  "login": "ian@exemplo.com",
  "password": "senha123"
}

Resposta (200 OK):
JSON

{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpYW5..."
}

2. Projetos (/project)
Listar Projetos

Retorna a lista de projetos disponíveis ou vinculados.

    Rota: GET /project

    Acesso: Privado (Requer Token)

Resposta (200 OK):
JSON

[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "title": "E-commerce Spring Boot",
    "description": "Desenvolvimento de API para loja virtual.",
    "budget": 2500.00,
    "status": "OPEN",
    "deadline": "2026-03-20T18:00:00",
    "clientUserName": "Vórtex Tech",
    "clientUserEmail": "contato@vortex.com"
  }
]

Criar Projeto

Cadastra um novo projeto na plataforma.

    Rota: POST /project

    Acesso: Privado (Requer Token)

Corpo da Requisição (JSON):
Campo	Tipo	Descrição
title	String	Título do projeto.
description	String	Detalhes do escopo.
budget	BigDecimal	Orçamento previsto (ex: 1500.00).
status	String	Estado atual (ex: "OPEN", "IN_PROGRESS").
deadline	LocalDateTime	Data/Hora limite (formato ISO-8601).
JSON

{
  "title": "Sistema de Gestão",
  "description": "Sistema para gestão de estoque e vendas.",
  "budget": 5000.00,
  "status": "OPEN",
  "deadline": "2026-12-31T23:59:00"
}

Resposta (201 Created):
JSON

{
  "id": "a1b2c3d4-e5f6-7890-1234-56789abcdef0",
  "title": "Sistema de Gestão",
  "description": "Sistema para gestão de estoque e vendas.",
  "budget": 5000.00,
  "status": "OPEN",
  "deadline": "2026-12-31T23:59:00",
  "clientName": "Ian Silva"
}

Atualizar Projeto

Atualiza os dados de um projeto existente.

    Rota: PUT /project/{id}

    Acesso: Privado (Requer Token)

    Parâmetro: id (UUID na URL)

Corpo da Requisição (JSON):
JSON

{
  "title": "Sistema de Gestão V2",
  "description": "Alteração de escopo para incluir módulo financeiro.",
  "budget": 6000.00,
  "status": "IN_PROGRESS",
  "deadline": "2027-01-15T10:00:00"
}

Resposta (200 OK):
JSON

{
  "id": "a1b2c3d4-e5f6-7890-1234-56789abcdef0",
  "title": "Sistema de Gestão V2",
  "description": "Alteração de escopo para incluir módulo financeiro.",
  "budget": 6000.00,
  "status": "IN_PROGRESS",
  "deadline": "2027-01-15T10:00:00",
  "clientName": "Ian Silva"
}

Deletar Projeto

Remove um projeto do sistema.

    Rota: DELETE /project/{id}

    Acesso: Privado (Requer Token)

    Parâmetro: id (UUID na URL)

Resposta:

    Status: 204 No Content (Sem corpo de resposta).

3. Clientes (/client)
Obter Dados do Cliente

Retorna as informações detalhadas de um cliente e seu usuário vinculado.

    Rota: GET /client

    Acesso: Privado (Requer Token)

Resposta (200 OK):

Os dados do usuário estão aninhados no objeto user.
JSON

{
  "id": "99887766-5544-3322-1100-aabbccddeeff",
  "companyName": "empresa",
  "cnpjNif": "12.345.678/0001-90",
  "address": "Rua do tal, 100, Centro",
  "user": {
    "id": "11223344-5566-7788-9900-aabbccddeeff",
    "name": "Ian Silva",
    "email": "ian@example.com"
    "phone": "85988887777"
  }
}

🛠 Tecnologias e Tipos de Dados

Para referência técnica na integração:

    UUID: Identificador único universal (ex: 550e8400-e29b-41d4-a716-446655440000).

    BigDecimal: Número decimal para valores monetários, enviado como number ou float no JSON (ex: 150.50).

    LocalDateTime: Data e hora no formato ISO-8601 (ex: YYYY-MM-DDTHH:mm:ss).

    UserType: Enumeração (String) definindo o papel do usuário.
