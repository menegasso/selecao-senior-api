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

    http://localhost:8080/swagger-ui/index.html

Certifique-se de que a aplicação esteja rodando na porta 8080.

## Autenticação

A aplicação já cria automaticamente um usuário de teste:

- **Usuário:** `admin`
- **Senha:** `admin123`

Utilize essas credenciais no endpoint de autenticação para gerar o token JWT e acessar os demais endpoints protegidos.

---

Desenvolvido para o processo seletivo Senior.