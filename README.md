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

## 🔧 Configuration

### application.properties
```properties
# Server Configuration
server.port=${PORT:8080}

# Environment
app.environment=${APP_ENV:development}
app.frontend.url=${FRONTEND_URL:http://localhost:5173}

# Gemini API
gemini.api.url=${GEMINI_URL:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=}
gemini.api.key=${GEMINI_KEY}
```

### Environment Variables
- `GEMINI_KEY` - **Required** - Your Google Gemini API key
- `PORT` - Server port (default: 8080)
- `APP_ENV` - Environment (development/production)
- `FRONTEND_URL` - Frontend URL for CORS (default: http://localhost:5173)

## 🚀 Deployment

### Railway
1. Connect GitHub repository
2. Set environment variable: `GEMINI_KEY=your-key`
3. Deploy automatically

### Render
1. Connect GitHub repository  
2. Build command: `./mvnw clean package`
3. Start command: `java -jar target/*.jar`
4. Environment variable: `GEMINI_KEY=your-key`

### Docker
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🔒 Security

- ✅ API keys stored as environment variables
- ✅ CORS configured for specific origins
- ✅ No sensitive data in logs
- ✅ Input validation and sanitization
- ✅ Error handling without data exposure

## 🎯 Features

### Smart Context Detection
- **Urgency Detection** - Identifies time-sensitive requests
- **Tone Analysis** - Recognizes gratitude, apologies, scheduling needs
- **Automatic Tone Selection** - Chooses appropriate response style

### AI Optimization
- **Prompt Engineering** - Optimized prompts for professional responses
- **Response Filtering** - Removes signatures, greetings, subject lines
- **Length Control** - Keeps responses concise (under 150 words)
- **Error Handling** - Graceful fallbacks for API failures

### Integration Ready
- **CORS Enabled** - Works with web apps and browser extensions
- **RESTful API** - Standard HTTP methods and status codes
- **JSON Responses** - Easy integration with frontend frameworks

## 🐛 Troubleshooting

### Common Issues

**Port 8080 already in use:**
```bash
# Kill existing process
pkill -f spring-boot:run
# Or use different port
PORT=8081 ./mvnw spring-boot:run
```

**API Key not found:**
```bash
# Verify environment variable
echo $GEMINI_KEY
# Or check .env file exists
cat .env
```

**Compilation errors:**
```bash
# Clean and rebuild
./mvnw clean compile
# Check Java version
java -version
```

**Gemini API errors:**
- Verify API key is valid
- Check internet connection
- Ensure API quota is available

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

## 📄 License

MIT License - Feel free to use in your projects!

---

**Built with ❤️ using Spring Boot and Google Gemini AI**
