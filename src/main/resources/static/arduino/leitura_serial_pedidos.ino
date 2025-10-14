void setup() {
  Serial.begin(9600);  // Inicia a comunicação serial na mesma taxa usada no Java
  Serial.println("🔌 Arduino pronto! Aguardando dados...");
}

void loop() {
  // Verifica se há dados disponíveis na porta serial
  if (Serial.available() > 0) {
    // Lê o texto enviado pelo Java até o caractere de nova linha (ou fim de buffer)
    String mensagem = Serial.readStringUntil('\n');

    // Remove possíveis espaços ou quebras de linha extras
    mensagem.trim();

    // Exibe no monitor serial o que foi recebido
    Serial.print("📩 Mensagem recebida: ");
    Serial.println(mensagem);

    // (Opcional) Enviar uma confirmação de volta
    Serial.println("✅ Mensagem processada com sucesso!");
  }
}
