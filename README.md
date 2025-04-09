# selecao-senior-api

API REST para gerenciamento de servidores efetivos, servidores temporários, unidades e lotações, com autenticação JWT, upload de imagens via MinIO e persistência em PostgreSQL.

## Dados do Candidato

- **Nome Completo:** Andre Eduardo Menegasso
- **CPF:** 06533563904
- **Identidade:** 100725150
- **Data de Nascimento:** 06/09/1989
- **Endereço:** Rua Santina Felipe de Faveri, 416, Três Lagoas, Foz do Iguaçu
- **Contato:** meneghasso@gmail.com | (45) 99926-3232

## Executando os Containers com Docker Compose

Para iniciar a aplicação e os serviços necessários (API, PostgreSQL e MinIO), certifique-se de ter o Docker e o Docker Compose instalados. Em seguida, execute o comando:

```bash
docker-compose up -d
```

## Acesso ao Swagger

Após iniciar a aplicação, acesse a documentação interativa gerada pelo Swagger pelo seguinte endereço:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Certifique-se de que a aplicação esteja rodando na porta 8080.

### Usando o Swagger

No Swagger, para realizar a autenticação e gerar o token JWT, localize a seção **auth-controller** na lista de endpoints. A partir dessa aba, você poderá acessar o endpoint de login e testar diretamente pela interface.

## Autenticação Manual (ex: Postman)

A aplicação já cria automaticamente um usuário de teste:

- **Usuário:** `admin`
- **Senha:** `admin123`

### Obtendo Token com Postman

```http
POST http://localhost:8080/auth/login
```

Body (raw / JSON):
```json
{
  "username": "admin",
  "password": "admin123"
}
```

---

## Visão Geral dos Endpoints

### 📂 Auth Controller
- `POST /auth/login` – Autenticação de usuários
- `POST /auth/refresh` – Renovação de token JWT

### 👤 Usuario Controller
Gerenciamento de usuários do sistema:
- CRUD completo em `/api/usuarios`

### 🏢 Unidade Controller
Gerenciamento de unidades organizacionais:
- CRUD completo em `/api/unidades`

### 📄 Servidor Temporário Controller
Cadastro e controle de servidores temporários:
- CRUD completo em `/api/servidores-temporarios`

### 🗃️ Servidor Efetivo Controller
Cadastro e controle de servidores efetivos:
- CRUD completo em `/api/servidores-efetivos`

### 🛡️ Role Controller
Gerenciamento de perfis/funções de acesso:
- CRUD parcial em `/api/roles` (sem DELETE)

### 🧍 Pessoa Controller
Gerenciamento de informações pessoais:
- CRUD completo em `/api/pessoas`

### 🧭 Lotação Controller
Controle de lotações funcionais:
- CRUD completo em `/api/lotacoes`
- `GET /api/lotacoes/servidores-efetivos` – Consulta especial
- `GET /api/lotacoes/endereco-funcional` – Consulta especial

### 🖼️ Foto Pessoa Controller
Upload e gestão de imagens (via MinIO):
- `GET/PUT/DELETE /api/fotos-pessoa/{id}`
- `GET /api/fotos-pessoa`
- `POST /api/fotos-pessoa` – Cadastro de metadados de imagem
- `POST /api/fotos-pessoa/upload` – Upload de uma imagem
- `POST /api/fotos-pessoa/uploadMany` – Upload múltiplo
- `GET /api/fotos-pessoa/{id}/url` – Obter URL pública da imagem

### 📍 Endereço Controller
CRUD de endereços físicos:
- CRUD completo em `/api/enderecos`

### 🏙️ Cidade Controller
Cadastro de cidades:
- CRUD completo em `/api/cidades`

---

Desenvolvido para o processo seletivo Senior.