# Trab_DSWEB

## Como rodar
1. docker compose up -d
2. acessar o banco com as credenciais no `compose.yml` e executar o seguinte script sql:
```sql
INSERT INTO admins (id, email, password)
VALUES
    ('29fa7012-f1cc-4e69-bb39-0bcd6746c0bf', 'admin@gmail.com.br', '$2a$10$G2pjfn.SXujLsFxZ7YgEkO39tBLMyoZ7x7UHU5Y9vdOFcLuN9y6XO');

INSERT INTO enterprises (id, cnpj, email, password, name, description, city)
VALUES
    ('cbb7ccc2-40bc-42e3-a06a-a365b6e517d1', '12.345.678/0001-11', 'techcorp@email.com', '$2a$10$OnDeI1PYjqEpIL9ffjP32ObJ8bBHAY8ywrWIPgErUCkbcCpGau1YO', 'Tech Corp', 'Empresa de tecnologia', 'São Paulo'),
    ('33333333-3333-3333-3333-333333333333', '34.567.890/0001-33', 'edusol@email.com', '$2a$10$OnDeI1PYjqEpIL9ffjP32ObJ8bBHAY8ywrWIPgErUCkbcCpGau1YO', 'Edu Solutions', 'Plataforma de ensino online', 'Curitiba');

INSERT INTO jobs (
    id, description, cnpj, job_type, application_deadline, job_active,
    remuneration, city, enterprise_id
)
VALUES
    ('944abac0-2d82-4515-9880-55bbc2c0de5c', 'Desenvolvedor Backend Java', '12.345.678/0001-11', 'FULL_TIME', '2025-07-30T23:59:59', true, 6000.00, 'São Paulo', 'cbb7ccc2-40bc-42e3-a06a-a365b6e517d1'),
    ('8db9fba3-5b39-4639-9e3d-b6492f465420', 'Analista de Dados', '23.456.789/0001-22', 'INTERNSHIP', '2025-08-15T23:59:59', true, 4500.00, 'Belo Horizonte', '4f8d2416-2a10-4518-b925-85563fddff2d');

INSERT INTO job_skills (job_id, skill)
VALUES
    ('944abac0-2d82-4515-9880-55bbc2c0de5c', 'Java'),
    ('944abac0-2d82-4515-9880-55bbc2c0de5c', 'Spring Boot'),
    ('944abac0-2d82-4515-9880-55bbc2c0de5c', 'PostgreSQL'),

    ('8db9fba3-5b39-4639-9e3d-b6492f465420', 'Python'),
    ('8db9fba3-5b39-4639-9e3d-b6492f465420', 'SQL'),
    ('8db9fba3-5b39-4639-9e3d-b6492f465420', 'Power BI');
```
3. para as enterprises já cadastradas, a senha é 123456
4. criar um arquivo env.properties dentro da pasta resources, e adicionar as seguintes credenciais após criar uma senha de app no gmail:
```
    EMAIL_USERNAME=seu_email
    EMAIL_PASSWORD=sua_senha_de_app_gerada
```
5. pronto, agora só executar a aplicação e começar a testar