# Smart Email Writer - Backend API

AI-powered email reply generation REST API built with Spring Boot and Google Gemini AI.

## 🚀 Overview

This Spring Boot application provides intelligent email reply generation using Google's Gemini 2.0 Flash model. It analyzes incoming email content, detects context and tone, then generates contextually appropriate professional responses.

## 🛠️ Tech Stack

- **Java 17** - Programming language
- **Spring Boot 3.4.1** - Application framework
- **Spring WebFlux** - Reactive web framework for async operations
- **Google Gemini 2.0 Flash API** - AI model for text generation
- **Maven** - Build and dependency management
- **Jackson** - JSON processing
- **Lombok** - Code generation (getters/setters)

## 🏗️ Architecture & Flow

```
Client Request → Controller → Service Layer → Context Detection → Gemini API → Response Processing → Client
```

### Key Components:

1. **EmailGeneratorController** - REST endpoints for API access
2. **EmailGeneratorService** - Core business logic and Gemini integration
3. **EmailContextService** - Smart tone and context detection
4. **CorsConfig** - Cross-origin configuration for web/extension access

### AI Integration Flow:

1. **Input Processing** - Receives email content and optional tone
2. **Context Analysis** - Detects urgency, gratitude, scheduling needs, etc.
3. **Prompt Engineering** - Builds optimized prompt for Gemini API
4. **AI Generation** - Calls Gemini 2.0 Flash with structured request
5. **Response Parsing** - Extracts and cleans AI-generated content
6. **Output Delivery** - Returns professional email reply

## 📋 Prerequisites

- **Java 17+** installed
- **Maven 3.6+** (or use included wrapper)
- **Google Gemini API Key** ([Get one here](https://aistudio.google.com/app/apikey))
- **Internet connection** for Gemini API calls

## ⚙️ Setup & Installation

### 1. Clone Repository
```bash
git clone https://github.com/kshitiz-access/smart-email-writer-sb.git
cd smart-email-writer-sb
```

### 2. Configure API Key

**Option A: Environment Variable (Recommended)**
```bash
export GEMINI_KEY="your-gemini-api-key-here"
```

**Option B: Create .env file**
```bash
echo "GEMINI_KEY=your-gemini-api-key-here" > .env
```

**Option C: Create run script**
```bash
echo '#!/bin/bash
export GEMINI_KEY="your-gemini-api-key-here"
./mvnw spring-boot:run' > run.sh
chmod +x run.sh
```

### 3. Build & Run
```bash
# Using Maven wrapper (recommended)
./mvnw clean compile
./mvnw spring-boot:run

# Or using run script
./run.sh

# Or with direct environment variable
GEMINI_KEY="your-key" ./mvnw spring-boot:run
```

## 🌐 API Endpoints

### Health Check
```http
GET /api/email/health
```
**Response:** `Email Writer API is running!`

### Generate Email Reply
```http
POST /api/email/generate
Content-Type: application/json

{
  "emailContent": "Hi, I need to reschedule our meeting due to an emergency.",
  "tone": "apologetic"
}
```

**Supported Tones:**
- `professional` (default)
- `casual`
- `friendly` 
- `apologetic`
- `urgent`

**Example Response:**
```
I understand the situation. No problem at all - emergencies happen. 

Please let me know your availability for next week and I'll adjust my schedule accordingly.
```

### Test Endpoint
```http
POST /api/email/test
```
Tests the system with a predefined email scenario.

## 🧪 Testing

### Manual Testing
```bash
# Health check
curl http://localhost:8080/api/email/health

# Email generation
curl -X POST http://localhost:8080/api/email/generate \
  -H "Content-Type: application/json" \
  -d '{
    "emailContent": "Hi, can we schedule a meeting tomorrow?",
    "tone": "professional"
  }'

# Quick test
curl -X POST http://localhost:8080/api/email/test
```

### Unit Tests
```bash
./mvnw test
```

## 📁 Project Structure

```
src/main/java/com/email/writer/
├── EmailWriterSbApplication.java    # Main Spring Boot application
└── app/
    ├── EmailGeneratorController.java # REST API endpoints
    ├── EmailGeneratorService.java   # Core business logic & Gemini integration
    ├── EmailContextService.java     # Smart context detection
    ├── EmailRequest.java            # Request DTO
    └── CorsConfig.java             # CORS configuration

src/main/resources/
└── application.properties          # Configuration file
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature-name`
3. Commit changes: `git commit -m 'Add feature'`
4. Push to branch: `git push origin feature-name`
5. Submit pull request

---

**Built with ❤️ using Spring Boot and Google Gemini AI**
# Build and run instructions added to README
