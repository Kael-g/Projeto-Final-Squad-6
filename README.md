# Projeto Final - Squad 6
<h1 align="center">
      <a href="#" alt="site do ecoleta"> BookCookAPI </a>
</h1>
<h4 align="center">
	🏗️   Em execução  🚧
</h4>

Tabela de conteúdos
=================
<!--ts-->
   * [Sobre o projeto](#-sobre-o-projeto)
   * [Endpoints](#-endpoints)
   * [Validações implementadas na API](#-validacoes-implementadas-na-api)
   * [Estrutura do Projeto](#-estrutura-do-projeto)
   * [Tecnologias](#-tecnologias)
   * [Como executar o projeto](#-como-executar-o-projeto)
     * [Rodando o Backend (Local)](#user-content--rodando-o-backend)
     * [Rodando Testes  (Local)](#user-content--rodando-testes-locais)
     * [Testes Locais](#user-content-testes-locais)
     * [Backend](#user-content-backend)
      * [Infra](#user-content-infra)
   * [Squad](#-squad-garantias-pessoais)
   * [Swagger](#-swagger)
<!--te-->


## 💻 Sobre o projeto

O BookCookAPI fornece uma vasta coleção de receitas culinárias.<br>
Os usuários podem:
* Cadastrar novas receitas;
* Alterar informações das receitas;
* Deletar uma receita pelo nome;
* Pesquisar por nome de receita;
* Pesquisar por ingredientes;
* Pesquisar por filtro de restrição alimentar.
---

## ⚙️ Endpoints
### 1. Cadastrar Receitas
* Endpoint:/api/recipes
* Metodo HTTP : POST 
* Descrição: Cadastro de novas receitas;
* Parâmetros da Solicitação:
```bash
- name (String): O novo nome da receita.
- ingredients (Array de Strings): A nova lista de ingredientes da receita.
- methodPreparation (String): As novas instruções para preparar a receita.
- classification (Arrays de Enum): VEGAN,VEGETARIAN,LACTOSE_FREE,GLUTEN_FREE,NO_CLASSIFICATION;
```
* Exemplo de Corpo da solicitação: 
```bash
POST/api/recipes
```
```bash
{
  "name": "bolo de limao",
  "ingredients": ["limao, farinha, leite"], 
  "methodPreparation": "Instruções de preparação da receita",
  "classifications": ["VEGETARIAN"]
}
```
* Exemplo de Reposta de Sucesso: 
```bash
Status 201 CREATED

{
  "name": "bolo de limao",
  "ingredients": ["limao, farinha, leite"], 
  "methodPreparation": "Instruções de preparação da receita",
 "classifications": ["VEGETARIAN"]
}
```
* Exemplo de Reposta de erro caso cadastrar receitas com o mesmo nome:
```bash
Status 400 BAD REQUEST

{
    "timestamp": "2023-10-10T18:23:51.754+00:00",
    "message": "Já existe uma receita com esse nome: bolo de limao"
}
```

### 2. Alterar informações da receita 
* Endpoint:/api/recipes/
* Metodo HTTP : PUT 
* Descrição: Cadastro de novas receitas;
* Exemplo de solicitação:
```bash
PUT/api/recipes
```
```bash
Irá passar no parâmetro o nome da receita que deseja alterar (query). 
 ```
* Exemplo do corpo de solicitação
```bash
Key : name  
Value: bolo de limao

{
  "name": "alterar nome do bolo",
  "ingredients": ["alterar ou acrescentar ingredientes"], 
  "methodPreparation": "Alterar a Instruções de preparação da receita",
  "classifications": ["alterar ou acrescentar classificação"]
}
```
* Exemplo de Reposta de Sucesso: 
```bash
Status 200 OK

{
  "name": "alterar nome do bolo",
  "ingredients": ["alterar ou acrescentar ingredientes"], 
  "methodPreparation": "Alterar a Instruções de preparação da receita",
  "classifications": ["alterar ou acrescentar classificação"]
}
```
* Exemplo de URL:
```bash
http://localhost:8080/api/recipes/?nome=bolo de limao
```
* Exemplo de Reposta de erro caso não existir a receita:
```bash
"timestamp": "2023-10-10T18:23:51.754+00:00",
"message": "Não existe receita com esse nome"
```
* Exemplo de Reposta de erro caso quiser alterar receita com o mesmo nome:
```bash
"timestamp": "2023-10-10T18:23:51.754+00:00",
"message": "Já existe uma receita com esse nome"
```
### 3. Deletar uma receita
* Endpoint:/api/recipes/
* Metodo HTTP : DELETE 
* Descrição: Deleção de;
* Exemplo de solicitação:
```bash
DELETE/api/recipes
```
```bash
Irá passar no parâmetro o nome da receita que deseja deletar (query).  
```
* Exemplo do corpo de solicitação
```bash
Key : name  
Value: bolo de limao
```
* Exemplo de Reposta de Sucesso: 
```bash
Status 200 OK
Deletado com sucesso
```
* Exemplo de URL:
```bash
http://localhost:8080/api/recipes/?nome=bolo de limao
```
* Exemplo de Reposta de erro caso não existir a receita:
```bash
"timestamp": "2023-10-10T18:23:51.754+00:00",
"message": "Não existe receita com esse nome"
```
### 4. Pesquisar receita por nome.
* Endpoint:/api/recipes/
* Metodo HTTP : GET 
* Descrição: Busca de receita pelo nome;
* Exemplo de solicitação:
```bash
GET/api/recipes
```
```bash
Irá passar no parâmetro o nome da receita que buscar (query).  
```
* Exemplo de solicitação
```bash
Key : name  
Value: bolo de limao
```
* Exemplo de Reposta de Sucesso: 
```bash
Status 200 OK
{
  "name": "bolo de limao",
  "ingredients": ["limao, farinha, leite"], 
  "methodPreparation": "Instruções de preparação da receita",
  "classifications": ["VEGETARIAN"]
}
```
Caso não passar nenhum parâmetro irá retornar todas listas de receitas.
* Exemplo de Reposta de Sucesso: 
```bash
Status 200 OK
{
  "name": "bolo de limao",
  "ingredients": ["limao, farinha, leite"], 
  "methodPreparation": "Instruções de preparação da receita",
  "classifications": ["VEGETARIAN"]
}
{
  "name": "bolo de chocolate",
  "ingredients": ["chocolate, farinha, leite"], 
  "methodPreparation": "Instruções de preparação da receita",
  "classifications": ["VEGETARIAN"]
}
```

* Exemplo de URL:
```bash
http://localhost:8080/api/recipes/?nome=bolo de limao
```
* Exemplo de Reposta de erro caso não existir a receita:
```bash
"timestamp": "2023-10-10T18:23:51.754+00:00",
"message": "Não existe receita com esse nome"
```
### 5. Pesquisar receita por ingredientes.
* Endpoint:/api/recipes/ingredients
* Metodo HTTP : GET 
* Descrição: Busca de receita que contem aqueles ingredientes;
* Exemplo de solicitação:
```bash
GET/api/recipes/ingredients
```
```bash
Irá passar no parâmetro os nomes ingredientes que queira na receita  (query).  
```
* Exemplo do corpo de solicitação
```bash
Key : ingredients  
Value: limao, farinha, leite
```
* Exemplo de Reposta de Sucesso: 
```bash
Status 200 OK
{
  "name": "bolo de limao",
  "ingredients": ["limao, farinha, leite"], 
  "methodPreparation": "Instruções de preparação da receita",
  "classification": "VEGETARIAN"
}
```
* Exemplo de URL:
```bash
http://localhost:8080/api/recipes/ingredients?ingredients=limao, farinha, leite
```
* Caso queira pesquisar receitas que contém uns dos ingredientes passados (query)
```bash
Irá passar no parâmetro o nome do ingrediente e o tipo de pesquisa (query).  
```
* Exemplo de solicitação
```bash
Key : ingredients  
Value: farinha, leite
Key: searchType 
Value : Exact 
```
* Exemplo de Reposta de Sucesso: 
```bash
Status 200 OK
{
  "name": "bolo de limao",
  "ingredients": ["limao, farinha, leite"], 
  "methodPreparation": "Instruções de preparação da receita",
  "classification": "VEGETARIAN"
}
{
  "name": "bolo de chocolate",
  "ingredients": ["chocolate, farinha, leite"], 
  "methodPreparation": "Instruções de preparação da receita",
  "classification": "VEGETARIAN"
}
```
* Exemplo de URL:
```bash
http://localhost:8080/api/recipes/ingredients?ingredients=farinha, leite&searchType=EXACT
```
* Exemplo de Reposta de erro caso não existir os ingredientes:
```bash
"timestamp": "2023-10-10T18:23:51.754+00:00",
"message": "Não existe receita com esse ingrediente"
```
* Exemplo de Reposta de erro caso passar um parâmetro vazio:
```bash
"timestamp": "2023-10-10T18:23:51.754+00:00",
"message": "A busca está vazia, favor insira os ingredientes para busca"
```

### 6. Pesquisar por filtro de restrição.
* Endpoint:/api/recipes/classifications
* Metodo HTTP : GET 
* Descrição: Busca de receita que contem as restrições solicitadas;
* Exemplo de solicitação:
```bash
GET/api/recipes/classifications
```
```bash
Irá passar no parâmetro as classificações ingredientes que queira na receita  (query).  
```
* Exemplo do corpo de solicitação
```bash
Key : classifications  
Value: VEGETARIAN
```
* Exemplo de Reposta de Sucesso: 
```bash
Status 200 OK
{
  "name": "bolo de limao",
  "ingredients": ["limao, farinha, leite"], 
  "methodPreparation": "Instruções de preparação da receita",
  "classification": "VEGETARIAN"
}
{
  "name": "bolo de chocolate",
  "ingredients": ["chocolate, farinha, leite"], 
  "methodPreparation": "Instruções de preparação da receita",
  "classification": "VEGETARIAN"
}
```
* Exemplo de URL:
```bash
http://localhost:8080/api/recipes/searchByClassification?classification=VEGAN,VEGETARIAN
```
* Exemplo de Reposta de erro caso não existir receitas com aquela classificação:
```bash
"timestamp": "2023-10-10T18:23:51.754+00:00",
"message": "Não existem receitas compatíveis com a busca"
```
* Exemplo de Reposta de erro caso passar um parâmetro vazio:
```bash
"timestamp": "2023-10-10T18:23:51.754+00:00",
"message": "Busca por restrições deve conter ao menos uma restrição alimentar"
```
* Exemplo de Reposta de erro caso passar uma restrição invalida:
```bash
"timestamp": "2023-10-10T18:23:51.754+00:00",
"message": "Restrição inválida"
```
---


## ✔️ Validações implementadas na API
<details>
  <summary>Click to expand!</summary>
  1. A não permição de cadastro com receitas com o mesmo nome <br>
  2. A busca deve reconhecer letras maiusculas e minusculas <br>
  3. Fazer todos tratamentos de erros  <br>
  4. Não permitir cadastro com nomes vazios <br>
</details>

---

## 📋 Estrutura do Projeto

<details>
  <summary>Click to expand!</summary>
	<b> 📁 src -> main -> java ->com.projetoSquad6.ApiReceitas</b><br>
-  <b>📁 config</b><br>
   1. <br> 	
-  <b>📁 controller </b><br>
   1.RepcipesController<br>
   2.AutenticationController<br>	
- <b> 📁 enuns </b><br>
   1.	ClassificationEnum<br>
   2.UserRole<br>
- <b>📁  Exceptions </b><br>
   1.CustomExceptionHandler<br>
   2.HandleNoFoundIngredients<br>
   3.HandleRecipeExistsByName<br>
   4.HandleRecipeNoExistsByName<br>
- <b> 📁 mapper </b><br>
   1.ClassificationMapper<br>
   2.RecipesMapper<br>
- <b>📁  model </b><br>
   📁1.dto<br>
   1.1 RecipesDto<br>
   1.2 AuthenticationDto<br>
   1.3 LoginResponseDto<br>
   1.4 RegisterDto<br>
   2.RecipesModel<br>
   3.UserModel<br>
- <b> 📁 repository </b><br>
   1.RecipesRepository<br> 
   2.UserRepository<br>
- <b> 📁 service </b><br>
   1.RecipesService<br> 
   2.UserRepository<br>
- <b>📁 security </b><br>
   1.SecurityConfigurations<br>
   2.SecurityFilter<br>
   3.TokenService<br>

<b>📁src -> main -> test-> java ->com.projetoSquad6.ApiReceitas</b><br>
-<b>📁  repositoryTest </b><br>
   1.RecipesRepositoryTest<br> 
   2.UserRepositoryTest<br>
-<b>📁  serviceTest </b><br>
   1.RecipesServiceTest<br> 
   2.UserRepositoryTest<br>

<b>Application</b><br>
-   core<br>
   1.ApiReceitasApplication<br>
   2.ApiReceitasApplicationTests<br>
-   ports<br>
   1.http://localhost:8080/
</details>

---
## 🛠 Tecnologias

As seguintes ferramentas foram usadas na construção do projeto:

#### **Testes Locais** 

-   Mockito
-   JUnit
-   Spring testing


#### **Backend**

-   Java 11
-   Maven 
-   Spring boot 2.7.16 
-   Lombok , Spring Data JPA , Spring Web , PostgreSQL Driver , Spring Security, Spring Validation


#### **Infra**

-   Docker-compose

#### **Utilitários**

-   GitBash:  **[GitBash](https://git-scm.com/about)**
-   Editor: **[Intellij 2022.x](https://www.jetbrains.com/pt-br/idea/)**


-------
## 🚀 Como executar o projeto
 
1. Backend (src/main/java)

```bash

# Clone este repositório
$ git clone <https://github.com/Kael-g/Projeto-Final-Squad-6.git>

# Configurando maven IDE
$ File > Settings (ou IntelliJ IDEA > Preferences em sistemas macOS)> SettingsBuild, Execution, Deployment > Build Tools > Maven
$ No campo "Maven home directory", selecione o diretório onde o Maven está instalado.
$ Clique em OK para salvar as configurações.

# Configurando o banco de dados
$ Edite o arquivo de configuração da sua aplicação (por exemplo, application.properties ou application.yml) para definir as propriedades de conexão com o banco de dados, como URL, nome de usuário e senha.

# Rodar a aplicação
$ IntelliJ IDEA: Clique com o botão direito no arquivo principal do aplicativo (geralmente a classe que contém o método main) e selecione "Run" ou "Debug".

```
2. Testes Locais (src/test/java)

```bash

# Clone este repositório
$ git clone <https://github.com/Kael-g/Projeto-Final-Squad-6.git>

# Configurando maven IDE
$ File > Settings (ou IntelliJ IDEA > Preferences em sistemas macOS)> SettingsBuild, Execution, Deployment > Build Tools > Maven
$ No campo "Maven home directory", selecione o diretório onde o Maven está instalado.
$ Clique em OK para salvar as configurações.

# Rodando os testes
$ Navegue até a pasta onde os testes estão localizados (src/test/java)
$ Selecione o arquivo de teste que você deseja executar.
$ Clique com o botão direito no arquivo de teste e escolha a opção "Run".
```
3. Infra
____

## 🦸 Squad 

Product Owner
- Lead 1 - Diego de Sena Bastos

Teach Lead's

- Lead 1 - Carolina de Carvalho Queiroz
- Lead 2 - Joyce Olympio Fonseca

Desenvolvido por:

- Dev1 - Iris Romanelli Perroni
- Dev2 - Mikael Mesquita Gualdi
- Dev3 - Pedro Robson De Oliveira
____

## 📝 Swagger
Consulte a documentação Swagger para obter informações detalhadas sobre a API.
http://localhost:8080/swagger-ui.html

