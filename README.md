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

Para autenticar e obter o token JWT usando o Postman:

1. Abra o Postman.
2. Crie uma nova requisição `POST` para:

```
http://localhost:8080/auth/login
```

3. No corpo da requisição, selecione o tipo `raw` e `JSON`, e insira o seguinte conteúdo:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

4. Envie a requisição. O token JWT será retornado na resposta e poderá ser usado nos demais endpoints protegidos.

---

Desenvolvido para o processo seletivo Senior.