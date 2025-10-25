
# 🖋️ API de Assinatura de Documentos

![Java](https://img.shields.io/badge/Java-25+-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.x-green)
![Status](https://img.shields.io/badge/status-beta-orange)

Uma API REST para **assinatura digital** e **verificação de documentos**, garantindo **integridade**, **autenticidade** e **segurança**.

---

## 📌 Sumário

- [Endpoints](#endpoints)  
- [Modelo de Documento](#modelo-de-documento)  
- [Exemplos de Requisição](#exemplos-de-requisição)  
- [Requisitos](#requisitos)  
- [Execução](#execução)  
- [Observações](#observações)  

---

## 🚀 Endpoints

### 1️⃣ Assinar Documento

**POST** `/documetos/asinaturas`  

Assina digitalmente o documento.

**Request Body:**

json
{
  "conteudo": "Texto ou conteúdo do documento"
}


**Response:**

json
{
  "conteudo": "Texto ou conteúdo do documento",
  "assinatura": "Base64 da assinatura digital"
}


---

### 2️⃣ Verificar Assinatura

**POST** `/documetos/asinaturas/verificar`

Verifica se a assinatura de um documento é válida.

**Request Body:**

json
{
  "conteudo": "Texto ou conteúdo do documento",
  "assinatura": "Base64 da assinatura digital"
}


**Response:**

json
true


---

### 3️⃣ Obter Algoritmo de Assinatura

**GET** `/documetos/asinaturas/algoritimo`

Retorna o algoritmo utilizado para assinatura.

**Response:**

json
"SHA256withRSA"


---

## 🗂 Modelo de Documento

java
public class Documento {
    private String conteudo;
    private String assinatura;

    public Documento(String conteudo, String assinatura) {
        this.conteudo = conteudo;
        this.assinatura = assinatura;
    }

    // getters e setters
}


- `conteudo`: Conteúdo do documento a ser assinado.
- `assinatura`: Assinatura digital codificada em Base64.

---

## 💻 Exemplos de Requisição

**Assinar Documento:**

bash
curl -X POST http://localhost:8080/documetos/asinaturas \
-H "Content-Type: application/json" \
-d '{"conteudo":"Meu documento importante"}'


**Verificar Assinatura:**

bash
curl -X POST http://localhost:8080/documetos/asinaturas/verificar \
-H "Content-Type: application/json" \
-d '{"conteudo":"Meu documento importante","assinatura":"<assinatura_base64>"}'


**Obter Algoritmo:**

bash
curl http://localhost:8080/documetos/asinaturas/algoritimo


---

## ⚙️ Requisitos

- Java 25 ou superior
- Spring Boot 3.5.x ou superior
- Certificado digital de teste auto-assinado incluso no projeto (PKCS#12 `.pfx`) 

---

## 🏃 Execução

1. Configure seu certificado digital no `KeyStoreUtils`.
2. Compile e execute a aplicação:

bash
./mvnw spring-boot:run


3. Acesse os endpoints via Postman, cURL ou front-end.

---

## 🔐 Observações

- Suporta **RSA**, **EC** e **Ed25519**.
- A assinatura é codificada em **Base64** para transporte seguro.
- O algoritmo é definido automaticamente baseado no tipo e tamanho da chave privada.
- Ideal para sistemas que exigem **compliance** em assinatura digital.

---

## 📌 Estrutura de Pastas


src/<br>
├─ main/<br>
│  ├─ java/<br>
│  │  └─ br/com/lrbarros/assinador/<br>
│  │     ├─ controller/AssinaturaDocumetosController.java<br>
│  │     ├─ service/AssinadorService.java<br>
│  │     ├─ model/Documento.java<br>
│  │     └─ util/KeyStoreUtils.java<br>
│  └─ resources/<br>
│     └─ certs/certificado.pfx


---

## 📝 Observações Técnicas

- `AssinadorService` é responsável por inicializar as chaves, assinar e verificar documentos.
- O algoritmo de assinatura é escolhido automaticamente:
    - **RSA >= 3072 bits** → `RSASSA-PSS`
    - **RSA < 3072 bits** → `SHA256withRSA`
    - **EC <= 256 bits** → `SHA256withECDSA`
    - **EC > 256 bits** → `SHA384withECDSA`
    - **Ed25519** → `Ed25519`

- As assinaturas são feitas utilizando `java.security.Signature` com parâmetros corretos para cada algoritmo.
- As exceções relacionadas a criptografia são encapsuladas em `CryptoException`.

