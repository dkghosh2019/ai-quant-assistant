# AI Quant Assistant

## Overview
**AI Quant Assistant** is a full-stack AI-powered trading assistant.  
It combines a **Spring Boot backend** integrated with **local LLMs via Ollama** and an **Angular frontend** for interactive chat.

The application is designed to demonstrate practical AI engineering, LLM orchestration, and trading-focused functionality, making it a strong portfolio project for AI Engineer or Quant roles.

---

## Features
- **Backend (Spring Boot + Ollama LLM)**
    - RESTful chat endpoints (`GET` & `POST`)
    - Layered architecture: Controller → Service → Config → LLM
    - Externalized system prompts via `application.yml`
    - Structured logging
    - Ready for conversation memory, streaming, and trade analysis

- **Frontend (Angular)**
    - Interactive chat UI
    - Displays AI responses in real-time
    - Input validation and error handling
    - Ready for enhancements like trade logs, charts, and dashboards

---

## Tech Stack
- **Backend:** Java 21, Spring Boot, Spring AI, Ollama (local LLM)
- **Frontend:** Angular, TypeScript, HTML/CSS
- **Build Tools:** Maven (backend), npm/Angular CLI (frontend)
- **Logging:** SLF4J / Logback

---

## Folder Structure
ai-quant-assistant/ <-- Root Git repository
├─ backend/ <-- Spring Boot backend
│ ├─ src/
│ ├─ pom.xml
├─ frontend/ <-- Angular frontend
│ ├─ src/
│ ├─ package.json
│ ├─ angular.json
├─ .gitignore
├─ README.md <-- This root README


## Getting Started

### Prerequisites
- Java 21
- Maven
- Node.js & npm
- Angular CLI
- Ollama installed with a local LLM model (e.g., llama3)

### Running the Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
****


### Running the Frontend
```bash
cd frontend
npm install
ng serve



UI available at: http://localhost:4200

Testing Chat Endpoints

GET: http://localhost:8080/api/chat?message=Hello AI
{
  "message": "Explain risk reward ratio in trading"
}

POST (JSON body):
{
  "message": "Explain risk reward ratio in trading"
}

Roadmap / Next Milestones

Session-based conversation memory

Streaming AI responses

Trade CSV ingestion & analytics

Retrieval-Augmented Generation (RAG) integration

Dockerization for easy deployment

Full-stack portfolio polish with responsive Angular UI

Contribution

This project is primarily a personal portfolio and learning project.
Contributions are welcome via pull requests with clear objectives and tests.

Author

Dipak Ghosh – AI Engineer & Quantitative Trader
GitHub
 | LinkedIn


