-- 1. Tabela: CIDADE
INSERT INTO cidade (cid_nome, cid_uf) VALUES ('São Paulo', 'SP');
INSERT INTO cidade (cid_nome, cid_uf) VALUES ('Rio de Janeiro', 'RJ');

-- 2. Tabela: ENDERECO
INSERT INTO endereco (end_tipo_logradouro, end_logradouro, end_numero, end_bairro, cid_id)
VALUES ('Avenida', 'Paulista', 1000, 'Bela Vista', 1);
INSERT INTO endereco (end_tipo_logradouro, end_logradouro, end_numero, end_bairro, cid_id)
VALUES ('Rua', 'das Laranjeiras', 200, 'Centro', 2);

-- 3. Tabela: PESSOA
INSERT INTO pessoa (pes_nome, pes_data_nascimento, pes_sexo, pes_mae, pes_pai)
VALUES ('João da Silva', '1990-05-20', 'Masculino', 'Maria da Silva', 'José da Silva');
INSERT INTO pessoa (pes_nome, pes_data_nascimento, pes_sexo, pes_mae, pes_pai)
VALUES ('Ana Santos', '1985-07-15', 'Feminino', 'Luísa Santos', 'Carlos Santos');

-- 4. Tabela: SERVIDOR_EFETIVO
-- Insere apenas para a Pessoa 1 (assumindo que o ID é 1)
INSERT INTO servidor_efetivo (pes_id, se_matricula)
VALUES (1, 'MATR001');

-- 5. Tabela: SERVIDOR_TEMPORARIO
-- Insere para a Pessoa 2 (assumindo que o ID é 2)
INSERT INTO servidor_temporario (pes_id, st_data_admissao, st_data_demissao)
VALUES (2, '2023-01-10', NULL);

-- 6. Tabela: UNIDADE
INSERT INTO unidade (unid_nome, unid_sigla)
VALUES ('Unidade Central', 'UC');
INSERT INTO unidade (unid_nome, unid_sigla)
VALUES ('Unidade Secundária', 'US');

-- 7. Tabela: LOTACAO
-- Relaciona a Pessoa 1 (servidor efetivo) com Unidade 1
INSERT INTO lotacao (pes_id, unid_id, lot_data_lotacao, lot_data_remocao, lot_portaria)
VALUES (1, 1, '2022-05-01', NULL, 'Portaria 001');
-- Relaciona a Pessoa 2 (servidor temporário) com Unidade 2
INSERT INTO lotacao (pes_id, unid_id, lot_data_lotacao, lot_data_remocao, lot_portaria)
VALUES (2, 2, '2023-02-15', NULL, 'Portaria 002');

-- 8. Tabela: FOTO_PESSOA
-- Valores simulados para os campos fp_bucket e fp_hash
INSERT INTO foto_pessoa (pes_id, fp_data, fp_bucket, fp_hash)
VALUES (1, '2023-03-10', 'unique_file_1.jpg', 'md5hash1');
INSERT INTO foto_pessoa (pes_id, fp_data, fp_bucket, fp_hash)
VALUES (2, '2023-03-12', 'unique_file_2.jpg', 'md5hash2');

-- 9. Tabela: ROLE
INSERT INTO role (role_nome) VALUES ('ROLE_ADMIN');
INSERT INTO role (role_nome) VALUES ('ROLE_USER');

-- 10. Tabela: USUARIO
-- Exemplo: as senhas devem estar criptografadas, valores abaixo são simulados
INSERT INTO usuario (usu_username, usu_senha)
VALUES ('admin', '$2a$10$encryptedpassword1');
INSERT INTO usuario (usu_username, usu_senha)
VALUES ('user', '$2a$10$encryptedpassword2');

-- 11. Tabela associativa: PESSOA_ENDERECO
-- Associação entre Pessoa e Endereco
INSERT INTO pessoa_endereco (pes_id, end_id) VALUES (1, 1);
INSERT INTO pessoa_endereco (pes_id, end_id) VALUES (2, 2);

-- 12. Tabela associativa: UNIDADE_ENDERECO
-- Associação entre Unidade e Endereco
INSERT INTO unidade_endereco (uni_id, end_id) VALUES (1, 1);
INSERT INTO unidade_endereco (uni_id, end_id) VALUES (2, 2);

-- 13. Tabela associativa: USUARIO_ROLE
-- Associação entre Usuario e Role
INSERT INTO usuario_role (usu_id, role_id) VALUES (1, 1);
INSERT INTO usuario_role (usu_id, role_id) VALUES (2, 2);
