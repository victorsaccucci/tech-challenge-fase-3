# HospiTech - Sistema Hospitalar

Sistema de gerenciamento hospitalar desenvolvido com microserviços, focado em segurança e comunicação assíncrona.

## Como executar

Apenas Docker é necessário:

```bash
git clone <repo-url>
cd tech-challenge-fase-3
docker-compose up --build
```

Acesse:
- APIs: http://localhost:8081, http://localhost:8082, http://localhost:8083
- Swagger: http://localhost:8081/swagger-ui.html
- GraphiQL: http://localhost:8082/graphiql
- RabbitMQ: http://localhost:15672 (guest/guest)

## Arquitetura

**Microserviços:**
- **Agendamento** (8081): Gerencia consultas e autenticação
- **Histórico** (8082): Armazena histórico médico via GraphQL
- **Notificações** (8083): Envia lembretes por email

**Stack:**
- Spring Boot 3.5.7 + Spring Security
- JWT para autenticação
- RabbitMQ para mensageria
- PostgreSQL (produção) / H2 (desenvolvimento)
- GraphQL para consultas flexíveis

## Funcionalidades

**Perfis de usuário:**
- **Médicos**: Criam consultas, gerenciam histórico médico
- **Enfermeiros**: Auxiliam no agendamento, visualizam histórico
- **Pacientes**: Visualizam suas consultas e recebem notificações

**Fluxo principal:**
1. Usuário se registra e faz login
2. Médico/enfermeiro agenda consulta
3. Sistema envia notificação automática ao paciente
4. Após consulta, médico registra histórico via GraphQL
5. Dados ficam disponíveis para consultas futuras

## APIs principais

### Autenticação
```bash
# Registrar
POST /api/auth/register
{
  "email": "medico@hospital.com",
  "password": "123456",
  "name": "Dr. João Silva",
  "role": "DOCTOR",
  "crm": "12345"
}

# Login
POST /api/auth/login
{
  "email": "medico@hospital.com",
  "password": "123456"
}
```

### Agendamentos
```bash
# Criar consulta
POST /api/appointments
Authorization: Bearer <token>
{
  "patientId": 1,
  "doctorId": 2,
  "appointmentDate": "2024-12-25T14:00:00",
  "notes": "Consulta de rotina"
}

# Listar consultas do paciente
GET /api/appointments/patient/1
```

### Histórico Médico (GraphQL)
```graphql
# Consultar histórico
query GetPatientHistory {
  patientHistory(patientId: "1") {
    id
    diagnosis
    treatment
    medications
    recordDate
  }
}

# Criar registro
mutation CreateMedicalRecord {
  createMedicalRecord(input: {
    patientId: "1"
    doctorId: "2"
    appointmentId: "1"
    diagnosis: "Hipertensão arterial"
    treatment: "Medicação anti-hipertensiva"
    medications: "Losartana 50mg"
  }) {
    id
    diagnosis
  }
}
```

## Segurança

- **JWT**: Tokens com expiração de 24h
- **Autorização**: Endpoints protegidos por perfil de usuário
- **Spring Security**: Configuração robusta de segurança

## Comunicação Assíncrona

- **RabbitMQ**: Mensagens entre serviços
- **Eventos**: Consulta criada → notificação enviada
- **Email**: Lembretes automáticos aos pacientes

## Monitoramento

- Health checks: `/actuator/health` em cada serviço
- Logs estruturados
- Interface de gerenciamento do RabbitMQ

---

*Desenvolvido para o Tech Challenge Fase 3 - FIAP*