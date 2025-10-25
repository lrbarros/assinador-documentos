🖋️ API de Assinatura de Documentos


Uma API REST para assinatura digital e verificação de documentos, garantindo integridade, autenticidade e segurança.

📌 Sumário

Endpoints

Modelo de Documento

Exemplos de Requisição

Requisitos

Execução

🚀 Endpoints
1️⃣ Assinar Documento

POST /documetos/asinaturas

Assina digitalmente o documento.

Request Body:

{
  "conteudo": "Texto ou conteúdo do documento"
}


Response:

{
  "conteudo": "Texto ou conteúdo do documento",
  "assinatura": "Base64 da assinatura digital"
}

2️⃣ Verificar Assinatura

POST /documetos/asinaturas/verificar

Verifica se a assinatura de um documento é válida.

Request Body:

{
  "conteudo": "Texto ou conteúdo do documento",
  "assinatura": "Base64 da assinatura digital"
}


Response:

true

3️⃣ Obter Algoritmo de Assinatura

GET /documetos/asinaturas/algoritimo

Retorna o algoritmo utilizado para assinatura.

Response:

"SHA256withRSA"

🗂 Modelo de Documento
public class Documento {
    private String conteudo;
    private String assinatura;

    public Documento(String conteudo, String assinatura) {
        this.conteudo = conteudo;
        this.assinatura = assinatura;
    }

    // getters e setters
}


conteudo: Conteúdo do documento a ser assinado.

assinatura: Assinatura digital codificada em Base64.

💻 Exemplos de Requisição

Assinar Documento:

curl -X POST http://localhost:8080/documetos/asinaturas \
-H "Content-Type: application/json" \
-d '{"conteudo":"Meu documento importante"}'


Verificar Assinatura:

curl -X POST http://localhost:8080/documetos/asinaturas/verificar \
-H "Content-Type: application/json" \
-d '{"conteudo":"Meu documento importante","assinatura":"<assinatura_base64>"}'


Obter Algoritmo:

curl http://localhost:8080/documetos/asinaturas/algoritimo

⚙️ Requisitos

Java 25 ou superior

Spring Boot 3.5.7 ou superior()

Certificado digital (PKCS#12 .pfx)

🏃 Execução

Configure seu certificado digital no KeyStoreUtils.

Compile e execute a aplicação:

./mvnw spring-boot:run


Acesse os endpoints via Postman, cURL ou front-end.

🔐 Observações

Suporta RSA, EC e Ed25519.

Assinatura é codificada em Base64 para transporte seguro.

O algoritmo é definido automaticamente baseado no tipo e tamanho da chave privada.

Ideal para sistemas que exigem compliance em assinatura digital.
