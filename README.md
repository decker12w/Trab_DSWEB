# Trab_DSWEB

## Como rodar
1. docker compose up -d
2. criar um arquivo env.properties dentro da pasta resources, e adicionar as seguintes credenciais após criar uma senha de app no gmail:
```
    EMAIL_USERNAME=seu_email
    EMAIL_PASSWORD=sua_senha_de_app_gerada
```
3. pronto, agora só executar a aplicação e começar a testa

## Como testar a aplicação
Os usuários cadastrados estão no arquivo `TrabDswebApplication`

## Implementações de cada membro
Implementamos todos os requisitos listados no PDF
1. João Vitor
   1. Candidatura a vaga de estágio/trabalho (requer login do profissional via e-mail + senha).
      Ao clicar em uma vaga (requisito R4), o profissional pode se candidatar à vaga. Nesse caso, é
      necessário que ele apresente suas qualificações para a vaga -- pode ser através do upload de
      seu currículo em formato PDF. O sistema deve restrigir que cada candidato se candidate
      apenas uma vez à cada vaga.
   2. Listagem de todas as candidaturas de um profissional (requer login do profissional via email + senha). Depois de fazer login, o profissional pode visualizar todas suas candidaturas
      cadastradas com seus respectivos status.
   3.  Ao término do período de inscrição, inicia-se a fase de análise. A empresa (requer login da
       empresa via e-mail + senha), para cada candidato, deve analisar as suas qualificações
       (currículo) e atualizar o status da candidatura para NÃO SELECIONADO ou ENTREVISTA. Nos
       dois casos, o candidato deve ser informado (via e-mail) sobre a decisão. No caso do status
       ENTREVISTA, a empresa deve também informar um horário para uma entrevista (via
       videoconferência) com o candidato -- o link da videoconferência (google meet, zoom, etc)
       deve estar presente no corpo da mensagem enviada.
   4. REST API -- Retorna a lista de vagas [Read - CRUD]
      GET http://localhost:8080/api/vagas
   5. REST API -- Retorna a vaga de id = {id} [Read - CRUD]
      GET http://localhost:8080/api/vagas/{id}
   6. REST API -- Retorna a lista de vagas (em aberto) da empresa de id = {id} [Read - CRUD]
      GET http://localhost:8080/api/vagas/empresas/{id}
2. José Maia
    1. CRUD 1 de profissionais (requer login de administrador 2 )
    2. Cadastro de vagas de estágio/trabalho (requer login da empresa via e-mail + senha).
       Depois de fazer login, a empresa pode cadastrar uma vaga de estágio/trabalho. O cadastro de
       vagas deve possuir os seguintes dados: CNPJ da empresa, descrição (perfil profissional,
       habilidades desejadas etc), remuneração e data limite de inscrição. O período de
       candidaturas (processo seletivo) para essa vaga encerra-se na data limite de inscrição.
    3. Listagem de todas as vagas (em aberto) em uma única página (não requer login).
       O sistema deve prover a funcionalidade de filtrar as vagas (em aberto) por cidade
    4. Listagem de todas as vagas de uma empresa (requer login da empresa via e-mail +
       senha). Depois de fazer login, a empresa pode visualizar todas suas vagas cadastradas.
    5. Cria um novo profissional [Create - CRUD]
       POST http://localhost:8080/api/profissionais
    6. Retorna a lista de profissionais 2 [Read - CRUD]
       GET http://localhost:8080/api/profissionais
    7. Retorna o profissional de id = {id} [Read - CRUD]
       GET http://localhost:8080/api/profissionais/{id}
    8. Atualiza o profissional de id = {id} [Update - CRUD]
       PUT http://localhost:8080/api/profissionais/{id}
    9. Remove o profissional de id = {id} [Delete - CRUD]
       DELETE http://localhost:8080/api/profissionais/{id}
3. Enzo Hirotani
   1. Empresas (requer login de administrador).
   2. O sistema deve ser internacionalizado em pelo menos dois idiomas: português + outro de
sua escolha
   3.  O sistema deve validar (tamanho, formato, etc) todas as informações (campos nos
formulários) cadastradas e/ou editadas.
   4. REST API -- CRUD de empresas
        1. Cria uma nova empresa [Create - CRUD]
            POST http://localhost:8080/api/empresas
            Body: raw/JSON (application/json)
        2. Retorna a lista de empresas [Read - CRUD]
            GET http://localhost:8080/api/empresas
        3.Retorna a empresa de id = {id} [Read - CRUD]
            GET http://localhost:8080/api/empresas/{id}
        4.Retorna a lista de todas as empresas da cidade de nome = {nome}
            GET http://localhost:8080/api/empresas/cidades/{nome}
        5.Atualiza a empresa de id = {id} [Update - CRUD]
            PUT http://localhost:8080/api/empresas/{id}
            Body: raw/JSON (application/json)
        6.Remove a empresa de id = {id} [Delete - CRUD]
            DELETE http://localhost:8080/api/empresas/{id}

